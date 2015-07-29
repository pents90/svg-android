package com.pixplicity.svgdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.larvalabs.svgandroid.Svg;
import com.larvalabs.svgandroid.SvgParser;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SvgDemoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_demo);

        mImageView = (ImageView) findViewById(R.id.iv_image);

        Svg svg = SvgParser.getSVGFromResource(getResources(), R.drawable.cartman);
        mImageView.setImageDrawable(svg.createDrawable(mImageView));

        mAttacher = new PhotoViewAttacher(mImageView);
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
