/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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
import heroesgrave.paint.main.Paint;

import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;

public class Pixel extends Tool
{
	private GeneralPath path;
	private ShapeChange change;
	
	public Pixel(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		path = new GeneralPath();
		path.moveTo(x, y);
		if(button == MouseEvent.BUTTON1)
		{
			change = new ShapeChange(path, Paint.main.getLeftColour());
		}
		else if(button == MouseEvent.BUTTON3)
		{
			change = new ShapeChange(path, Paint.main.getRightColour());
		}
		Paint.main.gui.canvas.preview(change);
	}
	
	public void onReleased(int x, int y, int button)
	{
		if(path != null)
		{
			path.lineTo(x, y);
		}
		Paint.main.gui.canvas.applyPreview();
		path = null;
		change = null;
	}
	
	public void whilePressed(int x, int y, int button)
	{
		if(path != null)
		{
			path.lineTo(x, y);
		}
		Paint.main.gui.canvas.getPanel().repaint();
	}
}