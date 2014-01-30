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

package heroesgrave.paint.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SelectionCanvas extends Canvas
{
	public static final Color mask_bg = new Color(32, 32, 32, 128);
	
	public SelectionCanvas(String name, int width, int height)
	{
		super(name, width, height);
	}
	
	public SelectionCanvas(String name, BufferedImage image)
	{
		super(name, image);
	}
	
	public void draw(Graphics2D g, boolean render)
	{
		if(render)
		{
			if(hist.wasChanged())
			{
				this.image = hist.getUpdatedImage();
			}
			
			g.setComposite(mode);
			g.setColor(mask_bg);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(this.image, 0, 0, null);
			
			if(!layers.isEmpty())
			{
				for(int i = layers.size() - 1; i >= 0; i--)
				{
					layers.get(i).draw(g, render);
				}
			}
		}
		else
		{
			super.draw(g, render);
		}
	}
}