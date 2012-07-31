package com.larvalabs.androidify.wallpaper;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Contains a whole bunch of constants, many of them relating to the exact positions of the android body parts.
 *
 * @author Larva Labs, LLC
 */
public class Constants {

    // Neither x/y nor y/x scale factors can exceed this.
    public static final float MAX_HEAD_RATIO = 1.5f;

    // Use Integer objects for these since otherwise they will be auto-boxed anyways
    public static final Integer HAIR_COLOR_DEFAULT = 0xFF382800;
//    public static final Integer HAIR_COLOR_TEST = 0xFFD5AA2A;
    // The default Android green
    public static final Integer ANDROID_COLOR = 0xFF9FBF3B;

    // As a factor of the droid size in each dimension
    public static final float MARGIN_SIZE = 0.15f;

    // Center X for Android
    public static final float CENTER_X = 250f;

    // Top Y of head for Android (not including antennae)
    public static final float TOP_Y = 72.059f;

    // Head Positions
    public static final PointF POINT_TOP_OF_HEAD = new PointF(CENTER_X, TOP_Y);
    public static final PointF POINT_BOTTOM_OF_HEAD = new PointF(CENTER_X, TOP_Y + 87.576f);
    public static final PointF POINT_LEFT_BOTTOM_OF_HEAD = new PointF(CENTER_X - 109.164f, TOP_Y + 87.576f);
    public static final PointF POINT_RIGHT_BOTTOM_OF_HEAD = new PointF(CENTER_X + 109.164f, TOP_Y + 87.576f);
    public static final PointF POINT_TOP_OF_LEFT_EYE = new PointF(CENTER_X - 46.048f, TOP_Y + 33.219f);
    public static final PointF POINT_TOP_OF_RIGHT_EYE = new PointF(CENTER_X + 46.048f, TOP_Y + 33.219f);
    public static final PointF POINT_BOTTOM_OF_LEFT_EYE = new PointF(CENTER_X - 46.048f, TOP_Y + 57.378f);
    public static final PointF POINT_BOTTOM_OF_RIGHT_EYE = new PointF(CENTER_X + 46.048f, TOP_Y + 57.378f);
    public static final PointF POINT_LEFT_EYE = new PointF(CENTER_X - 46.048f, TOP_Y + (33.219f + 57.378f) / 2);
    public static final PointF POINT_RIGHT_EYE = new PointF(CENTER_X + 46.048f, TOP_Y + (33.219f + 57.378f) / 2);
    public static final PointF POINT_BASE_OF_LEFT_ANTENNA = new PointF(CENTER_X - 41.529f, TOP_Y + 19.841f);
    public static final PointF POINT_BASE_OF_RIGHT_ANTENNA = new PointF(CENTER_X + 41.529f, TOP_Y + 19.841f);
    public static final PointF POINT_TOP_OF_LEFT_ANTENNA = new PointF(CENTER_X - 71.253f, TOP_Y - 23.139f);
    public static final PointF POINT_TOP_OF_RIGHT_ANTENNA = new PointF(CENTER_X + 71.253f, TOP_Y - 23.139f);
    public static final float EYE_HEIGHT = POINT_BOTTOM_OF_LEFT_EYE.y - POINT_TOP_OF_LEFT_EYE.y;

    public static final float HEAD_BOUNDS_TOP = POINT_TOP_OF_RIGHT_ANTENNA.y - (TOP_Y - POINT_TOP_OF_RIGHT_ANTENNA.y);

    public static final RectF DEFAULT_HAIR_BOUNDS = new RectF(POINT_LEFT_BOTTOM_OF_HEAD.x, POINT_TOP_OF_RIGHT_ANTENNA.y, POINT_RIGHT_BOTTOM_OF_HEAD.x, POINT_BOTTOM_OF_HEAD.y);

    // Body Positions
    public static final PointF POINT_TOP_OF_BODY = new PointF(CENTER_X, TOP_Y + 103.43f);
    public static final PointF POINT_TOP_LEFT_OF_BODY = new PointF(CENTER_X - 109.168f, TOP_Y + 103.43f);
    public static final PointF POINT_TOP_RIGHT_OF_BODY = new PointF(CENTER_X + 109.168f, TOP_Y + 103.43f);
    public static final PointF POINT_BOTTOM_OF_BODY = new PointF(CENTER_X, TOP_Y + 300.478f);
    public static final PointF POINT_CENTER_OF_BODY = new PointF(CENTER_X, TOP_Y + (103.43f + 300.478f) / 2);

