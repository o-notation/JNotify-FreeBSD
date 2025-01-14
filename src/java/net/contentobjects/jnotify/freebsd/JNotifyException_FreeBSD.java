/*******************************************************************************
 * JNotify - Allow java applications to register to File system events.
 *
 * Copyright (C) 2005 - Content Objects
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
 * JNotifyException_FreeBSD - Exception with BSD error-numbers.
 * <br/>
 * Created on Oct 27, 2011
 *
 * @author $LastChangedBy$
 * @version $LastChangedRevision: 161 $
 * <br/>
 * Date: $LastChangedDate: 2013-03-28 23:45:23 +0100 (Thu, 28 Mar 2013) $ 
 */
public class JNotifyException_FreeBSD extends JNotifyException
{

    private static final int BSD_NO_SUCH_FILE_OR_DIRECTORY = 2;
    private static final int BSD_PERMISSION_DENIED = 9;
    private static final int BSD_TOO_MANY_OPEN_FILES = 23;
    private static final int BSD_DEVICE_BUSY = 16;

    public JNotifyException_FreeBSD(String s, int systemErrorCode)
    {
        super(s, systemErrorCode);
    }

    @Override
    public int getErrorCode()
    {
        switch (_systemErrorCode)
        {
            case BSD_NO_SUCH_FILE_OR_DIRECTORY:
                return ERROR_NO_SUCH_FILE_OR_DIRECTORY;
            case BSD_PERMISSION_DENIED:
                return ERROR_PERMISSION_DENIED;
            case BSD_TOO_MANY_OPEN_FILES:
                return ERROR_WATCH_LIMIT_REACHED;
            case BSD_DEVICE_BUSY:
                return ERROR_PERMISSION_DENIED;
            default:
                return ERROR_UNSPECIFIED;
        }
    }
}
