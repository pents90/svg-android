package com.larvalabs.svgandroid;

import android.annotation.TargetApi;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;

public class SvgDrawable extends PictureDrawable {

    /**
     * Construct a new drawable referencing the specified picture. The picture
     * may be null. A view may be provided so that it's LayerType is set to
     * LAYER_TYPE_SOFTWARE.
     *
     * @param view    {@link View} that will hold this drawable
     * @param picture The picture to associate with the drawable. May be null.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SvgDrawable(@Nullable View view, Picture picture) {
        super(picture);
        if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

}
