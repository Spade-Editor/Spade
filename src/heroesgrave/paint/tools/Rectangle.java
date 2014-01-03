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

import heroesgrave.paint.image.ShapeChange;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Tool
{
	private int sx, sy;
	private Rectangle2D.Float rect;
	private ShapeChange change;
	
	public Rectangle(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		sx = x;
		sy = y;
		rect = new Rectangle2D.Float(x, y, 1, 1);
		if(button == MouseEvent.BUTTON1)
		{
			change = new ShapeChange(rect, Paint.main.getLeftColour());
		}
		else if(button == MouseEvent.BUTTON3)
		{
			change = new ShapeChange(rect, Paint.main.getRightColour());
		}
		Paint.main.gui.canvas.preview(change);
	}
	
	public void onReleased(int x, int y, int button)
	{
		adjustRectangle(x, y);
		Paint.main.gui.canvas.applyPreview();
	}
	
	public void whilePressed(int x, int y, int button)
	{
		adjustRectangle(x, y);
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	private void adjustRectangle(int x, int y)
	{
		if(Input.CTRL)
		{
			int w = x - sx;
			int h = y - sy;
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
			x = sx + w;
			y = sy + h;
		}
		rect.width = Math.abs(x - sx);
		rect.height = Math.abs(y - sy);
		rect.x = Math.min(x, sx);
		rect.y = Math.min(y, sy);
	}
	
	private int sign(int i)
	{
		if(i < 0)
			return -1;
		else if(i > 0)
			return 1;
		return 0;
	}
}