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

import heroesgrave.paint.image.BufferedChange;
import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.PixelChange;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

/*
 * WIP
 * 
 * Copies pixel from the parent layer into the current layer.
 * 
 */
public class LayerCopy extends Tool
{
	private BufferedChange change;
	private int lastX, lastY;
	private Canvas c, parent;
	
	public LayerCopy(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		lastX = x;
		lastY = y;
		c = Paint.main.gui.canvas.getCanvas();
		parent = Paint.main.gui.canvas.getParentOf(c);
		if(parent == null)
			return;
		change = new BufferedChange();
		brush(x, y);
		Paint.main.gui.canvas.preview(change);
	}
	
	public void onReleased(int x, int y, int button)
	{
		if(parent == null)
			return;
		stroke(lastX, lastY, x, y);
		Paint.main.gui.canvas.applyPreview();
		change = null;
		c = null;
		parent = null;
	}
	
	public void whilePressed(int x, int y, int button)
	{
		if(parent == null)
			return;
		stroke(lastX, lastY, x, y);
		Paint.main.gui.canvas.getPanel().repaint();
		lastX = x;
		lastY = y;
	}
	
	private void brush(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getWidth() || y >= Paint.main.gui.canvas.getHeight())
			return;
		change.buffer(new PixelChange(x, y, parent.getRGB(x, y)));
	}
	
	private void stroke(int x1, int y1, int x2, int y2)
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
					brush(x, MathUtils.floor((grad * (x - x1)) + y1));
				}
			}
			else
			{
				for(int x = x1; x >= x2; x--)
				{
					brush(x, MathUtils.floor((grad * (x - x1)) + y1));
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
					brush(MathUtils.floor((grad * (y - y1)) + x1), y);
				}
			}
			else
			{
				for(int y = y1; y >= y2; y--)
				{
					brush(MathUtils.floor((grad * (y - y1)) + x1), y);
				}
			}
		}
	}
}