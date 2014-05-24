// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.image;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class RawImage
{
	private int[] buffer;
	public final int width, height;
	
	public RawImage(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.buffer = new int[width * height];
	}
	
	public void setPixel(int x, int y, int c)
	{
		buffer[index(x, y)] = c;
	}
	
	public int getPixel(int x, int y)
	{
		return buffer[index(x, y)];
	}
	
	private int index(int x, int y)
	{
		return y * width + x;
	}
	
	public int[] copyBuffer()
	{
		return Arrays.copyOf(buffer, buffer.length);
	}
	
	public int[] getBuffer()
	{
		return buffer;
	}
	
	public void setBuffer(int[] buffer)
	{
		this.buffer = buffer;
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, width, height, buffer, 0, width);
		return image;
	}
}
