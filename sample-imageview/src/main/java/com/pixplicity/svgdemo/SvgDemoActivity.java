package com.pixplicity.svgdemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.BoundedPicture;
import com.larvalabs.svgandroid.OnSvgElementListener;
import com.larvalabs.svgandroid.SvgParser;

import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SvgDemoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;
    private SvgParser mSvgCartman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_demo);

        mSvgCartman = SvgParser.parseFromResource(getResources(), R.drawable.cartman);

        mImageView = (ImageView) findViewById(R.id.iv_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadSvg();
            }
        });

        //mAttacher = new PhotoViewAttacher(mImageView);
        //mAttacher.setMaximumScale(10f);

        reloadSvg();
    }

    private void reloadSvg() {
        mSvgCartman.setOnElementListener(new OnSvgElementListener() {
            @Override
            public <T> T onSvgElement(String id, T element, Paint paint) {
                if ("shirt".equals(id) || "hat".equals(id) || "pants".equals(id)) {
                    Random random = new Random();
                    paint.setColor(Color.argb(255, random.nextInt(256),
                            random.nextInt(256), random.nextInt(256)));
                }
                return element;
            }
        });
        BoundedPicture picture = mSvgCartman.getBoundedPicture();
        mImageView.setImageDrawable(picture.createDrawable(mImageView));
        //mAttacher.update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_svg_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
