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

import heroesgrave.paint.main.Paint;

/**
 * 
 * @author Longor1996 & HeroesGrave
 *
 */
public abstract class Plugin
{
	public final String name;
	
	public Plugin(String name)
	{
		this.name = name;
	}
	
	public abstract void init(Paint paint);
	
	public abstract void onLaunch();
	
	public abstract void registerImageOps(RegisterImageOps register);
	
	public abstract void registerTools(RegisterTools register);
}