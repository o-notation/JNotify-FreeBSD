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

/**
 * BSDNotifyListener - Interface
 * <br/>
 * Created on Dec 1, 2011
 *
 * @author $LastChangedBy$
 * @version $LastChangedRevision: 161 $
 * <br/>
 * Date: $LastChangedDate: 2013-03-28 23:45:23 +0100 (Thu, 28 Mar 2013) $ 
 */
public interface BSDNotifyListener
{
    public void notify(String name, int wd, int mask, int action);
}
