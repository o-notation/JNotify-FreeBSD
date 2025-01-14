JNotify for FreeBSD

0.Changes
- 07.02.2023 version 0.3.2 Renamed subfiles and subfolders weren't correctly handled.
- 13.02.2022 version 0.3.1 makefile replaced gcc replaced by cc
- 08.05.2013 version 0.3.0 Package names adapted. Bug: wrong handled file descriptors in JNI-lib and pathname problem with files, fixed.
- 14.02.2013 version 0.2.1 JNotify-0.94 integrated. Bug: removing unregistered Watchpoint throws NullPointerException, fixed. JNI-Library renamed to libjnotify_freebsd.so to differ from Linux libraries. 
- 14.01.2013 version 0.1.1 Bug in notified filename(path separator) fixed.
- 20.12.2012 version 0.1.0a Archive contains now a precompiled jar.
- 19.04.2012 version 0.1.0 published.

1.Introduction
JNotify is java library for file system events. The library listens to events like file created, file modified, file renamed and file deleted. Supported plattforms are Windows, Linux and Mac OS X(http://jnotify.sourceforge.net/).
This is a extended version of JNotify for FreeBSD since 4.1. All JNotify interfaces are implemented, so that moving from one supported platform to an other platform shouldn't be a problem(don't forget to adjust the filepaths and fileseparator). JNotify is realized with a platform dependend jni-library, but needs no additional java library. The FreeBSD jni-library has to be compiled for every platform and version.
JNotify is tested with: openjdk6, diabolo-jdk1.5.0, diablo-jdk1.6.0 
JNotify is published under LGPL2.1.


2.Archive 
build/ - compilation files
src/c/ - c sourcecode
src/java/ - java sourcecode
target/ - precompiled jni-libraries and jars
makefile - compilation targets
readme.txt - this file
epl.html - License notes
lgpl.txt - License notes

If you are using the precompiled JNI-libraries, please delete the version-notes from the filename.


3.Compiling and Installation
Before compiling the jni-library please ensure that JAVA_HOME is setted (without ending /) and the java executables are in your search path. Extract the archive and change to the new directory. To create the library libjnotify_freebsd.so type "make"; "make install" copies the library to the default path /usr/local/lib. The JVM finds the library in the standard library search path; other paths must be given by the -Djava.library.path=$path parameter.
Additional target: make jar - compiles the jnotify java sourcecode and creates a new jar archive.


4.Running
Starting and listen to the local folder:
java -jar -Djava.library.path=. JNotify.jar .
I extended the starting parameters:
JNotify.jar $path $subtree $eventmask
$path = semikolon separated directory list.
$subtree = 0 no subfolders, 1 with subfolders.
$eventmask = 1 file created, 2 file deleted, 4 file modified, 8 file renamed, 15 all event(deafult)s.


Matthias Zitzen, 11.02.2012 m.zitzen@o-notation.org
http://jnotify.o-notation.org
