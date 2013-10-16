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

import heroesgrave.paint.main.Change;
import heroesgrave.paint.main.MultiChange;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;
import heroesgrave.utils.math.MathUtils;

import java.util.ArrayList;

public abstract class Brush extends Tool
{
	private int lastX, lastY;

	private ArrayList<Change> buffer = new ArrayList<Change>();

	public Brush(String name)
	{
		super(name);
	}

	public void buffer(PixelChange c)
	{
		for(Change ch : buffer)
		{
			if(ch.samePos(c.x, c.y))
				return;
		}
		buffer.add(c);
		Paint.main.gui.canvas.bufferChange(c);
	}

	public void buffer(MultiChange c)
	{
		for(Change change : c.changes)
		{
			if(change instanceof PixelChange)
			{
				buffer((PixelChange) change);
			}
			else if(change instanceof MultiChange)
			{
				buffer((MultiChange) change);
			}
		}
	}

	public void flush()
	{
		buffer.clear();
		Paint.main.gui.canvas.flushChanges();
	}

	public void onPressed(int x, int y, int button)
	{
		lastX = x;
		lastY = y;
		brush(x, y, button);
	}

	public void onReleased(int x, int y, int button)
	{
		lastX = x;
		lastY = y;
		flush();
	}

	public void whilePressed(int x, int y, int button)
	{
		if(x != lastX || y != lastY)
		{
			stroke(lastX, lastY, x, y, button);
			lastX = x;
			lastY = y;
		}
	}

	private void stroke(int x1, int y1, int x2, int y2, int button)
	{
		float dx = x2 - x1;
		float dy = y2 - y1;

		float grad;

		if(Math.abs(dx) > Math.abs(dy))
		{
			grad = dy / dx;
			if(dx > 0)
			{
				for(int x = x1; x <= x2; x++)
				{
					brush(x, MathUtils.floor((grad * (x - x1)) + y1), button);
				}
			}
			else
			{
				for(int x = x1; x >= x2; x--)
				{
					brush(x, MathUtils.floor((grad * (x - x1)) + y1), button);
				}
			}
		}
		else
		{
			grad = dx / dy;
			if(dy > 0)
			{
				for(int y = y1; y <= y2; y++)
				{
					brush(MathUtils.floor((grad * (y - y1)) + x1), y, button);
				}
			}
			else
			{
				for(int y = y1; y >= y2; y--)
				{
					brush(MathUtils.floor((grad * (y - y1)) + x1), y, button);
				}
			}
		}
	}

	public void whileReleased(int x, int y, int button)
	{
		lastX = x;
		lastY = y;
	}

	public abstract void brush(int x, int y, int button);
}