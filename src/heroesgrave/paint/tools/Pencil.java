// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.tools;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.edit.PathChange;
import heroesgrave.paint.main.Paint;

public class Pencil extends Tool
{
	private PathChange path;
	
	public Pencil(String name)
	{
		super(name);
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		path = new PathChange(x, y, Paint.main.getColor(button));
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		layer.addChange(path);
		Paint.main.gui.repaint();
		path = null;
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		path.moveTo(x, y);
	}
	
	/*
	private void stroke(Layer l, int x1, int y1, int x2, int y2, int b)
	{
		float dx = x2 - x1;
		float dy = y2 - y1;
		
		float grad;
		
		if(Math.abs(dx) > Math.abs(dy))
		{
			grad = dy / dx;
			if(dx > 0)
			{
				for(short x = (short) x1; x <= x2; x++)
				{
					brush(l, x,
							(short) MathUtils.floor((grad * (x - x1)) + y1), b);
				}
			}
			else
			{
				for(short x = (short) x1; x >= x2; x--)
				{
					brush(l, x,
							(short) MathUtils.floor((grad * (x - x1)) + y1), b);
				}
			}
		}
		else
		{
			grad = dx / dy;
			if(dy > 0)
			{
				for(short y = (short) y1; y <= y2; y++)
				{
					brush(l, (short) MathUtils.floor((grad * (y - y1)) + x1),
							y, b);
				}
			}
			else
			{
				for(short y = (short) y1; y >= y2; y--)
				{
					brush(l, (short) MathUtils.floor((grad * (y - y1)) + x1),
							y, b);
				}
			}
		}
	}
	
	public void brush(Layer layer, short x, short y, int button)
	{
		if(x < 0 || y < 0 || x >= layer.getWidth() || y >= layer.getHeight())
			return;
		layer.addChange(new PixelChange(x, y, Paint.main.getColor(button)));
	}
	*/
}
