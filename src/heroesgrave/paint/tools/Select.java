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

import heroesgrave.paint.main.Paint;

public class Select extends Tool
{
	private int sx, sy;
	
	public Select(String name)
	{
		super(name);
	}

	public void onPressed(int x, int y)
	{
		sx = x;
		sy = y;
	}

	public void onReleased(int x, int y)
	{
		select(sx, sy, x, y);
		sx = x;
		sy = y;
	}
	
	private void select(int x1, int y1, int x2, int y2)
	{
		int temp;
		if(x2 < x1)
		{
			temp = x2;
			x2 = x1;
			x1 = temp;
		}
		if(y2 < y1)
		{
			temp = y2;
			y2 = y1;
			y1 = temp;
		}
		Paint.main.selection.select(Paint.main.gui.canvas.getImage(), x1, y1, x2-x1, y2-y1);
	}

	public void whilePressed(int x, int y)
	{
		
	}

	public void whileReleased(int x, int y)
	{
		
	}
}