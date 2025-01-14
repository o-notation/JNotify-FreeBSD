# 2023-02-07
# 2023-08-08	tar: hinzugefuegt
# 2024-06-29	del2 entfernt, da org-Path schon lange nicht mehr existiert
PRE := build
SRC := src/c
INC1 := ${JAVA_HOME}/include
INC2 := ${JAVA_HOME}/include/freebsd
LIB := /usr/local/lib
CC := cc

all: libjnotify_freebsd.so
	
mac: libjnotify_freebsd.jnilib 
	
libjnotify.jnilib: libjnotify.o
	$(CC) -shared -o $@ ${PRE}/libjnotify.o

libjnotify_freebsd.so: libjnotify.o
	$(CC) -shared -o $@ ${PRE}/libjnotify.o
	
libjnotify.o: 
	$(CC) -std=c89 -c -fPIC -o ${PRE}/$@ -I${INC1} -I${INC2} ${SRC}/libjnotify.c

install: all
	cp libjnotify.so ${LIB}
	
java: freebsd linux macosx win32
	javac -d build/classes/ -sourcepath src/java/ src/java/net/contentobjects/jnotify/JNotify.java

freebsd:
	javac -d build/classes/ -sourcepath src/java/ src/java/net/contentobjects/jnotify/freebsd/JNotifyAdapterFreeBSD.java
	
linux:
	javac -d build/classes/ -sourcepath src/java/ src/java/net/contentobjects/jnotify/linux/*.java
	
macosx:
	javac -d build/classes/ -sourcepath src/java/ src/java/net/contentobjects/jnotify/macosx/*.java
	
win32:
	javac -d build/classes/ -sourcepath src/java/ src/java/net/contentobjects/jnotify/win32/*.java

clean: del1

bsdjar: freebsd
	jar -cfm JNotify.jar src/MANIFEST.MF -C build/classes/ .

del1:
	rm -R build/classes/net/*
	
jar: java
	jar -cfm JNotify.jar src/MANIFEST.MF -C ${PRE}/classes/ .

tar:
	tar --exclude='.gz' --exclude='.svn' --exclude='.AppleDouble' --exclude='.DS_Store' -czvf JNotifyFreeBSD.tar.gz ../JNotify_freebsd

