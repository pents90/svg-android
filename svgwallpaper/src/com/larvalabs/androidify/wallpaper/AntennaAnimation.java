package com.larvalabs.androidify.wallpaper;

import android.os.Handler;

/**
 * Manages the persistent antenna-wiggling animation.
 *
 * @author Larva Labs, LLC
 */
public class AntennaAnimation {

    // Period of wiggling function
    private static final long PERIOD = 10000;
    // Scale of overall wiggle, in degrees
    private static final float WIGGLE_AMOUNT = 10f;
    // Frequency of update
    private static final long FREQUENCY = 100;

    private float leftAngle;
    private float rightAngle;

    private long startTime = 0;

    private Handler handler = new Handler();

    private boolean running = false;

    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            if (running) {
                step();
                handler.postDelayed(this, FREQUENCY);
            }
        }
    };

    private double leftFunction(double x) {
        return Math.cos(1.5 * x) / (Math.cos(0.5 * x) + 2);
    }

    private double rightFunction(double x) {
        return Math.sin(2 * x) / (Math.cos(0.5f * x) + 2);
    }

    private double hoverFunction(double x) {
        return Math.sin(2*x);
    }

    public void step() {
        double x = (System.currentTimeMillis() - startTime) / (double) PERIOD * 2 * Math.PI;
        //Util.debug("X: " + x);
        leftAngle = (float) (leftFunction(x) * WIGGLE_AMOUNT);
        rightAngle = (float) (leftFunction(x) * WIGGLE_AMOUNT);
    }

    public float getAngle(int i) {
        return (i == 0 ? leftAngle : rightAngle);
    }

    public float getOffsetAngle(int i, float offset) {
        double x = (System.currentTimeMillis() - startTime) / (double) PERIOD * 2 * Math.PI + offset;
        return (i == 0 ? (float) (leftFunction(x) * WIGGLE_AMOUNT) : (float) (leftFunction(x) * WIGGLE_AMOUNT));
    }

}
