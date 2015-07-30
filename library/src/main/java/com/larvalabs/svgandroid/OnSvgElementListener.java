package com.larvalabs.svgandroid;

import android.graphics.Paint;

public interface OnSvgElementListener {

    <T> T onSvgElement(String id, T element, Paint paint);

}
