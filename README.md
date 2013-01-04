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