    // Arm Positions
    public static final PointF POINT_TOP_OF_LEFT_ARM = new PointF(CENTER_X - 147.972f, TOP_Y + 102.675f);
    public static final PointF POINT_TOP_OF_RIGHT_ARM = new PointF(CENTER_X + 147.972f, TOP_Y + 102.675f);
    public static final PointF POINT_BOTTOM_OF_LEFT_ARM = new PointF(CENTER_X - 147.972f, TOP_Y + 249.894f);
    public static final PointF POINT_BOTTOM_OF_RIGHT_ARM = new PointF(CENTER_X + 147.972f, TOP_Y + 249.894f);
    public static final PointF POINT_LEFT_SHOULDER = new PointF(CENTER_X - 147.972f, TOP_Y + 127.595f);
    public static final PointF POINT_LEFT_OF_LEFT_SHOULDER = new PointF(CENTER_X - 171.89f, TOP_Y + 127.595f);
    public static final PointF POINT_RIGHT_OF_LEFT_SHOULDER = new PointF(CENTER_X - 123.181f, TOP_Y + 127.595f);
    public static final PointF POINT_RIGHT_SHOULDER = new PointF(CENTER_X + 147.972f, TOP_Y + 127.595f);
    public static final PointF POINT_RIGHT_OF_RIGHT_SHOULDER = new PointF(CENTER_X + 171.89f, TOP_Y + 127.595f);
    public static final PointF POINT_LEFT_OF_RIGHT_SHOULDER = new PointF(CENTER_X + 123.181f, TOP_Y + 127.595f);
    public static final PointF POINT_LEFT_HAND = new PointF(CENTER_X - 147.972f, TOP_Y + 224.974f);
    public static final PointF POINT_RIGHT_HAND = new PointF(CENTER_X + 147.972f, TOP_Y + 224.974f);
    public static final PointF POINT_CENTER_OF_LEFT_ARM = new PointF(CENTER_X - 147.972f, TOP_Y + (102.675f + 249.894f) / 2);
    public static final PointF POINT_CENTER_OF_RIGHT_ARM = new PointF(CENTER_X + 147.972f, TOP_Y + (102.675f + 249.894f) / 2);

    // Leg Positions
    public static final PointF POINT_TOP_OF_LEFT_LEG_LEFT_SIDE = new PointF(CENTER_X - 67.4f, TOP_Y + 300.478f);
    public static final PointF POINT_TOP_OF_RIGHT_LEG_RIGHT_SIDE = new PointF(CENTER_X + 67.4f, TOP_Y + 300.478f);
    public static final PointF POINT_TOP_OF_LEFT_LEG_RIGHT_SIDE = new PointF(CENTER_X - 18.691f, TOP_Y + 300.478f);
    public static final PointF POINT_TOP_OF_RIGHT_LEG_LEFT_SIDE = new PointF(CENTER_X + 18.691f, TOP_Y + 300.478f);
    public static final PointF POINT_TOP_OF_LEFT_LEG_CENTER = new PointF((POINT_TOP_OF_LEFT_LEG_LEFT_SIDE.x + POINT_TOP_OF_LEFT_LEG_RIGHT_SIDE.x)/2, TOP_Y + 300.478f);
    public static final PointF POINT_TOP_OF_RIGHT_LEG_CENTER = new PointF((POINT_TOP_OF_RIGHT_LEG_LEFT_SIDE.x + POINT_TOP_OF_RIGHT_LEG_RIGHT_SIDE.x)/2, TOP_Y + 300.478f);

    public static final PointF POINT_BOTTOM_OF_LEFT_LEG = new PointF(CENTER_X - 43.046f, TOP_Y + 379.576f);
    public static final PointF POINT_BOTTOM_OF_RIGHT_LEG = new PointF(CENTER_X + 43.046f, TOP_Y + 379.576f);
    public static final PointF POINT_CENTER_OF_LEFT_FOOT = new PointF(CENTER_X - 43.046f, TOP_Y + 355.221f);
    public static final PointF POINT_CENTER_OF_RIGHT_FOOT = new PointF(CENTER_X + 43.046f, TOP_Y + 355.221f);

