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

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;

public class Eraser extends Brush
{
	public Eraser(String name)
	{
		super(name);
	}
	
	public void brush(int x, int y, int button)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		if(button == MouseEvent.BUTTON1)
		{
			buffer(new PixelChange(x, y, 0x000000));
		}
		else if(button == MouseEvent.BUTTON3)
		{
			buffer(new PixelChange(x, y, 0x000000));
		}
	}
}