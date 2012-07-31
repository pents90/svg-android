package com.larvalabs.svgandroid.test;

import android.graphics.*;
import com.larvalabs.svgandroid.SVG;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: matt
 * Date: 5/5/11
 * Time: 11:59 AM
 */
public class SVGTestPair {
    private SVG svg;
    private Bitmap png;
    private Bitmap svgBitmap;

    public SVGTestPair(InputStream pngFileStream, SVG svg, String pairName) throws DimensionMismatchException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        this.png = BitmapFactory.decodeStream(pngFileStream, null, options);
        this.svg = svg;
        Picture picture = svg.getPicture();
/*
        if (picture.getWidth() != png.getWidth() || picture.getHeight() != png.getHeight()) {
            throw new DimensionMismatchException();
        }
*/
        svgBitmap = Bitmap.createBitmap(png.getWidth(), png.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(svgBitmap);
        svg.getPicture().draw(canvas);
        try {
            FileOutputStream fout = new FileOutputStream("/sdcard/" + pairName + ".png");
//        b.compress(CompressFormat.PNG, COMPRESSION_QUALITY_LEVEL, fout);
            svgBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean comparePoints(List<Point> pointsToCompare) {
        for (Point point : pointsToCompare) {
            if (!pointIsSame(point)) {
                System.out.println("PNG and SVG pixels didn't match at point " + point + ": "
                        + Integer.toHexString(png.getPixel(point.x, point.y)) + " vs. "
                        + Integer.toHexString(svgBitmap.getPixel(point.x, point.y)));
                return false;
            }
        }
        return true;
    }

    public boolean pointIsSame(Point point) {
        int pngPixel = png.getPixel(point.x, point.y);
        int svgPixel = svgBitmap.getPixel(point.x, point.y);
        if (pngPixel != svgPixel) {
//            int diffa = Color.alpha(pngPixel) - Color.alpha(svgPixel);
            int diffr = Math.abs(Color.red(pngPixel) - Color.red(svgPixel));
            int diffg = Math.abs(Color.green(pngPixel) - Color.green(svgPixel));
            int diffb = Math.abs(Color.blue(pngPixel) - Color.blue(svgPixel));
            int totalDiff = diffr + diffg + diffb;
            if (totalDiff > 3) {
//                System.out.println("PNG and SVG pixels didn't match at point " + point + ": "
//                        + Integer.toHexString(pngPixel) + " vs. " + Integer.toHexString(svgPixel));
                return false;
            }
        }
        return true;
    }

    public boolean comparePointsOnHorizontalLine(int y, int spacing) {
        ArrayList<Point> pts = new ArrayList<Point>();
        for (int x = 0; x < png.getWidth(); x += spacing) {
            Point point = new Point(x, y);
            if (!pointIsSame(point)) {
                System.out.println("PNG and SVG pixels didn't match at point " + point + ": "
                        + Integer.toHexString(png.getPixel(x, y)) + " vs. " + Integer.toHexString(svgBitmap.getPixel(x, y)));
                return false;
            }
        }
        return true;
    }


    /**
     * Return the amount the svg and the png differ, from 0 -> 1.
     * Because of differences in anti-aliasing and so on some pixels will differ between png sample svg.
     *
     * @return
     */
    public float fractionalDifference() {
        int numPixDiffs = 0;
        for (int x = 0; x < png.getWidth(); x++) {
            for (int y = 0; y < png.getHeight(); y++) {
                if (!pointIsSame(new Point(x, y))) {
//                    System.out.println("PNG and SVG pixels didn't match at point " + x + ", " + y + ": "
//                            + Integer.toHexString(pngPixel) + " vs. " + Integer.toHexString(svgPixel));
                    numPixDiffs++;
                }
            }
        }
        System.out.println("Number pixels different: " + numPixDiffs);
        return (float) numPixDiffs / (png.getWidth() * png.getHeight());
    }

    public static class DimensionMismatchException extends Exception {

    }
}
