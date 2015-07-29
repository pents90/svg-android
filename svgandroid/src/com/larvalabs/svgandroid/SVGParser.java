package com.larvalabs.svgandroid;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.PictureDrawable;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.zip.GZIPInputStream;

import java.util.StringTokenizer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

	History:
		original version from https://github.com/pents90/svg-android.git
		forked 2012-11-20 by Josef Pavlik josef@pavlik.it
		current git:
		git@github.com:josefpavlik/svg-android.git
		
	changelog:
		2012-11-20 multiple color and visibility changing, bugfixes 
		2012-12-17 path command ARCTO implemented (function drawarc)
		2012-12-20 gradient support for attr 'gradientUnits' and 'spreadMethod' 
		2012-12-20 content bounding-box check enhanced 
		2012-12-21 drawArc bugfix, color handling improvements (color by name, rgb(int,int,int) etc)
		2012-12-27 default color bug fixed, history added
		2013-01-03 gradient forward reference allowed
		2013-01-03 float number parse bug fixed
		2013-01-03 rounded rectangle supported
		2013-01-03 named color handling enhanced, (thanks to Stephen Uhler, http://code.google.com/p/svg-android-2)
		2013-01-03 zipped svg supported - autodetect, only if stream is seekable, works with svg in resources
		
	todo:
		inherit colors and other attributes from parent group
		split svg by layers to map<layerName. picture>
		
 */

/**
 * Entry point for parsing SVG files for Android.
 * Use one of the various static methods for parsing SVGs by resource, asset or input stream.
 * Optionally, a single color can be searched and replaced in the SVG while parsing.
 * You can also parse an svg path directly.
 *
 * @author Larva Labs, LLC
 * @see #getSVGFromResource(android.content.res.Resources, int)
 * @see #getSVGFromAsset(android.content.res.AssetManager, String)
 * @see #getSVGFromString(String)
 * @see #getSVGFromInputStream(java.io.InputStream)
 * @see #parsePath(String)
 */
public class SVGParser {

    static final String TAG = "SVGAndroid";

    public static float density = 1.0f;
    public static AssetManager assets = null;

	static HashMap<String, String> textDynamic = null;
	public static void prepareTexts(HashMap<String, String> texts) {
		textDynamic = texts;
	}

    /**
     * Parse SVG data from an input stream.
     *
     * @param svgData the input stream, with SVG XML data in UTF-8 character encoding.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromInputStream(InputStream svgData) throws SVGParseException {
        return SVGParser.parse(svgData, 0, 0, false);
    }

    /**
     * Parse SVG data from a string.
     *
     * @param svgData the string containing SVG XML data.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromString(String svgData) throws SVGParseException {
        return SVGParser.parse(new ByteArrayInputStream(svgData.getBytes()), 0, 0, false);
    }

    /**
     * Parse SVG data from an Android application resource.
     *
     * @param resources the Android context resources.
     * @param resId     the ID of the raw resource SVG.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromResource(Resources resources, int resId) throws SVGParseException {
        return SVGParser.parse(resources.openRawResource(resId), 0, 0, false);
    }

    /**
     * Parse SVG data from an Android application asset.
     *
     * @param assetMngr the Android asset manager.
     * @param svgPath   the path to the SVG file in the application's assets.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     * @throws IOException       if there was a problem reading the file.
     */
    public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath) throws SVGParseException, IOException {
        InputStream inputStream = assetMngr.open(svgPath);
        SVG svg = getSVGFromInputStream(inputStream);
        inputStream.close();
        return svg;
    }

    /**
     * Parse SVG data from an input stream, replacing a single color with another color.
     *
     * @param svgData      the input stream, with SVG XML data in UTF-8 character encoding.
     * @param idToColor    map ID to color or SVGParser.hide.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromInputStream(InputStream svgData, HashMap<String, Integer> idToColor) throws SVGParseException {
        return SVGParser.parse(svgData, 0, 0, false, idToColor);
    }

    /**
     * Parse SVG data from a string.
     *
     * @param svgData      the string containing SVG XML data.
     * @param idToColor    map ID to color or SVGParser.hide.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromString(String svgData, HashMap<String, Integer> idToColor) throws SVGParseException {
        return SVGParser.parse(new ByteArrayInputStream(svgData.getBytes()), 0, 0, false, idToColor);
    }

    /**
     * Parse SVG data from an Android application resource.
     *
     * @param resources    the Android context
     * @param resId        the ID of the raw resource SVG.
     * @param idToColor    map ID to color or SVGParser.hide.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromResource(Resources resources, int resId, HashMap<String, Integer> idToColor) throws SVGParseException {
        return SVGParser.parse(resources.openRawResource(resId), 0, 0, false, idToColor);
    }

    /**
     * Parse SVG data from an Android application asset.
     *
     * @param assetMngr    the Android asset manager.
     * @param svgPath      the path to the SVG file in the application's assets.
     * @param idToColor    map ID to color or SVGParser.hide.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     * @throws IOException       if there was a problem reading the file.
     */
    public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath, HashMap<String, Integer> idToColor) throws SVGParseException, IOException {
        InputStream inputStream = assetMngr.open(svgPath);
        SVG svg = getSVGFromInputStream(inputStream, idToColor);
        inputStream.close();
        return svg;
    }
    /**
     * Parse SVG data from an input stream, replacing a single color with another color.
     *
     * @param svgData      the input stream, with SVG XML data in UTF-8 character encoding.
     * @param searchColor  the color in the SVG to replace.
     * @param replaceColor the color with which to replace the search color.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromInputStream(InputStream svgData, int searchColor, int replaceColor) throws SVGParseException {
        return SVGParser.parse(svgData, searchColor, replaceColor, false);
    }

    /**
     * Parse SVG data from a string.
     *
     * @param svgData      the string containing SVG XML data.
     * @param searchColor  the color in the SVG to replace.
     * @param replaceColor the color with which to replace the search color.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromString(String svgData, int searchColor, int replaceColor) throws SVGParseException {
        return SVGParser.parse(new ByteArrayInputStream(svgData.getBytes()), searchColor, replaceColor, false);
    }

    /**
     * Parse SVG data from an Android application resource.
     *
     * @param resources    the Android context
     * @param resId        the ID of the raw resource SVG.
     * @param searchColor  the color in the SVG to replace.
     * @param replaceColor the color with which to replace the search color.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     */
    public static SVG getSVGFromResource(Resources resources, int resId, int searchColor, int replaceColor) throws SVGParseException {
        return SVGParser.parse(resources.openRawResource(resId), searchColor, replaceColor, false);
    }

    /**
     * Parse SVG data from an Android application asset.
     *
     * @param assetMngr    the Android asset manager.
     * @param svgPath      the path to the SVG file in the application's assets.
     * @param searchColor  the color in the SVG to replace.
     * @param replaceColor the color with which to replace the search color.
     * @return the parsed SVG.
     * @throws SVGParseException if there is an error while parsing.
     * @throws IOException       if there was a problem reading the file.
     */
    public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath, int searchColor, int replaceColor) throws SVGParseException, IOException {
        InputStream inputStream = assetMngr.open(svgPath);
        SVG svg = getSVGFromInputStream(inputStream, searchColor, replaceColor);
        inputStream.close();
        return svg;
    }


    /**
     * Parses a single SVG path and returns it as a <code>android.graphics.Path</code> object.
     * An example path is <code>M250,150L150,350L350,350Z</code>, which draws a triangle.
     *
     * @param pathString the SVG path, see the specification <a href="http://www.w3.org/TR/SVG/paths.html">here</a>.
     */
    public static Path parsePath(String pathString) {
        return doPath(pathString);
    }

    private static SVG parse(InputStream in, Integer searchColor, Integer replaceColor, boolean whiteMode) throws SVGParseException {
    	return parse(in, searchColor, replaceColor, whiteMode, null);
    }
    
    private static SVG parse(InputStream in, Integer searchColor, Integer replaceColor, boolean whiteMode, HashMap<String, Integer> idToColor) throws SVGParseException {
//        Util.debug("Parsing SVG...");
//    	Log.d("svgparser","stream is markable "+in.markSupported ());
        try {
        	if (in.markSupported()) {
        		in.mark(4);
        		byte[] magic=new byte[2];
        		int r=in.read(magic,0,2);
        		int magicInt=(magic[0]+(((int)magic[1])<<8))&0xffff;
        		in.reset();	
        		if (r==2 && magicInt==GZIPInputStream.GZIP_MAGIC) {
        			Log.d("svgparser","SVG is gzipped");
        			GZIPInputStream gin=new GZIPInputStream(in);
        			in=(InputStream)gin;
        		}
        	}
            long start = System.currentTimeMillis();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            final Picture picture = new Picture();
            SVGHandler handler = new SVGHandler(picture);
            handler.setColorSwap(searchColor, replaceColor);
            handler.setIdToColor(idToColor);
            handler.setWhiteMode(whiteMode);
            xr.setContentHandler(handler);
            xr.parse(new InputSource(in));
			if( null != textDynamic ) {
				textDynamic.clear();
				textDynamic = null;
			}
        Log.d("SVG PARSER","Parsing complete in " + (System.currentTimeMillis() - start) + " ms.");
            SVG result = new SVG(picture, handler.bounds);
            // Skip bounds if it was an empty pic
            if (!Float.isInfinite(handler.limits.top)) {
                result.setLimits(handler.limits);
            }
            return result;
        } catch (Exception e) {
            throw new SVGParseException(e);
        }
    }

    private static NumberParse parseNumbers(String s) {
//        Log.d("svgparser","Parsing numbers from: '" + s + "'");
        int n = s.length();
        int p = 0;
        ArrayList<Float> numbers = new ArrayList<Float>();
        boolean skipChar = false;
        for (int i = 1; i < n; i++) {
            if (skipChar) {
                skipChar = false;
                continue;
            }
            char c = s.charAt(i);
            switch (c) {
                // This ends the parsing, as we are on the next element
                case 'M':
                case 'm':
                case 'Z':
                case 'z':
                case 'L':
                case 'l':
                case 'H':
                case 'h':
                case 'V':
                case 'v':
                case 'C':
                case 'c':
                case 'S':
                case 's':
                case 'Q':
                case 'q':
                case 'T':
                case 't':
                case 'a':
                case 'A':
                case ')': {
                    String str = s.substring(p, i);
                    if (str.trim().length() > 0) {
                        //Util.debug("  Last: " + str);
                        Float f = Float.parseFloat(str);
                        numbers.add(f);
                    }
                    p = i;
                    return new NumberParse(numbers, p);
                }
                case 'e':
                case 'E': {
                	// exponent in float number - skip eventual minus sign following the exponent
                	skipChar=true; 
                	break;
                }
                case '\n':
                case '\t':
                case ' ':
                case ',':
                case '-': {
                    String str = s.substring(p, i);
                    // Just keep moving if multiple whitespace
                    if (str.trim().length() > 0) {
//                        Log.d("svgparser","  Next: " + str);
                        Float f = Float.parseFloat(str);
                        numbers.add(f);
                        if (c == '-') {
                            p = i;
                        } else {
                            p = i + 1;
                            skipChar = true;
                        }
                    } else {
                        p++;
                    }
                    break;
                }
            }
        }
        String last = s.substring(p);
        if (last.length() > 0) {
            //Util.debug("  Last: " + last);
            try {
                numbers.add(Float.parseFloat(last));
            } catch (NumberFormatException nfe) {
                // Just white-space, forget it
            }
            p = s.length();
        }
        return new NumberParse(numbers, p);
    }

    private static NumberParse readTransform(String attr, String type) {
      int i = attr.indexOf(type +"(");
      if( i > -1 ) {
        i += type.length() +1;
        int j = attr.indexOf(")", i);
        if( j > -1 ) {
          NumberParse np = parseNumbers( attr.substring(i, j) );
          if( np.numbers.size() > 0 )
            return np;
        }
      }
      return null;
    }

    private static Matrix parseTransform(String s) {
        Matrix matrix = new Matrix();
        boolean transformed = false;

        if (s.startsWith("matrix(")) {
            NumberParse np = parseNumbers(s.substring("matrix(".length()));
            if (np.numbers.size() == 6) {
                matrix.setValues(new float[]{
                        // Row 1
                        np.numbers.get(0),
                        np.numbers.get(2),
                        np.numbers.get(4),
                        // Row 2
                        np.numbers.get(1),
                        np.numbers.get(3),
                        np.numbers.get(5),
                        // Row 3
                        0,
                        0,
                        1,
                });
                transformed = true;
            }
        }

        NumberParse np = readTransform(s, "scale");
        if( null != np ) {
            float sx = np.numbers.get(0);
            float sy = sx;
            if (np.numbers.size() > 1) {
               sy = np.numbers.get(1);
            }
            matrix.postScale(sx, sy);
  
            transformed = true;
        }

        np = readTransform(s, "skewX");
        if( null != np ) {
            float angle = np.numbers.get(0);
            matrix.preSkew((float) Math.tan(angle), 0);
  
            transformed = true;
        }

        np = readTransform(s, "skewY");
        if( null != np ) {
            float angle = np.numbers.get(0);
            matrix.preSkew(0, (float) Math.tan(angle));
  
            transformed = true;
        }

        np = readTransform(s, "rotate");
        if( null != np ) {
            float angle = np.numbers.get(0);
            float cx = 0;
            float cy = 0;
            if (np.numbers.size() > 2) {
                cx = np.numbers.get(1);
                cy = np.numbers.get(2);
                matrix.preRotate(angle, cx, cy);
            } else {
                matrix.preRotate(angle);
            }
            transformed = true;
        }

        np = readTransform(s, "translate");
        if( null != np ) {
            float tx = np.numbers.get(0);
            float ty = 0;
            if (np.numbers.size() > 1) {
                ty = np.numbers.get(1);
            }
            matrix.postTranslate(tx, ty);

            transformed = true;
        }
        
        return transformed ? matrix : null;
    }

    /**
     * This is where the hard-to-parse paths are handled.
     * Uppercase rules are absolute positions, lowercase are relative.
     * Types of path rules:
     * <p/>
     * <ol>
     * <li>M/m - (x y)+ - Move to (without drawing)
     * <li>Z/z - (no params) - Close path (back to starting point)
     * <li>L/l - (x y)+ - Line to
     * <li>H/h - x+ - Horizontal ine to
     * <li>V/v - y+ - Vertical line to
     * <li>C/c - (x1 y1 x2 y2 x y)+ - Cubic bezier to
     * <li>S/s - (x2 y2 x y)+ - Smooth cubic bezier to (shorthand that assumes the x2, y2 from previous C/S is the x1, y1 of this bezier)
     * <li>Q/q - (x1 y1 x y)+ - Quadratic bezier to
     * <li>T/t - (x y)+ - Smooth quadratic bezier to (assumes previous control point is "reflection" of last one w.r.t. to current point)
     * </ol>
     * <p/>
     * Numbers are separate by whitespace, comma or nothing at all (!) if they are self-delimiting, (ie. begin with a - sign)
     *
     * @param s the path string from the XML
     */
    private static Path doPath(String s) {
        int n = s.length();
        ParserHelper ph = new ParserHelper(s, 0);
        ph.skipWhitespace();
        Path p = new Path();
        float lastX = 0;
        float lastY = 0;
        float lastX1 = 0;
        float lastY1 = 0;
        float subPathStartX = 0;
        float subPathStartY = 0;
        char prevCmd = 0;
        while (ph.pos < n) {
            char cmd = s.charAt(ph.pos);
            switch (cmd) {
                case '-':
                case '+':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (prevCmd == 'm' || prevCmd == 'M') {
                        cmd = (char) (((int) prevCmd) - 1);
                        break;
                    } else if ( ("lhvcsqta").indexOf( Character.toLowerCase(prevCmd)) >= 0 ) {
                        cmd = prevCmd;
                        break;
                    }
                default: {
                    ph.advance();
                    prevCmd = cmd;
                }
            }

            boolean wasCurve = false;
            switch (cmd) {
                case 'M':
                case 'm': {
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 'm') {
                        subPathStartX += x;
                        subPathStartY += y;
                        p.rMoveTo(x, y);
                        lastX += x;
                        lastY += y;
                    } else {
                        subPathStartX = x;
                        subPathStartY = y;
                        p.moveTo(x, y);
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                case 'Z':
                case 'z': {
                    p.close();
                    p.moveTo(subPathStartX, subPathStartY);
                    lastX = subPathStartX;
                    lastY = subPathStartY;
                    lastX1 = subPathStartX;
                    lastY1 = subPathStartY;
                    wasCurve = true;
                    break;
                }
                case 'T':
                case 't':
                	// todo - smooth quadratic Bezier (two parameters)
                case 'L':
                case 'l': {
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 'l') {
                        p.rLineTo(x, y);
                        lastX += x;
                        lastY += y;
                    } else {
                        p.lineTo(x, y);
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                case 'H':
                case 'h': {
                    float x = ph.nextFloat();
                    if (cmd == 'h') {
                        p.rLineTo(x, 0);
                        lastX += x;
                    } else {
                        p.lineTo(x, lastY);
                        lastX = x;
                    }
                    break;
                }
                case 'V':
                case 'v': {
                    float y = ph.nextFloat();
                    if (cmd == 'v') {
                        p.rLineTo(0, y);
                        lastY += y;
                    } else {
                        p.lineTo(lastX, y);
                        lastY = y;
                    }
                    break;
                }
                case 'C':
                case 'c': {
                    wasCurve = true;
                    float x1 = ph.nextFloat();
                    float y1 = ph.nextFloat();
                    float x2 = ph.nextFloat();
                    float y2 = ph.nextFloat();
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if (cmd == 'c') {
                        x1 += lastX;
                        x2 += lastX;
                        x += lastX;
                        y1 += lastY;
                        y2 += lastY;
                        y += lastY;
                    }
                    p.cubicTo(x1, y1, x2, y2, x, y);
                    lastX1 = x2;
                    lastY1 = y2;
                    lastX = x;
                    lastY = y;
                    break;
                }
                case 'Q':
                case 'q':
                	// todo - quadratic Bezier (four parameters)
                case 'S':
                case 's': {
                    wasCurve = true;
                    float x2 = ph.nextFloat();
                    float y2 = ph.nextFloat();
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if ( Character.isLowerCase(cmd) ) {
                        x2 += lastX;
                        x += lastX;
                        y2 += lastY;
                        y += lastY;
                    }
                    float x1 = 2 * lastX - lastX1;
                    float y1 = 2 * lastY - lastY1;
                    p.cubicTo(x1, y1, x2, y2, x, y);
                    lastX1 = x2;
                    lastY1 = y2;
                    lastX = x;
                    lastY = y;
                    break;
                }
                case 'A':
                case 'a': {
                    float rx = ph.nextFloat();
                    float ry = ph.nextFloat();
                    float theta = ph.nextFloat();
                    int largeArc = (int) ph.nextFlag();
                    int sweepArc = (int) ph.nextFlag();
                    float x = ph.nextFloat();
                    float y = ph.nextFloat();
                    if ( Character.isLowerCase(cmd) ) {
                        x += lastX;
                        y += lastY;
                    }   
                    drawArc(p, lastX, lastY, x, y, rx, ry, theta, largeArc, sweepArc);
                    lastX = x;
                    lastY = y;
                    break;
                }
            }
            if (!wasCurve) {
                lastX1 = lastX;
                lastY1 = lastY;
            }
            ph.skipWhitespace();
        }
        return p;
    }

    private static float angle(float x1, float y1, float x2, float y2) {
    	
		return (float) Math.toDegrees(Math.atan2(x1, y1) - Math.atan2(x2, y2)) % 360;
    }
    
    private static final RectF arcRectf = new RectF();
    private static final Matrix arcMatrix = new Matrix();
    private static final Matrix arcMatrix2 = new Matrix();

    private static void drawArc(Path p, float lastX, float lastY, float x, float y, float rx, float ry, float theta, int largeArc, int sweepArc) {
//		Log.d("drawArc", "from (" + lastX + "," + lastY + ") to (" + x + ","+ y + ") r=(" + rx + "," + ry + ") theta=" + theta + " flags="+ largeArc + "," + sweepArc);
 
// http://www.w3.org/TR/SVG/implnote.html#ArcImplementationNotes
		
    	if (rx == 0 || ry == 0) {
    		p.lineTo(x, y);
    		return;
		}
		
    	if (x == lastX && y == lastY)
    		return; // nothing to draw
		
    	rx = Math.abs(rx);
    	ry = Math.abs(ry);
		
    	final float thrad = theta * (float) Math.PI / 180;
    	final float st = (float) Math.sin(thrad);
    	final float ct = (float) Math.cos(thrad);
    	
    	final float xc = (lastX - x) / 2;
    	final float yc = (lastY - y) / 2;
    	final float x1t = ct * xc + st * yc;
    	final float y1t = -st * xc + ct * yc;
    	
    	final float x1ts = x1t * x1t;
    	final float y1ts = y1t * y1t;
    	float rxs = rx * rx;
    	float rys = ry * ry;
		
    	float lambda = (x1ts / rxs + y1ts / rys) * 1.001f; // add 0.1% to be sure that no out of range occurs due to limited precision
    	if (lambda > 1) {
    		float lambdasr = (float) Math.sqrt(lambda);
    		rx *= lambdasr;
    		ry *= lambdasr;
    		rxs = rx * rx;
    		rys = ry * ry;
    	}
		
    	final float R = (float) Math.sqrt((rxs * rys - rxs * y1ts - rys * x1ts) / (rxs * y1ts + rys * x1ts))
						* ((largeArc == sweepArc) ? -1 : 1);
    	final float cxt = R * rx * y1t / ry;
    	final float cyt = -R * ry * x1t / rx;
    	final float cx = ct * cxt - st * cyt + (lastX + x) / 2;
    	final float cy = st * cxt + ct * cyt + (lastY + y) / 2;
		
    	final float th1 = angle(1, 0, (x1t - cxt) / rx, (y1t - cyt) / ry);
    	float dth = angle((x1t - cxt) / rx, (y1t - cyt) / ry,	(-x1t - cxt) / rx, (-y1t - cyt) / ry);
		
    	if (sweepArc == 0 && dth > 0)
    		dth -= 360;
    	else if (sweepArc != 0 && dth < 0)
    		dth += 360;
		
		// draw
    	if ((theta % 360) == 0) {
    		// no rotate and translate need
    		arcRectf.set(cx - rx, cy - ry, cx + rx, cy + ry);
    		p.arcTo(arcRectf, th1, dth);
    	} else {
    		// this is the hard and slow part :-)
    		arcRectf.set(-rx, -ry, rx, ry);
		
    		arcMatrix.reset();
    		arcMatrix.postRotate(theta);
    		arcMatrix.postTranslate(cx, cy);
    		arcMatrix.invert(arcMatrix2);
		
    		p.transform(arcMatrix2);
    		p.arcTo(arcRectf, th1, dth);
    		p.transform(arcMatrix);
    	}
    }

	private static NumberParse getNumberParseAttr(String name,
			Attributes attributes) {
        int n = attributes.getLength();
        for (int i = 0; i < n; i++) {
            if (attributes.getLocalName(i).equals(name)) {
                return parseNumbers(attributes.getValue(i));
            }
        }
        return null;
    }

    private static String getStringAttr(String name, Attributes attributes) {
        int n = attributes.getLength();
        for (int i = 0; i < n; i++) {
            if (attributes.getLocalName(i).equals(name)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }

    private static Float getFloatAttr(String name, Attributes attributes) {
        return getFloatAttr(name, attributes, null);
    }

    private static Float getFloatAttr(String name, Attributes attributes, Float defaultValue) {
        String v = getStringAttr(name, attributes);
        if (v == null) {
        	return defaultValue;
        } else if (v.endsWith("px")) {
        	v = v.substring(0, v.length() - 2);
        } else if (v.endsWith("pt")) {
        	v = v.substring(0, v.length() - 2);
          return Float.parseFloat(v) * density + 0.5f;
        } else if (v.endsWith("%")) {
        	v = v.substring(0, v.length() - 1);
        	return Float.parseFloat(v)/100;
        }
//            Log.d(TAG, "Float parsing '" + name + "=" + v + "'");
        return Float.parseFloat(v);
    }

    private static class NumberParse {
        private ArrayList<Float> numbers;
        private int nextCmd;

        public NumberParse(ArrayList<Float> numbers, int nextCmd) {
            this.numbers = numbers;
            this.nextCmd = nextCmd;
        }

        public int getNextCmd() {
            return nextCmd;
        }

        public float getNumber(int index) {
            return numbers.get(index);
        }

    }

    private static class Gradient {
        String id;
        String xlink;
        boolean isLinear;
        float x1, y1, x2, y2;
        float x, y, radius;
        ArrayList<Float> positions = new ArrayList<Float>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        Matrix matrix = null;
        public Shader shader = null;
		public boolean boundingBox = false;
		public TileMode tilemode;
/*
        public Gradient createChild(Gradient g) {
            Gradient child = new Gradient();
            child.id = g.id;
            child.xlink = id;
            child.isLinear = g.isLinear;
            child.x1 = g.x1;
            child.x2 = g.x2;
            child.y1 = g.y1;
            child.y2 = g.y2;
            child.x = g.x;
            child.y = g.y;
            child.radius = g.radius;
            child.positions = positions;
            child.colors = colors;
            child.matrix = matrix;
            if (g.matrix != null) {
                if (matrix == null) {
                    child.matrix = g.matrix;
                } else {
                    Matrix m = new Matrix(matrix);
                    m.preConcat(g.matrix);
                    child.matrix = m;
                }
            }
            child.boundingBox = g.boundingBox;
            child.shader = g.shader;
            child.tilemode = g.tilemode;
            return child;
        }
*/
        public void inherit(Gradient parent) {
            Gradient child = this;
            child.xlink = parent.id;
            child.positions = parent.positions;
            child.colors = parent.colors;
            if (child.matrix == null) {
                child.matrix = parent.matrix;
            } else if (parent.matrix != null) {
            	Matrix m = new Matrix(parent.matrix);
                m.preConcat(child.matrix);
                child.matrix = m;
            }
        }
    }

    private static class StyleSet {
        HashMap<String, String> styleMap = new HashMap<String, String>();

        private StyleSet(String string) {
            String[] styles = string.split(";");
            for (String s : styles) {
                String[] style = s.split(":");
                if (style.length == 2) {
                    styleMap.put(style[0], style[1]);
                }
            }
        }

        public String getStyle(String name) {
            return styleMap.get(name);
        }
    }

    private static class Properties {
        StyleSet styles = null;
        Attributes atts;

        private Properties(Attributes atts) {
            this.atts = atts;
            String styleAttr = getStringAttr("style", atts);
            if (styleAttr != null) {
                styles = new StyleSet(styleAttr);
            }
        }

        public String getAttr(String name) {
            String v = null;
            if (styles != null) {
                v = styles.getStyle(name);
            }
            if (v == null) {
                v = getStringAttr(name, atts);
            }
            return v;
        }

        public String getString(String name) {
            return getAttr(name);
        }

        private Integer rgb(int r, int g, int b) {
        	return ((r&0xff)<<16) | ((g&0xff)<<8) | ((b&0xff)<<0); 
        }

        private int parseNum(String v) throws NumberFormatException {
        	if (v.endsWith("%")) {
            	v = v.substring(0, v.length() - 1);
            	return Math.round(Float.parseFloat(v)/100*255);
            }
            return Integer.parseInt(v);
        }

        
        public Integer getColor(String name) {
			String v= getAttr(name);
			if (v == null) {
                return null;
			} else if (v.startsWith("#")) {
                try {
					int c = Integer.parseInt(v.substring(1), 16);
					if (v.length() == 4) {
						// short form color, i.e. #FFF
						c = (c & 0x0f) * 0x11 + (c & 0xf0) * 0x110 + (c & 0xf00) * 0x1100;
					}
					return c;
                } catch (NumberFormatException nfe) {
                	return null;
                }
			} else if (v.startsWith("rgb(") && v.endsWith(")")) {
				String values[]=v.substring(4,v.length()-1).split(",");
				try {
					return rgb(parseNum(values[0]),parseNum(values[1]),parseNum(values[2]));
				} catch (NumberFormatException nfe) {
					return null;
				} catch ( ArrayIndexOutOfBoundsException e) {
					return null;
				}
			} else {
            	return SVGColors.mapColor(v);
            }
        }

        // convert 0xRGB into 0xRRGGBB
        private int hex3Tohex6(int x) {
            return  (x & 0xF00) << 8 | (x & 0xF00) << 12 |
            (x & 0xF0) << 4 | (x & 0xF0) << 8 |
            (x & 0xF) << 4 | (x & 0xF);
        }

        public Float getFloat(String name, float defaultValue) {
            Float v = getFloat(name);
            if (v == null) {
                return defaultValue;
            } else {
                return v;
            }
        }

        public Float getFloat(String name) {
            String v = getAttr(name);
            if (v == null) {
                return null;
            } else {
                try {
                    return Float.parseFloat(v);
                } catch (NumberFormatException nfe) {
                    return null;
                }
            }
        }
    }

    private static class SVGHandler extends DefaultHandler {

        Picture picture;
        Canvas canvas;
        Paint strokePaint;
        boolean strokeSet = false;
        Stack<Paint> strokePaintStack = new Stack<Paint>();
        Stack<Boolean> strokeSetStack = new Stack<Boolean>();

        Paint fillPaint;
        boolean fillSet = false;
        Stack<Paint> fillPaintStack = new Stack<Paint>();
        Stack<Boolean> fillSetStack = new Stack<Boolean>();
        // Scratch rect (so we aren't constantly making new ones)
        RectF rect = new RectF();
        RectF bounds = null;
        RectF limits = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        Integer searchColor = null;
        Integer replaceColor = null;
		private HashMap<String, Integer> idToColor=null;

        boolean whiteMode = false;

        Stack<Boolean> transformStack = new Stack<Boolean>();
        Stack<Matrix> matrixStack = new Stack<Matrix>();

        HashMap<String, Gradient> gradientMap = new HashMap<String, Gradient>();
        Gradient gradient = null;

		final Stack<SvgText> textStack = new Stack<SvgText>();

        HashMap<String, String> defs = new HashMap<String, String>();
        boolean defsReading = false;

        private SVGHandler(Picture picture) {
            this.picture = picture;

            strokePaint = new Paint();
            strokePaint.setAntiAlias(true);
            strokePaint.setStyle(Paint.Style.STROKE);

            fillPaint = new Paint();
            fillPaint.setAntiAlias(true);
            fillPaint.setStyle(Paint.Style.FILL);

        	matrixStack.push(new Matrix());
        }

        public void setIdToColor(HashMap<String, Integer> idToColor) {
			this.idToColor=idToColor;
		}

		public void setColorSwap(Integer searchColor, Integer replaceColor) {
            this.searchColor = searchColor;
            this.replaceColor = replaceColor;
        }

        public void setWhiteMode(boolean whiteMode) {
            this.whiteMode = whiteMode;
        }

        @Override
        public void startDocument() throws SAXException {
            // Set up prior to parsing a doc
        }

        @Override
        public void endDocument() throws SAXException {
            // Clean up after parsing a doc
        }

        private final Matrix gradMatrix=new Matrix();
        
        private boolean doFill(Properties atts, RectF bounding_box) {
            if ("none".equals(atts.getString("display"))) {
                return false;
            }
            if (whiteMode) {
                fillPaint.setShader(null);
                fillPaint.setColor(Color.WHITE);
                return true;
            }
            String fillString = atts.getString("fill");
            if (fillString != null) {
				if( fillString.startsWith("url(#") ) {
	                // It's a gradient fill, look it up in our map
	                String id = fillString.substring("url(#".length(), fillString.length() - 1);
	                Gradient g=gradientMap.get(id);
	                Shader shader = null;
	                if (g != null) shader=g.shader;
	                if (shader != null) {
	                    //Util.debug("Found shader!");
	                    fillPaint.setShader(shader);
						if( null != bounding_box ) {
							gradMatrix.set(g.matrix);
							if (g.boundingBox) {
		//                    	Log.d("svg", "gradient is bounding box");
								gradMatrix.preTranslate(bounding_box.left, bounding_box.top);
								gradMatrix.preScale(bounding_box.width(), bounding_box.height());
							}
							shader.setLocalMatrix(gradMatrix);
						}
	                    return true;
	                } else {
	                	Log.d(TAG, "Didn't find shader, using black: " + id);
	                    fillPaint.setShader(null);
	                    doColor(atts, Color.BLACK, true, fillPaint);
	                    return true;
	                }
				} else if (fillString.equalsIgnoreCase("none")) {
	            	fillPaint.setShader(null);
	                fillPaint.setColor(Color.TRANSPARENT);
	                return true;
	            } else {
	                fillPaint.setShader(null);
	                Integer color = atts.getColor("fill");
	                if (color != null) {
	                    doColor(atts, color, true, fillPaint);
	                    return true;
	                } else {
	                    Log.d(TAG, "Unrecognized fill color, using black: " + fillString);
	                    doColor(atts, Color.BLACK, true, fillPaint);
	                    return true;
					}
				}
            } else {
                if (fillSet) {
                    // If fill is set, inherit from parent
                    return fillPaint.getColor() != Color.TRANSPARENT;   // optimization
                } else {
                    // Default is black fill
                    fillPaint.setShader(null);
                    fillPaint.setColor(Color.BLACK);
                    return true;
                }
            }
        }
		
        // XXX not done yet
        private boolean doText(Attributes atts, Paint paint) {
            if ("none".equals(atts.getValue("display"))) {
                return false;
            }
            if (atts.getValue("font-size") != null) {
                paint.setTextSize(getFloatAttr("font-size", atts, 10f));
            }
            Typeface typeface = setTypeFace(atts);
            if (typeface != null) {
                paint.setTypeface(typeface);
            }
            Align align = getTextAlign(atts);
            if (align != null) {
                paint.setTextAlign(getTextAlign(atts));
            }
            return true;
        }

        private boolean doStroke(Properties atts) {
            if (whiteMode) {
                // Never stroke in white mode
                return false;
            }
            if ("none".equals(atts.getString("display"))) {
                return false;
            }
            Integer color = atts.getColor("stroke");
            if (color != null) {
                doColor(atts, color, false, strokePaint);
                // Check for other stroke attributes
                Float width = atts.getFloat("stroke-width");
                // Set defaults

                if (width != null) {
                    strokePaint.setStrokeWidth(width);
                }
                String linecap = atts.getString("stroke-linecap");
                if ("round".equals(linecap)) {
                    strokePaint.setStrokeCap(Paint.Cap.ROUND);
                } else if ("square".equals(linecap)) {
                    strokePaint.setStrokeCap(Paint.Cap.SQUARE);
                } else if ("butt".equals(linecap)) {
                    strokePaint.setStrokeCap(Paint.Cap.BUTT);
                }
                String linejoin = atts.getString("stroke-linejoin");
                if ("miter".equals(linejoin)) {
                    strokePaint.setStrokeJoin(Paint.Join.MITER);
                } else if ("round".equals(linejoin)) {
                    strokePaint.setStrokeJoin(Paint.Join.ROUND);
                } else if ("bevel".equals(linejoin)) {
                    strokePaint.setStrokeJoin(Paint.Join.BEVEL);
                }
                strokePaint.setStyle(Paint.Style.STROKE);
                return true;
            }
            return false;
        }

        private Gradient doGradient(boolean isLinear, Attributes atts) {
            Gradient gradient = new Gradient();
            gradient.id = getStringAttr("id", atts);
            gradient.isLinear = isLinear;
            if (isLinear) {
                gradient.x1 = getFloatAttr("x1", atts, 0f);
                gradient.x2 = getFloatAttr("x2", atts, 1f);
                gradient.y1 = getFloatAttr("y1", atts, 0f);
                gradient.y2 = getFloatAttr("y2", atts, 0f);
            } else {
                gradient.x = getFloatAttr("cx", atts, 0f);
                gradient.y = getFloatAttr("cy", atts, 0f);
                gradient.radius = getFloatAttr("r", atts, 0f);
            }
            String transform = getStringAttr("gradientTransform", atts);
            if (transform != null) {
                gradient.matrix = parseTransform(transform);
            }
            String spreadMethod = getStringAttr("spreadMethod", atts);
            if (spreadMethod==null) spreadMethod="pad";
            
            gradient.tilemode=	(spreadMethod.equals("reflect"))?Shader.TileMode.MIRROR:
            					(spreadMethod.equals("repeat"))?Shader.TileMode.REPEAT:
            												    Shader.TileMode.CLAMP;

            String unit = getStringAttr("gradientUnits", atts);
            if (unit==null) unit="objectBoundingBox";
            gradient.boundingBox = !unit.equals("userSpaceOnUse");

            String xlink = getStringAttr("href", atts);
            if (xlink != null) {
                if (xlink.startsWith("#")) {
                    xlink = xlink.substring(1);
                }
                gradient.xlink = xlink;
            }
            return gradient;
        }
        
        private void finishGradients() {
        	for(Gradient gradient : gradientMap.values()) {
                if (gradient.xlink != null) {
                    Gradient parent = gradientMap.get(gradient.xlink);
                    if (parent != null) {
                        gradient.inherit(parent);
                    }
                }
                int[] colors = new int[gradient.colors.size()];
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = gradient.colors.get(i);
                }
                float[] positions = new float[gradient.positions.size()];
                for (int i = 0; i < positions.length; i++) {
                    positions[i] = gradient.positions.get(i);
                }
                if (colors.length == 0) {
               		Log.d("BAD", "BAD gradient, id="+gradient.id);
                }
                if (gradient.isLinear) {
                	gradient.shader= new LinearGradient(gradient.x1, gradient.y1, gradient.x2, gradient.y2, colors, positions, gradient.tilemode);
                } else {
                	gradient.shader= new RadialGradient(gradient.x, gradient.y, gradient.radius, colors, positions, gradient.tilemode);
                }

        	}
        }

        private void doColor(Properties atts, Integer color, boolean fillMode, Paint paint) {
            int c = (0xFFFFFF & color) | 0xFF000000;
            if (searchColor != null && searchColor.intValue() == c) {
                c = replaceColor;
            }
            if (idToColor != null) {
            	String id=atts.getString("id");
            	if (id.length()!=0 && idToColor.containsKey(id)) {
                	c = idToColor.get(id);
            	}
            }
            paint.setShader(null);
            paint.setColor(c);
            Float opacity = atts.getFloat("opacity");
            if (opacity == null) {
                opacity = atts.getFloat(fillMode ? "fill-opacity" : "stroke-opacity");
            }
            if (opacity == null) {
                paint.setAlpha(255);
            } else {
                paint.setAlpha((int) (255 * opacity));
            }
        }

        /**
         * set the path style (if any)
         *  stroke-dasharray="n1,n2,..."
         *  stroke-dashoffset=n
         */
        private void pathStyleHelper(String style, String offset) {
            if (style == null) {
                return;
            }

            if (style.equals("none")) {
                strokePaint.setPathEffect(null);
                return;
            }

            StringTokenizer st = new StringTokenizer(style, " ,");
            int count = st.countTokens();
            float[] intervals = new float[(count&1) == 1 ? count * 2 : count];
            float max = 0;
            float current = 1f;
            int i = 0;
            while(st.hasMoreTokens()) {
                intervals[i++] = current = toFloat(st.nextToken(), current);
                max += current;
            }

            // in svg speak, we double the intervals on an odd count
            for (int start=0; i < intervals.length; i++, start++) {
                max += intervals[i] = intervals[start];
            }

            float off = 0f;
            if (offset != null) {
                try {
                    off = Float.parseFloat(offset) % max;
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

            strokePaint.setPathEffect(new DashPathEffect(intervals, off));
        }

        private static float toFloat(String s, float dflt) {
            float result = dflt;
            try {
                result = Float.parseFloat(s);
            } catch (NumberFormatException e) {
                // ignore
            }
            return result;
        }

		private boolean hidden = false;
        private int hiddenLevel = 0;
        private boolean boundsMode = false;

        private void doLimits2(float x, float y) {
            if (x < limits.left) {
                limits.left = x;
            }
            if (x > limits.right) {
                limits.right = x;
            }
            if (y < limits.top) {
                limits.top = y;
            }
            if (y > limits.bottom) {
                limits.bottom = y;
            }
        }

        final private RectF limitRect = new RectF();

        private void doLimits(RectF box, Paint paint) {
        	Matrix m = matrixStack.peek();
        	m.mapRect(limitRect, box);
        	float width2 = (paint == null) ? 0 : strokePaint.getStrokeWidth() / 2;
        	doLimits2(limitRect.left - width2, limitRect.top - width2);
        	doLimits2(limitRect.right + width2, limitRect.bottom + width2);
		}

        private void doLimits(RectF box) {
        	doLimits(box, null);
        }

        private void pushTransform(Attributes atts) {
            final String transform = getStringAttr("transform", atts);
            boolean pushed = transform != null;
            transformStack.push(pushed);
            if (pushed) {
                final Matrix matrix = parseTransform(transform);
                canvas.save();
                canvas.concat(matrix);
                matrix.postConcat(matrixStack.peek());
                matrixStack.push(matrix);
            }
            	
        }

        private void popTransform() {
            if (transformStack.pop()) {
                canvas.restore();
                matrixStack.pop();
            }
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            // Reset paint opacity
            strokePaint.setAlpha(255);
            fillPaint.setAlpha(255);
            // Ignore everything but rectangles in bounds mode
            if (boundsMode) {
                if (localName.equals("rect")) {
                    Float x = getFloatAttr("x", atts);
                    if (x == null) {
                        x = 0f;
                    }
                    Float y = getFloatAttr("y", atts);
                    if (y == null) {
                        y = 0f;
                    }
                    Float width = getFloatAttr("width", atts);
                    Float height = getFloatAttr("height", atts);
                    bounds = new RectF(x, y, x + width, y + height);
                }
                return;
            }
            boolean forcedToHide=false;
            String id=getStringAttr("id", atts);
//            Log.d("svgparser","id="+id);
            if ( id != null && idToColor != null ) {
            	forcedToHide= idToColor.containsKey(id) && idToColor.get(id)==Color.TRANSPARENT;
            }
            boolean hidden2=hidden || forcedToHide;

            if(!hidden2 && localName.equals("use")) {
                localName = "path";
            }

            if (localName.equals("svg")) {
                int width = (int) Math.ceil(getFloatAttr("width", atts));
                int height = (int) Math.ceil(getFloatAttr("height", atts));
                canvas = picture.beginRecording(width, height);
            } else if (localName.equals("defs")) {
                // Ignore
                defsReading = true;
            } else if (localName.equals("linearGradient")) {
                gradient = doGradient(true, atts);
            } else if (localName.equals("radialGradient")) {
                gradient = doGradient(false, atts);
            } else if (localName.equals("stop")) {
            	if (gradient != null) {
            		Properties props = new Properties(atts);
            		float offset = props.getFloat("offset", 0); 
            		int color = props.getColor("stop-color");
            		float alpha = props.getFloat("stop-opacity", 1);
            		int alphaInt = Math.round(255 * alpha);
            		color |= (alphaInt << 24);
            		gradient.positions.add(offset);
            		gradient.colors.add(color);
                }
            } else if (localName.equals("g")) {
                // Check to see if this is the "bounds" layer
                if ("bounds".equalsIgnoreCase(getStringAttr("id", atts))) {
                    boundsMode = true;
                }
                if (hidden) {
                    hiddenLevel++;
                    //Util.debug("Hidden up: " + hiddenLevel);
                }
                // Go in to hidden mode if display is "none"
                if (  "none".equals(getStringAttr("display", atts)) || forcedToHide ) {
                    if (!hidden) {
                        hidden = true;
                        hiddenLevel = 1;
                        //Util.debug("Hidden up: " + hiddenLevel);
                    }
                }
                pushTransform(atts);
                Properties props = new Properties(atts);

                fillPaintStack.push(new Paint(fillPaint));
                strokePaintStack.push(new Paint(strokePaint));
                fillSetStack.push(fillSet);
                strokeSetStack.push(strokeSet);

                doText(atts, fillPaint);
                doText(atts, strokePaint);
                doFill(props, null);
                doStroke(props);

                fillSet |= (props.getString("fill") != null);
                strokeSet |= (props.getString("stroke") != null);
            } else if (!hidden2 && localName.equals("rect")) {
                Float x = getFloatAttr("x", atts, 0f);
                Float y = getFloatAttr("y", atts, 0f);
                
                Float width = getFloatAttr("width", atts);
                Float height = getFloatAttr("height", atts);
                Float rx = getFloatAttr("rx", atts);
                Float ry = getFloatAttr("ry", atts);
                if (ry==null) ry=rx;
                if (rx==null) rx=ry;
                if (rx==null || rx<0) rx=0f;
                if (ry==null || ry<0) ry=0f;
                if (rx>width/2) rx=width/2;
                if (ry>height/2) ry=height/2;
                pushTransform(atts);
                Properties props = new Properties(atts);
 				rect.set(x, y, x + width, y + height);
                if (doFill(props, rect)) {
                    canvas.drawRoundRect(rect, rx, ry, fillPaint);
                    doLimits(rect);
                }
                if (doStroke(props)) {
                    canvas.drawRoundRect(rect, rx, ry, fillPaint);
                    doLimits(rect, fillPaint);
                }
                popTransform();
            } else if (!hidden2 && localName.equals("line")) {
                Float x1 = getFloatAttr("x1", atts);
                Float x2 = getFloatAttr("x2", atts);
                Float y1 = getFloatAttr("y1", atts);
                Float y2 = getFloatAttr("y2", atts);
                Properties props = new Properties(atts);
                if (doStroke(props)) {
                    pushTransform(atts);
					rect.set(x1, y1, x2, y2);
                    canvas.drawLine(x1, y1, x2, y2, strokePaint);
                    doLimits(rect, strokePaint);
                    popTransform();
                }
			} else if (!hidden2 && (localName.equals("circle") || localName.equals("ellipse"))) {
				Float centerX, centerY, radiusX, radiusY;

				centerX = getFloatAttr("cx", atts);
				centerY = getFloatAttr("cy", atts);
				if (localName.equals("ellipse")) {
					radiusX = getFloatAttr("rx", atts);
					radiusY = getFloatAttr("ry", atts);

				} else {
					radiusX = radiusY = getFloatAttr("r", atts);
                }
				if (centerX != null && centerY != null && radiusX != null && radiusY != null) {
                    pushTransform(atts);
                    Properties props = new Properties(atts);
					rect.set(centerX-radiusX, centerY-radiusY, centerX+radiusX, centerY+radiusY);
                    if (doFill(props, rect)) {
                        canvas.drawOval(rect, fillPaint);
                        doLimits(rect);
                    }
                    if (doStroke(props)) {
                        canvas.drawOval(rect, strokePaint);
                        doLimits(rect, strokePaint);
                    }
                    popTransform();
                }
            } else if (!hidden2 && (localName.equals("polygon") || localName.equals("polyline"))) {
                NumberParse numbers = getNumberParseAttr("points", atts);
                if (numbers != null) {
                    Path p = new Path();
                    ArrayList<Float> points = numbers.numbers;
                    if (points.size() > 1) {
                        pushTransform(atts);
                        Properties props = new Properties(atts);
                        p.moveTo(points.get(0), points.get(1));
                        for (int i = 2; i < points.size(); i += 2) {
                            float x = points.get(i);
                            float y = points.get(i + 1);
                            p.lineTo(x, y);
                        }
                        // Don't close a polyline
                        if (localName.equals("polygon")) {
                            p.close();
                        }
                        p.computeBounds(rect, false);
                        if (doFill(props, rect)) {
                            canvas.drawPath(p, fillPaint);
                            doLimits(rect);
                        }
                        if (doStroke(props)) {
                            canvas.drawPath(p, strokePaint);
                            doLimits(rect, strokePaint);
                        }
                        popTransform();
                    }
                }
            } else if (!hidden2 && localName.equals("path")) {
                String d = getStringAttr("d", atts);

                if(defsReading) {
                  defs.put(id, getStringAttr("d", atts));
                  return;
                } else if(null == d) {
                  String href = getStringAttr("href", atts);
                  if( null != href && href.startsWith("#") ) {
                    href = href.substring(1);
                  }
                  if( null != href && defs.containsKey(href) ) {
                    d = defs.get(href);
                  }
                  if(null == d)
                    return;
                }
                Path p = doPath(d);
                pushTransform(atts);
                Properties props = new Properties(atts);
                p.computeBounds(rect, false);
                if (doFill(props, rect)) {
                    canvas.drawPath(p, fillPaint);
                    doLimits(rect);
                }
                if (doStroke(props)) {
                    canvas.drawPath(p, strokePaint);
                    doLimits(rect, strokePaint);
                }
                popTransform();
            } else if (!hidden2 && localName.equals("text")) {
                if (textStack.isEmpty()) {
                    pushTransform(atts);
                    textStack.push(new SvgText(atts));
                } else {
                    Log.w(TAG, "Cannot process <text> tag nested inside another <text> tag");
                }
            } else if (!hidden2 && localName.equals("tspan")) {
                if (!textStack.isEmpty()) {
                    textStack.push(new SvgText(atts));
                } else {
                    Log.w(TAG, "Cannot process <tspan> tag outside of <text> tag");
                }
            } else if (!hidden2) {
                Log.d(TAG, "UNRECOGNIZED SVG COMMAND: " + localName);
            }
        }

        @Override
        public void characters(char ch[], int start, int length) {
            // no-op
            if (!textStack.isEmpty()) {
                textStack.peek().setText(ch, start, length);
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName)
                throws SAXException {
            if (localName.equals("svg")) {
                picture.endRecording();
                // clear
                defs.clear();
                defs = null;
            } else if (localName.equals("text") || localName.equals("tspan")) {
                if (!textStack.isEmpty()) {
                    SvgText text = textStack.pop();
                    text.render(canvas);
                }
                if (localName.equals("text")) {
                    popTransform();
                }
            } else if (localName.equals("linearGradient") || localName.equals("radialGradient")) {
                if (gradient.id != null) {
                    gradientMap.put(gradient.id, gradient);
                }
            } else if (localName.equals("defs")) {
            	finishGradients();
              defsReading = false;
            } else if (localName.equals("g")) {
                if (boundsMode) {
                    boundsMode = false;
                }
                // Break out of hidden mode
                if (hidden) {
                    hiddenLevel--;
                    //Util.debug("Hidden down: " + hiddenLevel);
                    if (hiddenLevel == 0) {
                        hidden = false;
                    }
                }
//                // Clear gradient map
//                gradientRefMap.clear();
                popTransform();
                fillPaint = fillPaintStack.pop();
                fillSet = fillSetStack.pop();
                strokePaint = strokePaintStack.pop();
                strokeSet = strokeSetStack.pop();
            }
        }
  
		// class to hold text properties
        private class SvgText {
            private final static int MIDDLE = 1;
            private final static int TOP = 2;
            private Paint stroke = null, fill = null;
            private float x, y;
            private String svgText;
            private int vAlign = 0;

            public SvgText(Attributes atts) {
                // Log.d(TAG, "text");
                x = getFloatAttr("x", atts, 0f);
                y = getFloatAttr("y", atts, 0f);
                svgText = null;

                Properties props = new Properties(atts);
                if (doFill(props, null)) {
                    fill = new Paint(fillPaint);
                    doText(atts, fill);
                }
                if (doStroke(props)) {
                    stroke = new Paint(strokePaint);
                    doText(atts, stroke);
                }
                // quick hack
                String valign = getStringAttr("alignment-baseline", atts);
                if ("middle".equals(valign)) {
                    vAlign = MIDDLE;
                } else if ("top".equals(valign)) {
                    vAlign = TOP;
                }
            }

            public void setText(char[] ch, int start, int len) {
                if (svgText == null) {
                    svgText = new String(ch, start, len);
                } else {
                    svgText += new String(ch, start, len);
                }
				if( null != textDynamic && textDynamic.containsKey(svgText) ) {
					svgText = textDynamic.get(svgText);
				}

                // This is an experiment for vertical alignment
                if (vAlign > 0) {
                    Paint paint = stroke == null ? fill : stroke;
                    Rect bnds = new Rect();
                    paint.getTextBounds(svgText, 0, svgText.length(), bnds);
                    // Log.i(TAG, "Adjusting " + y + " by " + bnds);
                    y += (vAlign == MIDDLE) ? -bnds.centerY() : bnds.height();
                }
            }

            public void render(Canvas canvas) {
                // Log.i(TAG, "Drawing: " + svgText + " " + x + "," + y);
                if (svgText != null) {
                    if (fill != null) {
                        canvas.drawText(svgText, x, y, fill);
                    }
                    if (stroke != null) {
                        canvas.drawText(svgText, x, y, stroke);
                    }
                }
            }
        }

        private Align getTextAlign(Attributes atts) {
            String align = getStringAttr("text-anchor", atts);
            if (align == null) {
                return null;
            }
            if ("middle".equals(align)) {
                return Align.CENTER;
            } else if ("end".equals(align)) {
                return Align.RIGHT;
            } else {
                return Align.LEFT;
            }
        }

        private Typeface setTypeFace(Attributes atts) {
            String face = getStringAttr("font-family", atts);
            String style = getStringAttr("font-style", atts);
            String weight = getStringAttr("font-weight", atts);

            int styleParam = Typeface.NORMAL;
            if ("italic".equals(style)) {
                styleParam |= Typeface.ITALIC;
            }
            if ("bold".equals(weight)) {
                styleParam |= Typeface.BOLD;
            }
            Typeface plain = null;
            if( null != face && null != assets && face.indexOf(".ttf") > 0 ) {
              plain = Typeface.createFromAsset(assets, "fonts/" + face);
              if( null != plain ) {
                return Typeface.create(plain, styleParam);
              }
            }

            // Log.d(TAG, "typeface=" + result + " " + styleParam);
            return Typeface.create(face, styleParam);
        }
    }
}
