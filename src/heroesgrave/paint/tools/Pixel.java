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
import java.awt.geom.GeneralPath;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.ShapeChange;

public class Pixel extends Tool
{
    private GeneralPath pixels;
    private ShapeChange shapeChange;
    
	public Pixel(String name)
	{
		super(name);
	}

    @Override
    public void onPressed(int x, int y, int button) {
        pixels = new GeneralPath();
        pixels.moveTo(x, y);
        if(button == MouseEvent.BUTTON1) {
            shapeChange = new ShapeChange(pixels, Paint.main.getLeftColour());
        }
        else if(button == MouseEvent.BUTTON3) {
            shapeChange = new ShapeChange(pixels, Paint.main.getRightColour());
        }
        Paint.main.gui.canvas.preview(shapeChange);
    }

    @Override
    public void onReleased(int x, int y, int button) {
        if(pixels != null) {
            pixels.lineTo(x, y);
            Paint.main.gui.canvas.applyPreview();
        }
        pixels = null;
        shapeChange = null;
    }

    @Override
    public void whilePressed(int x, int y, int button) {
        if(pixels != null) {
            pixels.lineTo(x, y);
            Paint.main.gui.canvas.preview(shapeChange);
        }
    }

    @Override
    public void whileReleased(int x, int y, int button) {
    }
	
	
}