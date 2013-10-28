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

import java.awt.event.MouseEvent;

import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;
import heroesgrave.utils.math.MathUtils;

public class Ellipse extends Tool
{
	private int sx, sy;
	
	public Ellipse(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		sx = x;
		sy = y;
	}
	
	public void onReleased(int x, int y, int button)
	{
		circle(sx, sy, Math.abs(x - sx), Math.abs(y - sy), button);
		Paint.main.gui.canvas.applyPreview();
	}
	
	public void whilePressed(int x, int y, int button)
	{
		circle(sx, sy, Math.abs(x - sx), Math.abs(y - sy), button);
	}
	
	public void whileReleased(int x, int y, int button)
	{
		
	}
	
	public void circle(int cx, int cy, float rx, float ry, int button)
	{
		Paint.main.gui.canvas.clearPreview();
		
		if(Input.CTRL)
		{
			rx = ry = Math.max(rx, ry);
		}
		
		for(int i = (int) (cx - rx); i <= cx + rx; i++)
		{
			float ex = (float) i - cx;
			
			float j = 1f - ((ex * ex) / (rx * rx));
			j = j * ry * ry;
			j = (float) Math.sqrt(j);
			
			brush(i, MathUtils.floor(cy + j), button);
			brush(i, MathUtils.ceil(cy - j), button);
		}
		
		for(int j = (int) (cy - ry); j <= cy + ry; j++)
		{
			float ey = (float) j - cy;
			
			float i = 1f - ((ey * ey) / (ry * ry));
			i = i * rx * rx;
			i = (float) Math.sqrt(i);
			
			brush(MathUtils.floor(cx + i), j, button);
			brush(MathUtils.ceil(cx - i), j, button);
		}
	}
	
	public void brush(int x, int y, int button)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getLeftColour()));
		if(button == MouseEvent.BUTTON1)
		{
			Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getLeftColour()));
		}
		else if(button == MouseEvent.BUTTON3)
		{
			Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getRightColour()));
		}
	}
}
