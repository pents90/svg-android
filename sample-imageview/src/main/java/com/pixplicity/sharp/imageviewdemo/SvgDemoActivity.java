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

package com.pixplicity.sharp.imageviewdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pixplicity.sharp.OnSvgElementListener;
import com.pixplicity.sharp.Sharp;
import com.pixplicity.sharp.SharpPicture;

import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SvgDemoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mButton;

    private PhotoViewAttacher mAttacher;
    private Sharp mSvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_demo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.iv_image);
        mButton = (Button) findViewById(R.id.bt_button);

        mSvg = Sharp.loadResource(getResources(), R.drawable.cartman);
        // If you want to load typefaces from assets:
        //          .withAssets(getAssets());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadSvg(true);
            }
        });

        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setMaximumScale(10f);

        reloadSvg(false);
    }

    private void reloadSvg(final boolean changeColor) {
        mSvg.setOnElementListener(new OnSvgElementListener() {

            @Override
            public void onSvgStart(Canvas canvas) {
            }

            @Override
            public void onSvgEnd(Canvas canvas) {
            }

            @Override
            public <T> T onSvgElement(String id, T element, Canvas canvas, Paint paint) {
                if (changeColor && ("shirt".equals(id) || "hat".equals(id) || "pants".equals(id))) {
                    Random random = new Random();
                    paint.setColor(Color.argb(255, random.nextInt(256),
                            random.nextInt(256), random.nextInt(256)));
                }
                return element;
            }

            @Override
            public <T> void onSvgElementDrawn(String id, T element, Canvas canvas) {
            }

        });
        SharpPicture picture = mSvg.getSharpPicture();

        {
            Drawable drawable = picture.createDrawable(mImageView);
            mImageView.setImageDrawable(drawable);
        }

        {
            // We don't want to use the same drawable, as we're specifying a custom size; therefore
            // we call createDrawable() instead of getDrawable()
            int iconSize = getResources().getDimensionPixelSize(R.dimen.icon_size);
            Drawable drawable = picture.createDrawable(mButton, iconSize);
            mButton.setCompoundDrawables(
                    drawable,
                    null, null, null);
        }

        mAttacher.update();
    }

}
