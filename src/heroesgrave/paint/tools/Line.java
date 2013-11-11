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

import java.awt.geom.Line2D;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.ShapeChange;

import java.awt.event.MouseEvent;

public class Line extends Tool
{
	private Line2D.Float line;
	private ShapeChange shapeChange;
	
	public Line(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
	    line = new Line2D.Float(x, y, x, y);
        if(button == MouseEvent.BUTTON1) {
            shapeChange = new ShapeChange(line, Paint.main.getLeftColour());
        }
	
            shapeChange = new ShapeChange(line, Paint.main.getRightColour());
        }
        Paint.main.gui.canvas.preview(shapeChange);
	}
	
	public void onReleased(int x, int y, int button)
	{
		line.x2 = x;
		
		
		
		Paint.main.gui.canvas.applyPreview();
	}
	
		if(button == MouseEvent.BUTTON1)
		{
			Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getLeftColour()));
		}
		else if(button == MouseEvent.BUTTON3)
		{
			Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getRightColour()));
		}
	
	public void whilePressed(int x, int y, int button)
	{
	    line.x2 = x;
	    line.y2 = y;

        Paint.main.gui.canvas.preview(shapeChange);
	}
	
	public void whileReleased(int x, int y, int button)
	{
		
	}
}