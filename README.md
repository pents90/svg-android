svg-android
===========

Support for scalable vector graphics in Android

this is fork of the svg-android from https://code.google.com/p/svg-android/

many missing features was added, many bugs was fixed and I hope that only few of new bugs was introduced :-)

new features:
* SVGZ - zipped svg is auto detected and supported (only on seekable input stream, i.e. file or resource)
* colors can be replaced by their id (including hiding objects replacing their color by Color.TRANSPARENT)
* ARC on path is supported
* named colors are supported
* gradient handling improved
* and many other issues fixed

* supports simple text / tspan from [svg-android](https://github.com/michaelnovakjr/svg-android), [svg-android-2 on google code](https://code.google.com/p/svg-android-2/)
* fix pt crash bug

Used in a demo android app - [DaSign from Google Play](https://play.google.com/store/apps/details?id=com.mindon.idea.dasign)
