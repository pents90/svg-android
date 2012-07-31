package com.larvalabs.androidify.wallpaper;

/**
 * Holds the information necessary to zoom in on the android and drift them along with a Ken Burns type of effect in
 * the live wallpaper.
 *
 * @author Larva Labs, LLC
 */
public class ZoomInfo {

    public float scale;
    public float offsetX;
    public float offsetY;
    public int angle;
    public boolean leftEye;
    public float driftAmount;
    public long driftTime;
    public int driftAngle;

    /**
     * Specify the zoom information.
     * @param scale how much to zoom in. A value of 1 means that the width of the head will fit the width of the screen.
     * @param offsetX how much to budge the android off center in the X axis, in units of the android's eye diameter.
     * @param offsetY how much to budge the android off center in the Y axis, in units of the android's eye diameter.
     * @param angle the angle to rotate the android.
     * @param leftEye whether the view should be initially centered on the left eye (true) or right eye (false). This is prior to offsets being applied.
     * @param driftAmount the distance the Android should drift, in units of the android's eye diameter.
     * @param driftTime the amount of time the Android should spend drifting, in milliseconds.
     * @param driftAngle the direction of the drift, in degrees. A value of 0 means drifting to the right.
     */
    public ZoomInfo(float scale, float offsetX, float offsetY, int angle, boolean leftEye, float driftAmount, long driftTime, int driftAngle) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.angle = angle;
        this.leftEye = leftEye;
        this.driftAmount = driftAmount;
        this.driftTime = driftTime;
        this.driftAngle = driftAngle;
    }
}
