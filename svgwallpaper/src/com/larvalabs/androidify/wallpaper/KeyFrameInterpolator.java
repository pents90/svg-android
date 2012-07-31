package com.larvalabs.androidify.wallpaper;

import android.view.animation.Interpolator;

import java.util.ArrayList;

/**
 * An Interpolator that moves linearly between specified key frames. Handy for controlling movements during animations.
 *
 * @author Larva Labs, LLC
 */
public class KeyFrameInterpolator implements Interpolator {

    private static class KeyFrame {
        float time;
        float pos;

        private KeyFrame(float time, float pos) {
            this.time = time;
            this.pos = pos;
        }
    }

    private ArrayList<KeyFrame> keyFrames;
    private int n;

    /**
     * Create a new key frame interpolator with the specified starting and stopping points.
     *
     * @param start the position at the start of the animation. Scaled to 0 and 1, but overshooting is allowed.
     * @param stop the position at the end of the animation. Scaled to 0 and 1, but overshooting is allowed.
     */
    public KeyFrameInterpolator(float start, float stop) {
        keyFrames = new ArrayList<KeyFrame>();
        keyFrames.add(new KeyFrame(0f, start));
        keyFrames.add(new KeyFrame(1f, stop));
        n = keyFrames.size();
    }

    /**
     * Add a key frame to this interpolator. Add them in chronological order!
     *
     * @param time     the time (0 mean start, 1 means end).
     * @param position the position at this time (scaled to 0 and 1, but overshooting is allowed).
     */
    public void addKeyFrame(float time, float position) {
        keyFrames.add(n - 1, new KeyFrame(time, position));
        n = keyFrames.size();
    }

    @Override
    public float getInterpolation(float input) {
        for (int i = 1; i < n; i++) {
            if (input <= keyFrames.get(i).time) {
                float x = (input - keyFrames.get(i - 1).time) / (keyFrames.get(i).time - keyFrames.get(i - 1).time);
                float y = keyFrames.get(i - 1).pos + (keyFrames.get(i).pos - keyFrames.get(i - 1).pos) * x;
                return y;
            }
        }
        return keyFrames.get(n - 1).pos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (KeyFrame keyFrame : keyFrames) {
            sb.append(keyFrame.time + " -> " + keyFrame.pos + "\n");
        }
        return sb.toString();
    }
}
