Sharp
===========

Sharp is a Scalable Vector Graphics (SVG) implementation for Android. It facilitates loading vector graphics as SharpDrawables, and can effectively be used wherever a conventional image would be displayed, whether it be as a background, ImageView source, inside a StateListDrawable or used as composites in a TextView.

Forked from:  
https://github.com/pents90/svg-android

Merged changes from forks:  
https://github.com/b2renger/svg-android  
https://github.com/mindon/svg-android  
https://github.com/josefpavlik/svg-android

## Sample

[A sample](https://github.com/Pixplicity/svg-android/tree/master/svgdemo) is included in this repository.

It's easy to load an SVG:

    Sharp.loadResource(getResources(), R.drawable.cartman)
         .into(mImageView);

<img src="https://raw.githubusercontent.com/Pixplicity/sharp/master/sample-imageview/screenshots/cartman1.png" width="220" alt="Sample screenshot" />
<img src="https://raw.githubusercontent.com/Pixplicity/sharp/master/sample-imageview/screenshots/cartman2.png" width="220" alt="Sample screenshot" />
<img src="https://raw.githubusercontent.com/Pixplicity/sharp/master/sample-imageview/screenshots/cartman3.png" width="220" alt="Sample screenshot" />

SVGs can be loaded from various sources:

- `loadAsset(AssetManager, String)` loads SVG data from an Android application asset;
- `loadResource(Resources, int)` loads SVG data from an Android application resource;
- `loadString(String)` loads SVG data directly from a String;
- `loadInputStream(InputStream)` loads SVG data from an InputStream (but it's your responsibility to close it afterwards);
- `loadFile(File)` loads SVG data from a File, internally opening and closing a FileInputStream to do so.

Sharp facilitates the application of the resulting drawable as well, through the following methods:

- `into(ImageView)` takes care of loading the SVG into the source of the ImageView, or falling back to setting the background if the view is not an ImageView;
- `intoBackground(View)` takes care of loading the SVG into the View's background;
- `getSharpPicture()` provides a wrapper containing a `Picture` and the SVG bounds and limits;
- `getDrawable()` generates a `SharpDrawable`, which is a subclass of `PictureDrawable` that respects the `SharpPicture` boundaries.

It's recommended to use `into()` or `intoBackground()`, as the View parameter takes care of setting the view's layer type to `View.LAYER_TYPE_SOFTWARE`.

## Why isn't my SVG appearing?

If you're setting your view's drawable manually, instead of using `into()` or `intoBackground()`, be sure to set the view's layer type to `View.LAYER_TYPE_SOFTWARE`.

## Why no hardware acceleration?

Excellent question! Aside from the fact that PictureDrawable doesn't render correctly, paths do not efficiently render in hardware acceleration. Even if it worked, it would have poor performance. [Read this excellent discussion](http://stackoverflow.com/questions/15039829/drawing-paths-and-hardware-acceleration) about why this is, if you're interested.

You don't need to disable hardware acceleration on your entire application. Only *individual views* need to have the layer type changed, and providing your view into `SharpPicture.createDrawable()` takes care of this for you.

## Known issues

1. Group opacity is not applied. In order to allow this to work correctly, the entire group would need to be drawn in a separate picture and applied as a whole. We want to avoid having to do this as this would have a significant performance hit and are open to alternative suggestions.
2. Text size and position isn't accurate. Until we can get this sorted out, convert your text to paths if you want it to appear pixel-perfect.
