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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

@SuppressWarnings("unused")
public interface OnSvgElementListener {

    void onSvgStart(Canvas canvas, RectF bounds);

    void onSvgEnd(Canvas canvas, RectF bounds);

    <T> T onSvgElement(String id, T element, RectF elementBounds, Canvas canvas, RectF canvasBounds, Paint paint);

    <T> void onSvgElementDrawn(String id, T element, Canvas canvas);

}
