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

package heroesgrave.paint.main;

import heroesgrave.utils.math.MathUtils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Selection
{
	private BufferedImage image;
	private int x, y; // Full image coordinates of the top-left corner of the selection
	private boolean floating;
	
	public void select(BufferedImage image, int x, int y, int w, int h)
	{
		this.image = image.getSubimage(x, y, w, h);
		this.x = x;
		this.y = y;
		System.out.println("Selected");
	}
	
	public boolean isEmpty()
	{
		return image == null;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public void translate(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
	public void draw(Graphics2D g, float scale)
	{
		if(image != null)
			g.drawImage(image, MathUtils.floor(x * scale), MathUtils.floor(y * scale), MathUtils.floor(image.getWidth() * scale),
					MathUtils.floor(image.getHeight() * scale), null);
	}
}