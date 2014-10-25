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

import heroesgrave.utils.math.MathUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
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
	
	public RawImage(int width, int height, int[] buffer)
	{
		this.width = width;
		this.height = height;
		assert buffer.length == width * height;
		this.buffer = buffer;
	}
	
	// Drawing functions
	
	public void drawLine(int x1, int y1, int x2, int y2, int c)
	{
		// Do the clamping once at the start so we don't have to perform checks when drawing the pixel.
		x1 = MathUtils.clamp(x1, 0, width);
		x2 = MathUtils.clamp(x2, 0, width);
		y1 = MathUtils.clamp(y1, 0, height);
		y2 = MathUtils.clamp(y2, 0, height);
		
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int sx = -1;
		int sy = -1;
		if(x1 < x2)
			sx = 1;
		if(y1 < y2)
			sy = 1;
		int err = dx - dy;
		do
		{
			setPixel(x1, y1, c);
			int e2 = 2 * err;
			if(e2 > -dy)
			{
				err = err - dy;
				x1 = x1 + sx;
			}
			if(e2 < dx)
			{
				err = err + dx;
				y1 = y1 + sy;
			}
		}
		while(!(x1 == x2 && y1 == y2));
	}
	
	public void drawRect(int x1, int y1, int x2, int y2, int c)
	{
		// Do the clamping once at the start so we don't have to perform checks when drawing the pixel.
		x1 = MathUtils.clamp(x1, 0, width);
		x2 = MathUtils.clamp(x2, 0, width);
		y1 = MathUtils.clamp(y1, 0, height);
		y2 = MathUtils.clamp(y2, 0, height);
		
		// top
		final int ix = y1 * width;
		Arrays.fill(buffer, ix + x1, ix + x2 + 1, c);
		
		// bottom
		final int jx = y2 * width;
		Arrays.fill(buffer, jx + x1, jx + x2 + 1, c);
		
		for(int i = y1; i <= y2; i++)
		{
			setPixel(x1, i, c);
			setPixel(x2, i, c);
		}
	}
	
	public void fillRect(int x1, int y1, int x2, int y2, int c)
	{
		x1 = MathUtils.clamp(x1, 0, width);
		x2 = MathUtils.clamp(x2, 0, width);
		y1 = MathUtils.clamp(y1, 0, height);
		y2 = MathUtils.clamp(y2, 0, height);
		
		for(; y1 <= y2; y1++)
		{
			final int k = y1 * width;
			Arrays.fill(buffer, k + x1, k + x2 + 1, c);
		}
	}
	
	// Buffer Manipulation
	
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
	
	// Create a RawImage which has direct access to the pixels of the BufferedImage.
	// This could be quite unreliable.
	public static RawImage fromRaster(BufferedImage image)
	{
		return new RawImage(image.getWidth(), image.getHeight(), ((DataBufferInt) image.getRaster().getDataBuffer()).getData());
	}
	
	public static RawImage fromBufferedImage(BufferedImage image)
	{
		return new RawImage(image.getWidth(), image.getHeight(), image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth()));
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, width, height, buffer, 0, width);
		return image;
	}
	
	public static RawImage copyOf(RawImage image)
	{
		return new RawImage(image.width, image.height, image.copyBuffer());
	}
	
	public void copyFrom(RawImage image)
	{
		if(this.width != image.width || this.height != image.height)
			throw new RuntimeException("Cannot copy from a different sized RawImage");
		System.arraycopy(image.buffer, 0, buffer, 0, width * height);
	}
}
