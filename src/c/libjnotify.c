#include <sys/time.h>
#include <sys/types.h>
#include <sys/event.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <jni_md.h>
#include "libjnotify.h"
#include <sys/errno.h>

struct fileinfo {
    int fd;
    int mask;
    const char *path;
    struct fileinfo *next;
};

void processEvent(JNIEnv *env, jclass clazz, struct fileinfo *fi, int action);
int init();
void delete(int fd);
void out(void);
int kq;
struct fileinfo *first = NULL;
struct fileinfo *current = NULL;

JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeInit(JNIEnv *env, jclass clazz)
{
    return (jint) init();
}

JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeAddWatch(JNIEnv *env, jclass clazz, jstring path, jint mask)
{
    struct kevent listen;
    struct fileinfo *fi;
    fi = (struct fileinfo *) malloc(sizeof(struct fileinfo));
    //remember first element of list
    if(first == NULL )
    {
            first = fi;
            current = fi;
    }
    else
    {
            current->next = fi;
            current = fi;
    }
    fi->next = NULL;
    const char *p;
    int wp;
    fi->mask = (int) mask;
    p = (*env)->GetStringUTFChars(env, path ,NULL);
    fi->path = p;
    int fh = open(fi->path,O_RDONLY);
    fi->fd = fh;
    //out();
    //printf("<filehandle: %i>\n",fi->fd);
    //printf("<Path: %s>\n",fi->path);
    if(fh == -1)
        perror("nativeAddWatch.open");
    else
    {
        /* EV_CLEAR clears after firing the eventstate; events with fired states will not block any more */
        //printf("<ev rename: %i>\n",NOTE_RENAME);
        EV_SET( &listen, fh, EVFILT_VNODE, EV_ADD | EV_CLEAR, fi->mask, 0, fi);
        wp = kevent(kq, &listen, 1, NULL, 0, NULL);
        if( wp == -1)
            perror("nativeAddWatch.kevent");
    }	
    return fi->fd;
}

JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeRemoveWatch(JNIEnv *env, jclass clazz, jint fd)
{
    struct kevent del;
    int rp;
    EV_SET(&del, (int) fd, EVFILT_VNODE,EV_DELETE,0,0,NULL);
    rp = kevent(kq, &del,1,NULL,0,NULL);
    if( rp == -1)
        perror("nativeRemoveWatch.kevent");
    close((int) fd);
    //fileinfo has to be deleted
    delete((int) fd);
    return rp;
}

JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_nativeStartLoop(JNIEnv *env, jclass clazz)
{
    struct fileinfo *fo;
    struct kevent fevents;
    int nev;
    while (1)
    {
        nev = kevent(kq, NULL, 0, &fevents, 1, NULL);
        if(nev == -1)
        perror("nativeStartLoop.kevent");
        else if(nev > 0)
        {
            fo = (struct fileinfo *)fevents.udata;
            if( fevents.filter == EVFILT_VNODE)
            {
                processEvent(env, clazz, fo, fevents.fflags);		
            }
        }
    }
}

void processEvent(JNIEnv *env, jclass clazz, struct fileinfo *fi, int action)
{
    jmethodID mid = (*env)->GetStaticMethodID(env, clazz, "callbackProcessEvent", "(Ljava/lang/String;III)V");
     if (mid == NULL)
     {
        printf("callbackProcessEvent not found!\n");
        return;
     }
     jstring p = (*env)->NewStringUTF(env, fi->path);
     //printf("<action: %i>\n",action);
     (*env)->CallStaticVoidMethod(env, clazz, mid, p, fi->fd, fi->mask, action);
}

JNIEXPORT jint JNICALL Java_net_contentobjects_jnotify_freebsd_JNotify_1freebsd_getErrorNo(JNIEnv *env, jclass clazz)
{
    return errno;
}

int init()
{
    int back = 0;
    kq = kqueue();
    if ( kq == -1 )
    {
        perror("init.kqueue");
        back = kq;
    }
    return back;
}

/*
deletes fileinfo from list
fileinfo is not cleared after removing event from kqueue
*/
void delete(int fd)
{
    struct fileinfo *cur;
    struct fileinfo *next;
    cur = first;
    next = first;
    if(first != NULL)
    {
        //the first element
        if(first->fd == fd)
        {
            cur = first->next;
            free(first);
            first = cur;
        }
        else
        {
            cur = first;
            while(cur->next != NULL)
            {
                next = cur->next;
                if(next->fd == fd)
                {
                    cur->next = next->next;
                    //printf("DELETE Pfad=%s\n",next->path);
                    free(next);
                    break;
                }
                cur = next;
            }
        }
    }
    //out();
}

/*
for tests only
*/
void out()
{
    struct fileinfo *curr;
    curr = first;
    printf("<Start Liste>\n");
    while(curr != NULL)
    {
        printf("<Pfad=%s>\n",curr->path);
        printf("<FD=%i>\n",curr->fd);
        curr=curr->next;
    }
    printf("<Ende Liste>\n");
}
