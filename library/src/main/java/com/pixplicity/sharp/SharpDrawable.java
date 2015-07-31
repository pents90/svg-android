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

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;

public class SharpDrawable extends PictureDrawable {

    private float mScaleX = 1f;
    private float mScaleY = 1f;

    /**
     * Construct a new drawable referencing the specified picture. The picture
     * may be null. A view may be provided so that it's LayerType is set to
     * LAYER_TYPE_SOFTWARE.
     *
     * @param view    {@link View} that will hold this drawable
     * @param picture The picture to associate with the drawable. May be null.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SharpDrawable(@Nullable View view, Picture picture) {
        super(picture);
        prepareView(view);
    }

    static void prepareView(@Nullable View view) {
        if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Picture picture = getPicture();
        if (picture != null) {
            Rect bounds = getBounds();
            canvas.save();
            canvas.clipRect(bounds);
            canvas.translate(bounds.left, bounds.top);
            canvas.scale(mScaleX, mScaleY, 0, 0);
            canvas.drawPicture(picture);
            canvas.restore();
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        Picture picture = getPicture();
        int width = right - left;
        int height = bottom - top;
        mScaleX = (float) width / (float) picture.getWidth();
        mScaleY = (float) height / (float) picture.getHeight();
        super.setBounds(left, top, right, bottom);
    }

}
