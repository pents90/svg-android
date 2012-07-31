package com.larvalabs.androidify.wallpaper;

import android.util.Log;

/**
 * A few utility methods.
 *
 * @author Larva Labs, LLC
 */
public class Util {

    private static boolean debugMode = true;

    private static String tag = "";

    public static void setProjectName(String t) {
        tag = t;
    }

    public static String getProjectTag() {
        return tag;
    }

    public static void setDebugMode(boolean debugMode) {
        Util.debugMode = debugMode;
    }

    public static void debug(String message) {
        if (debugMode) {
            Log.d(tag, message);
        }
    }

    public static void error(String message, Throwable t) {
        Log.e(tag, message, t);
    }

}
