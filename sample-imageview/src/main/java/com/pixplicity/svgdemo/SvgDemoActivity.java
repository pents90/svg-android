package com.pixplicity.svgdemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.larvalabs.svgandroid.BoundedPicture;
import com.larvalabs.svgandroid.OnSvgElementListener;
import com.larvalabs.svgandroid.SvgParser;

import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SvgDemoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mButton;

    private PhotoViewAttacher mAttacher;
    private SvgParser mSvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_demo);

        mImageView = (ImageView) findViewById(R.id.iv_image);
        mButton = (Button) findViewById(R.id.bt_button);

        mSvg = SvgParser.parseFromResource(getResources(), R.drawable.cartman);

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
        mSvg.setOnElementListener(new OnSvgElementListener() {
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
        BoundedPicture picture = mSvg.getBoundedPicture();

        {
            Drawable drawable = picture.createDrawable(mImageView);
            mImageView.setImageDrawable(drawable);
        }

        {
            Drawable drawable = picture.createDrawable(mButton);
            drawable.setBounds(0, 0, 200, 200);
            mButton.setCompoundDrawables(
                    drawable,
                    null, null, null);
        }

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
