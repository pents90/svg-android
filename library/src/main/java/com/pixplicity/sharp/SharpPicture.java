/*
    Copyright 2011, 2015 Pixplicity, Larva Labs LLC and Google, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Sharp is heavily based on prior work. It was originally forked from
        https://github.com/pents90/svg-android
    And changes from other forks have been consolidated:
        https://github.com/b2renger/svg-android
        https://github.com/mindon/svg-android
        https://github.com/josefpavlik/svg-android
 */

package com.pixplicity.sharp;

import android.graphics.Picture;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Describes a vector Picture object, and optionally its bounds.
 *
 * @author Larva Labs, LLC
 */
public class SharpPicture {

    /**
     * The parsed Picture.
     */
    private Picture mPicture;

    /**
     * These are the bounds for the SVG specified as a hidden "bounds" layer in the SVG.
     */
    private RectF mBounds;

    /**
     * These are the estimated bounds of the SVG computed from the SVG elements while parsing.
     * Note that this could be null if there was a failure to compute limits (ie. an empty SVG).
     */
    private RectF mLimits = null;

    /**
     * Construct a new SVG.
     *
     * @param picture the parsed picture object.
     * @param bounds  the bounds computed from the "bounds" layer in the SVG.
     */
    SharpPicture(Picture picture, RectF bounds) {
        mPicture = picture;
        mBounds = bounds;
    }

    /**
     * Set the limits of the SVG, which are the estimated bounds computed by the parser.
     *
     * @param limits the bounds computed while parsing the SVG, may not be entirely accurate.
     */
    void setLimits(RectF limits) {
        mLimits = limits;
    }

    /**
     * Create a drawable from the SVG with its bounds set to {@code sizeInPixels} such that its
     * aspect ratio is retained. A view may be provided so that it's LayerType is set to
     * LAYER_TYPE_SOFTWARE.
     *
     * @param view         {@link View} that will hold this drawable
     * @param sizeInPixels maximum width or height dimension of the drawable
     * @return the Drawable.
     */
    public SharpDrawable createDrawable(@Nullable View view, int sizeInPixels) {
        SharpDrawable drawable = getDrawable(view);
        float ratio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        if (ratio < 1) {
            drawable.setBounds(0, 0, (int) (sizeInPixels * ratio), sizeInPixels);
        } else {
            drawable.setBounds(0, 0, sizeInPixels, (int) (sizeInPixels / ratio));
        }
        return drawable;
    }

    /**
     * Create a drawable from the SVG. A view may be provided so that it's LayerType is set to
     * LAYER_TYPE_SOFTWARE.
     *
     * @param view {@link View} that will hold this drawable
     * @return the Drawable.
     */
    public SharpDrawable getDrawable(@Nullable View view) {
        return new SharpDrawable(view, mPicture);
    }

    /**
     * Get the parsed SVG picture data.
     *
     * @return the picture.
     */
    public Picture getPicture() {
        return mPicture;
    }

    /**
     * Gets the bounding rectangle for the SVG, if one was specified.
     *
     * @return rectangle representing the bounds.
     */
    public RectF getBounds() {
        return mBounds;
    }

    /**
     * Gets the bounding rectangle for the SVG that was computed upon parsing. It may not be entirely accurate for certain curves or transformations, but is often better than nothing.
     *
     * @return rectangle representing the computed bounds.
     */
    public RectF getLimits() {
        return mLimits;
    }

}
