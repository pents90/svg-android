package com.larvalabs.svgandroid;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;

public class SvgDrawable extends PictureDrawable {

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
    public SvgDrawable(@Nullable View view, Picture picture) {
        super(picture);
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
