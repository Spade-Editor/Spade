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

import java.awt.geom.Ellipse2D;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.ShapeChange;

import java.awt.event.MouseEvent;

public class Ellipse extends Tool
{
    int sx, sy;
	private Ellipse2D.Float ellipse;
	private ShapeChange shapeChange;
	
	public Ellipse(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
	    sx = x;
	    sy = y;
	    ellipse = new Ellipse2D.Float(x, y, 1, 1);
        if(button == MouseEvent.BUTTON1) {
            shapeChange = new ShapeChange(ellipse, Paint.main.getLeftColour());
        }
	
            shapeChange = new ShapeChange(ellipse, Paint.main.getRightColour());
        }
	
	}
	
	public void onReleased(int x, int y, int button)
	{
        adjustEllipse(x, y);
		
        Paint.main.gui.canvas.applyPreview();
	}
	
	public void whilePressed(int x, int y, int button)
	{
	    adjustEllipse(x, y);
		
        Paint.main.gui.canvas.preview(shapeChange);
	}
		
	public void whileReleased(int x, int y, int button)
	{
			
			
	}
		
	private void adjustEllipse(int x, int y) {
        ellipse.width = Math.abs(x - sx);
        ellipse.height = Math.abs(y - sy);
			
            ellipse.x = x;
			
        }
        else {
            ellipse.x = sx;
        }
	
            ellipse.y = y;
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
