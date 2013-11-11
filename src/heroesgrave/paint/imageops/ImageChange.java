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

package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Change;

import java.awt.image.BufferedImage;

public class ImageChange implements Change
{
	private BufferedImage newImage, oldImage;
	
	public ImageChange(BufferedImage newImage)
	{
		this.newImage = newImage;
	}
	
	public BufferedImage apply(BufferedImage image)
	{
		oldImage = image;
		
		return newImage;
	}
	
	public BufferedImage revert(BufferedImage image)
	{
		return oldImage;
	}
	
	public int getSize()
	{
		return oldImage.getWidth() * oldImage.getHeight();
	}
	
	public boolean samePos(int x, int y)
	{
		return false;
	}
}