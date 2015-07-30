package com.larvalabs.androidify.wallpaper;

import android.graphics.Picture;

/**
 * Stores the vector image, the scale and offsets for a certain body part (head, arms, body or legs).
 *
 * @author Larva Labs, LLC
 */
public class Part {

    public Picture picture;
    public float scaleX;
    public float scaleY;
    public float offsetX;
    public float offsetY;

    public Part(Picture picture) {
        this.picture = picture;
        scaleX = 1f;
        scaleY = 1f;
    }
}
