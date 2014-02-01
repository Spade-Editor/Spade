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

package heroesgrave.paint.imageops;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.image.doc.ImageOpChange;
import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ResizeCanvasChange extends ImageOpChange
{
	private int w, h;
	
	public ResizeCanvasChange(int w, int h)
	{
		this.w = w;
		this.h = h;
	}
	
	@Override
	public void apply()
	{
		recurse(Paint.main.gui.canvas.getRoot());
	}
	
	public void recurse(Canvas c)
	{
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = (Graphics2D) newImage.getGraphics();
		g2d.drawImage(c.getImage(), 0, 0, null);
		
		c.addChange(new KeyFrame(newImage));
		
		ArrayList<Canvas> list = c.getChildren();
		for(Canvas cn : list)
			recurse(cn);
	}
}