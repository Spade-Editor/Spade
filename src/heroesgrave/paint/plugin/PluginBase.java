/*
 *	Copyright 2013 Longor1996 & HeroesGrave
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.paint.plugin;

import javax.swing.JMenu;

import heroesgrave.paint.main.Paint;

/**
 * 
 * @author Longor1996
 *
 */
public class PluginBase
{
	
	public PluginBase()
	{
		
	}
	
	/**
	 * Init method.
	 * This method is called on application startup by the plugin-loader.
	 * 
	 * @param paint The Paint.JAVA application class instance.
	 **/
	public void init(Paint paint)
	{
		
	}
	
	public void imageopRegistration(JMenu menu)
	{
		
	}
	
	public void toolRegistration(JMenu menu)
	{
		
	}
	
	public void filemenuRegistration(JMenu menu)
	{
		
	}
	
}