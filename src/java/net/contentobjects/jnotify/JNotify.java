/**
 * *****************************************************************************
 * JNotify - Allow java applications to register to File system events.
 *
 * Copyright (C) 2005 - Content Objects
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 ******************************************************************************
 *
 * Content Objects, Inc., hereby disclaims all copyright interest in the library
 * `JNotify' (a Java library for file system events).
 *
 * Yahali Sherman, 21 November 2005 Content Objects, VP R&D.
 *
 ******************************************************************************
 * Author : Omry Yadan
 * ****************************************************************************
 */
package net.contentobjects.jnotify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.contentobjects.jnotify.freebsd.JNotifyAdapterFreeBSD;

public class JNotify
{

    public static final int FILE_CREATED = 0x1;
    public static final int FILE_DELETED = 0x2;
    public static final int FILE_MODIFIED = 0x4;
    public static final int FILE_RENAMED = 0x8;
    public static final int FILE_ANY = FILE_CREATED | FILE_DELETED | FILE_MODIFIED | FILE_RENAMED;
    private static IJNotify _instance;

    static
    {
        String overrideClass = System.getProperty("jnotify.impl.override");
        if (overrideClass != null)
        {
            try
            {
                _instance = (IJNotify) Class.forName(overrideClass).newInstance();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        } else
        {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.equals("linux"))
            {
                try
                {
                    _instance = (IJNotify) Class.forName("net.contentobjects.jnotify.linux.JNotifyAdapterLinux").newInstance();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            } else if (osName.startsWith("windows"))
            {
                try
                {
                    _instance = (IJNotify) Class.forName("net.contentobjects.jnotify.win32.JNotifyAdapterWin32").newInstance();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            } else if (osName.startsWith("mac os x"))
            {
                try
                {
                    _instance = (IJNotify) Class.forName("net.contentobjects.jnotify.macosx.JNotifyAdapterMacOSX").newInstance();
                    //using bsdlib for mac
                    //lib has to be compiled
//                    _instance = (IJNotify) Class.forName("net.contentobjects.jnotify.freebsd.JNotifyAdapterFreeBSD").newInstance();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            } else if (osName.startsWith("freebsd"))
            {
                try
                {
                    _instance = (IJNotify) Class.forName("net.contentobjects.jnotify.freebsd.JNotifyAdapterFreeBSD").newInstance();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            } else
            {
                throw new RuntimeException("Unsupported OS : " + osName);
            }
        }
    }

    public static int addWatch(String path, int mask, boolean watchSubtree, JNotifyListener listener) throws JNotifyException
    {
        return _instance.addWatch(path, mask, watchSubtree, listener);
    }

    public static boolean removeWatch(int watchId) throws JNotifyException
    {
        return _instance.removeWatch(watchId);
    }

    /**
     * Changed for debugging. First argument takes semikolon separated filelist;
     * second argument 1 for observing subtree; third mask.
     *
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        boolean tree = false;
        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss.SS");
        int mask = JNotify.FILE_ANY;
        System.out.println(JNotifyAdapterFreeBSD.VERSION);
        if (args.length == 0)
        {
            System.out.println("No Files given.");
            System.exit(0);
        } 
        else
        {
            if (args.length > 1 && args[1].equals("1"))
            {
                tree = true;
                System.out.println("with subtree...");
            } else
            {
                System.out.println("without subtree...");
            }
            if (args.length > 2)
            {
                mask = Integer.parseInt(args[2]);
            }
            String[] ps = args[0].split(";");
            for (int i = 0; i < ps.length; i++)
            {
                try
                {
                    addWatch(ps[i].trim(), mask, tree, new JNotifyListener()
                    {

                        @Override
                        public void fileCreated(int wd, String rootPath, String name)
                        {
                            print(wd, rootPath, name, "Created");
                        }

                        @Override
                        public void fileDeleted(int wd, String rootPath, String name)
                        {
                            print(wd, rootPath, name, "Deleted");
                        }

                        @Override
                        public void fileModified(int wd, String rootPath, String name)
                        {
                            print(wd, rootPath, name, "Modified");
                        }

                        @Override
                        public void fileRenamed(int wd, String rootPath, String oldName, String newName)
                        {
                            print(wd, rootPath, oldName, newName + " Renamed");
                        }

                        private void print(int wd, String path, String name, String action)
                        {
                            String now = df.format(new Date());
                            System.out.println(now + " Pfad:" + path + ";Name:" + name + " " + action + "(WD=" + wd + ")");
                            System.out.println("---");

                        }
                    });
                }
                catch (JNotifyException ex)
                {
                    System.out.println("ERROR:" + ex.getMessage() + ";ErrorCode:" + ex.getErrorCode());
                }
            }
        }
        System.err.println("Monitoring , ctrl+c to stop");
        while (true)
        {
            Thread.sleep(10000);
        }
    }
}
