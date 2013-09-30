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

import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;

public class Rectangle extends Tool
{
	private int sx, sy;

	public Rectangle(String name)
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
		rectangle(sx, sy, x, y);
		Paint.main.gui.canvas.applyPreview();
	}

	public void whilePressed(int x, int y)
	{
		rectangle(sx, sy, x, y);
	}

	public void whileReleased(int x, int y)
	{

	}

	private int sign(int i)
	{
		if(i < 0)
			return -1;
		else if(i > 0)
			return 1;
		return 0;
	}

	public void rectangle(int x1, int y1, int x2, int y2)
	{
		Paint.main.gui.canvas.clearPreview();

		if(Input.CTRL)
		{
			int w = x2 - x1;
			int h = y2 - y1;
			if(Math.abs(w) > Math.abs(h))
			{
				int r = Math.abs(w);
				h = sign(h) * r;
			}
			else
			{
				int r = Math.abs(h);
				w = sign(w) * r;
			}
			x2 = x1 + w;
			y2 = y1 + h;
		}

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

		for(int i = x1; i <= x2; i++)
		{
			brush(i, y1);
			brush(i, y2);
		}

		for(int j = y1; j <= y2; j++)
		{
			brush(x1, j);
			brush(x2, j);
		}
	}

	public void brush(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getColour()));
	}
}