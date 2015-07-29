svg-android
===========

Forked from:  
https://github.com/pents90/svg-android

Merged changes from forks:  
https://github.com/b2renger/svg-android  
https://github.com/mindon/svg-android  
https://github.com/josefpavlik/svg-android

## Sample

[A sample](https://github.com/Pixplicity/svg-android/tree/master/svgandroid/svgdemo) is included in this repository.

It's easy to load an SVG:

    BoundedPicture picture = SvgParser.parseFromResource(getResources(), R.drawable.cartman)
                                      .getBoundedPicture();
    mImageView.setImageDrawable(picture.createDrawable(mImageView));

You do not necessarily need to provide the view into `svg.createDrawable()`; the optional View parameter simply takes care of setting the view's layer type to `View.LAYER_TYPE_SOFTWARE`.

## Why no hardware acceleration?

Excellent question! Aside from the fact that PictureDrawable doesn't render correctly, paths do not efficiently render in hardware acceleration. Even if it worked, it would have poor performance. [Read this excellent discussion](http://stackoverflow.com/questions/15039829/drawing-paths-and-hardware-acceleration) about why this is, if you're interested.

You don't need to disable hardware acceleration on your entire application. Only *individual views* need to have the layer type changed, and providing your view into `svg.createDrawable()` takes care of this for you.
