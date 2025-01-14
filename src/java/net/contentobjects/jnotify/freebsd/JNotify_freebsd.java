/*******************************************************************************
 * JNotify - Allow java applications to register to File system events.
 *
 * Copyright (C) 2011 - Matthias Zitzen
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 ******************************************************************************
 * This is a Extension for FreeBSD file events.
 *
 * Author : Matthias Zitzen; m.zitzen@o-notation.org
 * Oktober 2011, Moenchengladbach, Germany
 ******************************************************************************/

package net.contentobjects.jnotify.freebsd;

import net.contentobjects.jnotify.JNotifyException;

/**
 * JNotify_freebsd - Native Library for handling the native filesystem events of
 * FreeBSD.<br/>
 * kevents works only with filedescriptors, you'll have to add your own data to the user_data
 * parameter of kqueue.<br/>
 * When files are renamed you'll have to rescan the subtree to find the new renamed filename.
 * Unfortunately FreeBSD has no reliable function to get a filename by filedescriptor.<br/>
 * The same procedure when a file is created.<br/>
 * Not every event in a directory is triggered and the fired events are only triggered
 * with attribute BSD_WRITE. So files in the subtree are registered separately.<br/>
 * Moved Files out off the obeserved filesystem, are not notified correctly.
 * <br/>
 * Created on Oct 24, 2011
 *
 * @author $LastChangedBy$
 * @version $LastChangedRevision: 166 $
 * <br/>
 * Date: $LastChangedDate: 2013-05-08 20:19:28 +0200 (Wed, 08 May 2013) $ 
 */
public class JNotify_freebsd
{
    
    static boolean isDEBUG = false;
    public final static int BSD_DELETED =   0x00000001;
    public final static int BSD_WRITE =     0x00000002;//file or folder created in folder
    public final static int BSD_EXTEND =    0x00000004;
    public final static int BSD_ATTRIBUTE = 0x00000008;
    public final static int BSD_LINK =      0x00000010;
    public final static int BSD_RENAME =    0x00000020;
    public final static int BSD_ALL =       BSD_DELETED | BSD_WRITE | BSD_EXTEND | BSD_ATTRIBUTE | BSD_RENAME | BSD_LINK;
    
    static
    {
            System.loadLibrary("jnotify_freebsd");
            int res = nativeInit();
            if (res != 0)
            {
                throw new RuntimeException("Error initializing library. freebsd error code #" + res + ", man errno for more info");
            }
            init();
    }

    private static BSDNotifyListener nListener;
    
    private static native int nativeInit();

    private static native int nativeAddWatch(String path, int mask);

    private static native int nativeRemoveWatch(int wd);

    private native static int nativeStartLoop();

    private native static int getErrorNo();

    //starting the kqueue-loop
    private static void init()
    {
        Thread eventThread = new Thread()
        {

            @Override
            public void run()
            {
                nativeStartLoop();

            }
        };
        eventThread.setDaemon(true);
        eventThread.start();
    }

    public static int removeWatch(int fd) throws JNotifyException
    {
        int b = nativeRemoveWatch(fd);
        debug("INFO:JNotify_freebsd.removeWatch:FH=" + fd);
        if(b<0)
        {
            throw new JNotifyException_FreeBSD("Error removeWatching: " + fd, getErrorNo());
        }
        return b;
    }

    public static void callbackProcessEvent(String path, int fd, int mask, int action)
    {
        debug("JNotify_freebsd.callbackProcessEvent:FD="+fd+";Mask="+mask+";Action="+action);
        if(nListener != null)
        {
            nListener.notify(path, fd, mask, action);
        }
    }

    public static void setNotifyListener(BSDNotifyListener l)
    {
        if(nListener == null)
        {
            nListener = l;
        }
        else
        {
            throw new RuntimeException("Notify listener is already set. multiple notify listeners are not supported.");
        }
    }

    public static int addWatch(String path,int mask) throws JNotifyException
    {
        int fd = nativeAddWatch(path, mask);
        debug("INFO:JNotify_freebsd.addWatch:FH=" + fd + ";Path=" + path);
        if(fd<0)
        {
            throw new JNotifyException_FreeBSD("Error watching " + path + ": Bad file descriptor.", getErrorNo());
        }
        return fd;
    }

    static void debug(String message)
    {
        if(isDEBUG)
        {
            System.out.println(message);
        }
    }

}