    // Resize limits (as a fraction of the default Android logo)
    public static final float RESIZE_HEAD_MIN_X = 0.6f;
    public static final float RESIZE_HEAD_MAX_X = 1.8f;
    public static final float RESIZE_HEAD_MIN_Y = 0.6f;
    public static final float RESIZE_HEAD_MAX_Y = 1.8f;

    public static final float RESIZE_BODY_MIN_X = 0.6f;
    public static final float RESIZE_BODY_MAX_X = 1.20f;
    public static final float RESIZE_BODY_MIN_Y = 0.6f;
    public static final float RESIZE_BODY_MAX_Y = 1.5f;

    public static final float RESIZE_ARMS_MIN_X = 0.5f;
    public static final float RESIZE_ARMS_MAX_X = 1.2f;
    public static final float RESIZE_ARMS_MIN_Y = 0.6f;
    public static final float RESIZE_ARMS_MAX_Y = 1.5f;

    public static final float RESIZE_LEGS_MIN_X = 0.4f;
    public static final float RESIZE_LEGS_MAX_X = 1.1f;
    public static final float RESIZE_LEGS_MIN_Y = 0.6f;
    public static final float RESIZE_LEGS_MAX_Y = 3f;

    public static final int[] HAIR_COLORS =
            {
                    0xFF000000, // Black
                    0xFF382800, // Brown
                    0xFF6D5622, // Light Chesnut
                    0xFF99792f, // Light Brown
                    0xFFDD4900, // Carrot/Red
					0xFFfcbe31, // Dirty Blonde
                    0xFFE0DA00, // Blonde
                    0xFF909090, // Gray
                    0xFFF0F0F0, // White
                    0xFF66771c, // Dark Green
                    0xFF0082ff, // Blue
                    0xFFC73D7E, // Hot Pink
                    //0xFFBF4C1F, // Red
            };

    public static final String[] HAIR_COLOR_NAMES = {
            "Black",
            "Brown",
            "Light Chestnut",
            "LightBrown",
            "Red",
            "Dirty Blonde",
            "Blonde",
            "Gray",
            "White",
            "Dark Green",
            "Blue",
            "Hot Pink",
    };

    public static final int[] SKIN_COLORS =
            {
                    0xFF9FBF3B, // Android
                    0xFFFFDEB8,
                    0xFFF2CFAA,
                    0xFFE1C492,
                    0xFFD3B489,
                    0xFFD3A76E,
                    0xFFB8864A,
                    0xFF977446,
                    0xFF5F3E2B,
                    0xFF3F230F,
            };

    public static final String[] SKIN_COLOR_NAMES = {
            "Android Skin",
            "Skin Tone 1",
            "Skin Tone 2",
            "Skin Tone 3",
            "Skin Tone 4",
            "Skin Tone 5",
            "Skin Tone 6",
            "Skin Tone 7",
            "Skin Tone 8",
            "Skin Tone 9",
    };

    public static String getSkinColorName(int color) {
        return lookupColor(color, SKIN_COLORS, SKIN_COLOR_NAMES);
    }

    public static String getHairColorName(int color) {
        return lookupColor(color, HAIR_COLORS, HAIR_COLOR_NAMES);
    }

    private static String lookupColor(int color, int[] colors, String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (color == colors[i]) {
                return names[i];
            }
        }
        return "Unknown";
    }

    public static final int[] PANTS_COLORS =
            {
                    0xFF9FBF3B, // Android (actually just defaults to skin color)
                    0xFF1D2E82, // Navy Blue
                    0xFF3D4CFF, // Electric blue
                    0xFF890909, // Burdeox
                    0xFFC10606, // Red
                    0xFF000000, // Black
                    0xFFF495BC, // Light Pink
                    0xFFBC5383, // Dark Pink
                    0xFFE8D600, // Yellow
                    0xFFFC6C00, // Orange
                    0xFF276927, // Dark Green
                    0xFF332D27, // Brown
                    0xFF4D260C, // Dark Saturated Brown
                    0xFFA5A55E, // All mighty Khaki
                    0xFFEBEBEB, // White
                    0xFF878787, // Grey
                    0xFF271F3A, // Violet
                    0xFF2F71A8, // Dark Sky Blue
            };

}
