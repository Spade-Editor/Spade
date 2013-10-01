/*
 *	Copyright 2013 HeroesGrave
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

package heroesgrave.paint.tools;

import javax.swing.JMenuBar;

public abstract class Tool
{
	public final String name;
	
	protected final JMenuBar menu;

	public Tool(String name)
	{
		this.name = name;
		menu = new JMenuBar();
	}
	
	public final JMenuBar getOptions()
	{
		return menu;
	}

	public abstract void onPressed(int x, int y);

	public abstract void onReleased(int x, int y);

	public abstract void whilePressed(int x, int y);

	public abstract void whileReleased(int x, int y);
}