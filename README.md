# Release Notes
1. This project depends on [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock) as a library project.

# TriblerDroid
_NEEDS UPDATING_
To build, 

1. Run arno-build.bat in jni, after installing Android NDK and editing .bat
2. Build .apk from Eclipse (e.g. Run project)
3. Patch .apk to add missing native lib by running patch0.bat in the hack/ directory.
4. Install and run .apk on device

# ORIGINAL
_TOTALLY OBSOLETE?_
To rebuild the zip file and scripts jar:

Use build.xml as an ant script.

NOTE: Project settings (classpath etc) are stored in ".tmp/" and needs to be modified if changes are made to the build path.
