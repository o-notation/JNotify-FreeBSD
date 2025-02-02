/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class net_contentobjects_jnotify_freebsd_JNotify_freebsd */

#ifndef _Included_net_contentobjects_jnotify_freebsd_JNotify_freebsd
#define _Included_net_contentobjects_jnotify_freebsd_JNotify_freebsd
#ifdef __cplusplus
extern "C" {
#endif
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_DELETED
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_DELETED 1L
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_WRITE
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_WRITE 2L
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_EXTEND
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_EXTEND 4L
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_ATTRIBUTE
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_ATTRIBUTE 8L
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_LINK
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_LINK 16L
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_RENAME
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_RENAME 32L
#undef net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_ALL
#define net_contentobjects_jnotify_freebsd_JNotify_freebsd_BSD_ALL 63L
/*
 * Class:     net_contentobjects_jnotify_freebsd_JNotify_freebsd
 * Method:    nativeInit
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeInit
  (JNIEnv *, jclass);

/*
 * Class:     net_contentobjects_jnotify_freebsd_JNotify_freebsd
 * Method:    nativeAddWatch
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeAddWatch
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     net_contentobjects_jnotify_freebsd_JNotify_freebsd
 * Method:    nativeRemoveWatch
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeRemoveWatch
  (JNIEnv *, jclass, jint);

/*
 * Class:     net_contentobjects_jnotify_freebsd_JNotify_freebsd
 * Method:    nativeStartLoop
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeStartLoop
  (JNIEnv *, jclass);

/*
 * Class:     net_contentobjects_jnotify_freebsd_JNotify_freebsd
 * Method:    getErrorNo
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_getErrorNo
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
