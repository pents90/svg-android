S V G   F O R   A N D R O I D
=============================

See NOTICE for license.

Requires the Android SDK and Apache Ant installed to compile.

-- JAR File --

The jar file svg-android.jar is already built and ready for use. Just add to the "libs" directory of your project.

-- BUILD A New Jar File --

Create a file in this directory called 'local.properties' with the single line:

sdk.dir=PATH/TO/ANDROID_SDK

(Replace "PATH/TO/ANDROID_SDK" with the actual path to your Android SDK install).

To build the jar file, run:

ant jar

(Note, Android platform level 4 (Android 1.1) must be present in your Android SDK to build the jar. Alternatively you can edit build.xml, replacing the string "android-4" with "android-7" or whatever other platform level you want to build against.)

-- LIBRARY Project --

To include svgandroid as a library project in your own Android project, see here:

http://developer.android.com/guide/developing/projects/projects-cmdline.html#ReferencingLibraryProject