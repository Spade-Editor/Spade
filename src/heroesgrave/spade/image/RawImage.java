// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.image;

import heroesgrave.utils.math.MathUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public final class RawImage
{
	private static int[] TMP;
	
	public static enum MaskMode
	{
		REP, ADD, SUB, XOR, AND;
		
		public boolean transform(boolean bool)
		{
			switch(this)
			{
				case REP:
				case ADD:
					return true;
				case SUB:
					return false;
				case XOR:
					return !bool;
				case AND:
					return bool;
			}
			throw new IllegalStateException("Unreachable");
		}
	}
	
	private int[] buffer;
	public final int width, height;
	
	private boolean[] mask;
	
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
		if(buffer.length != width * height)
			throw new IllegalArgumentException("Buffer length must be `width*height`");
		this.buffer = buffer;
	}
	
	public RawImage(int width, int height, int[] buffer, boolean[] mask)
	{
		this.width = width;
		this.height = height;
		if(buffer.length != width * height || buffer.length != mask.length)
			throw new IllegalArgumentException("Buffer length must be `width*height`");
		this.buffer = buffer;
		this.mask = mask;
	}
	
	// Returns or allocates a temporary buffer with a size of at least width*height.
	private static int[] get_tmp(int width, int height)
	{
		if(TMP == null || TMP.length < width * height)
		{
			TMP = new int[width * height];
		}
		return TMP;
	}
	
	public void move(int dx, int dy)
	{
		final int offset = dx + dy * width;
		if(Math.abs(dx) >= width || Math.abs(dy) >= height)
		{
			this.fill(0); // Fill, not clear, so the mask is respected.
			return;
		}
		else if(offset == 0)
			return;
		
		int[] tmp = get_tmp(width, height);
		
		int src = 0;
		int dst = offset;
		if(offset < 0)
		{
			src = -offset;
			dst = 0;
		}
		final int len = buffer.length - (src + dst);
		
		// Copy whole buffer to tmp.
		System.arraycopy(buffer, 0, tmp, 0, buffer.length);
		
		// Copy back from tmp
		if(mask == null)
		{
			// Do x-axis bounds clipping.
			if(dx < 0)
			{
				for(int y = 0; y < height; y++)
				{
					Arrays.fill(tmp, y * width, y * width - dx, 0);
				}
			}
			else if(dx > 0)
			{
				for(int y = 1; y <= height; y++)
				{
					Arrays.fill(tmp, y * width - dx, y * width, 0);
				}
			}
			
			// Do the actual copying.
			System.arraycopy(tmp, src, buffer, dst, len);
			
			// Do y-axis bounds clipping.
			Arrays.fill(buffer, 0, dst, 0); // Cut out top.
			Arrays.fill(buffer, buffer.length - src, buffer.length, 0); // Cut out bottom
		}
		else
		{
			this.fill(0); // Clear the old pixels. Only necessary when mask is enabled.
			
			// First translate the mask.
			System.arraycopy(mask, src, mask, dst, len);
			// Then do bounds clipping.
			Arrays.fill(mask, 0, dst, false); // Cut out top.
			Arrays.fill(mask, buffer.length - src, buffer.length, false); // Cut out bottom
			if(dx < 0) // Cut out whatever side.
			{
				for(int y = 1; y <= height; y++)
				{
					Arrays.fill(mask, y * width + dx, y * width, false);
				}
			}
			else if(dx > 0)
			{
				for(int y = 0; y < height; y++)
				{
					Arrays.fill(mask, y * width, y * width + dx, false);
				}
			}
			
			// Now we can copy the data back, if it's not masked out.
			for(int i = 0; i < len; i++)
			{
				if(mask[dst + i])
					buffer[dst + i] = tmp[src + i];
			}
		}
	}
	
	// Drawing functions
	
	public void drawLine(int x1, int y1, final int x2, final int y2, final int c)
	{
		final int dx = Math.abs(x2 - x1);
		final int dy = Math.abs(y2 - y1);
		final int sx = (x1 < x2) ? 1 : -1;
		final int sy = (y1 < y2) ? 1 : -1;
		int err = dx - dy;
		do
		{
			drawPixelChecked(x1, y1, c);
			final int e2 = 2 * err;
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
		drawPixelChecked(x2, y2, c);
	}
	
	public void drawRect(int x1, int y1, int x2, int y2, final int c)
	{
		// Do the clamping once at the start so we don't have to perform checks when drawing the pixel.
		x1 = MathUtils.clamp(x1, 0, width - 1);
		x2 = MathUtils.clamp(x2, 0, width - 1);
		y1 = MathUtils.clamp(y1, 0, height - 1);
		y2 = MathUtils.clamp(y2, 0, height - 1);
		
		if(mask == null)
		{
			// top
			final int ix = y1 * width;
			Arrays.fill(buffer, ix + x1, ix + x2 + 1, c);
			
			// bottom
			final int jx = y2 * width;
			Arrays.fill(buffer, jx + x1, jx + x2 + 1, c);
			
			for(int i = y1 + 1; i < y2; i++)
			{
				setPixel(x1, i, c);
				setPixel(x2, i, c);
			}
		}
		else
		{
			// top
			final int ix = y1 * width;
			for(int k = ix + x1; k < ix + x2 + 1; k++)
			{
				if(mask[k])
					buffer[k] = c;
			}
			
			// bottom
			final int jx = y2 * width;
			for(int k = jx + x1; k < jx + x2 + 1; k++)
			{
				if(mask[k])
					buffer[k] = c;
			}
			
			for(int i = y1 + 1; i < y2; i++)
			{
				drawPixel(x1, i, c);
				drawPixel(x2, i, c);
			}
		}
	}
	
	public void fillRect(int x1, int y1, int x2, int y2, int c)
	{
		x1 = MathUtils.clamp(x1, 0, width - 1);
		x2 = MathUtils.clamp(x2, 0, width - 1);
		y1 = MathUtils.clamp(y1, 0, height - 1);
		y2 = MathUtils.clamp(y2, 0, height - 1);
		
		if(mask == null)
		{
			for(; y1 <= y2; y1++)
			{
				final int k = y1 * width;
				Arrays.fill(buffer, k + x1, k + x2 + 1, c);
			}
		}
		else
		{
			for(; y1 <= y2; y1++)
			{
				final int offset = y1 * width;
				for(int k = offset + x1; k < offset + x2 + 1; k++)
				{
					if(mask[k])
						buffer[k] = c;
				}
			}
		}
	}
	
	public void drawPixel(int x, int y, int c)
	{
		final int loc = index(x, y);
		if(mask == null || mask[loc])
		{
			buffer[loc] = c;
		}
	}
	
	public void drawPixelChecked(int x, int y, int c)
	{
		if(x < 0 || y < 0 || x >= width || y >= height)
			return;
		final int loc = index(x, y);
		if(mask == null || mask[loc])
		{
			buffer[loc] = c;
		}
	}
	
	public void fill(int c)
	{
		if(mask == null)
		{
			this.clear(c);
		}
		else
		{
			for(int i = 0; i < buffer.length; i++)
			{
				if(mask[i])
					buffer[i] = c;
			}
		}
	}
	
	// Mask Manipulation
	
	public void fillMask(MaskMode mode)
	{
		if(this.mask == null)
			return;
		switch(mode)
		{
			case REP:
			case ADD:
				Arrays.fill(mask, true);
				break;
			case SUB:
				Arrays.fill(mask, false);
				break;
			case XOR:
				for(int i = 0; i < mask.length; i++)
					mask[i] = !mask[i];
				break;
			case AND:
				break;
		}
	}
	
	public void maskRect(int x1, int y1, int x2, int y2, MaskMode mode)
	{
		x1 = MathUtils.clamp(x1, 0, width - 1);
		x2 = MathUtils.clamp(x2, 0, width - 1);
		y1 = MathUtils.clamp(y1, 0, height - 1);
		y2 = MathUtils.clamp(y2, 0, height - 1);
		
		switch(mode)
		{
			case REP:
				this.fillMask(MaskMode.SUB);
			case ADD:
				for(; y1 <= y2; y1++)
				{
					final int k = y1 * width;
					Arrays.fill(mask, k + x1, k + x2 + 1, true);
				}
				break;
			case SUB:
				for(; y1 <= y2; y1++)
				{
					final int k = y1 * width;
					Arrays.fill(mask, k + x1, k + x2 + 1, false);
				}
				break;
			case XOR:
				for(; y1 <= y2; y1++)
				{
					final int offset = y1 * width;
					for(int k = offset + x1; k < offset + x2 + 1; k++)
					{
						mask[k] = !mask[k];
					}
				}
				break;
			case AND:
				// Before Rectangle
				final int offset = y1 * width + x1;
				Arrays.fill(mask, 0, offset, false);
				
				// After Rectangle
				final int offset2 = y2 * width + x2 + 1;
				Arrays.fill(mask, offset2, mask.length, false);
				
				// Within Rectangle
				final int onstep = x2 - x1 + 1;
				final int offstep = width - onstep;
				
				for(int i = offset + onstep; i < offset2; i += width)
					Arrays.fill(mask, i, i + offstep, false);
				break;
		}
	}
	
	public void setMaskEnabled(boolean enabled)
	{
		if(enabled)
		{
			if(mask == null)
				mask = new boolean[buffer.length];
		}
		else if(mask != null)
		{
			mask = null;
		}
	}
	
	public void toggleMask()
	{
		if(mask == null)
		{
			mask = new boolean[buffer.length];
		}
		else
		{
			mask = null;
		}
	}
	
	public boolean[] copyMask()
	{
		return mask == null ? null : Arrays.copyOf(mask, mask.length);
	}
	
	public boolean[] borrowMask()
	{
		return mask;
	}
	
	public void setMask(boolean[] mask)
	{
		this.mask = mask;
	}
	
	// Buffer Manipulation
	
	public void clear(int c)
	{
		Arrays.fill(buffer, c);
	}
	
	public void setPixel(int x, int y, int c)
	{
		buffer[index(x, y)] = c;
	}
	
	public int getPixel(int x, int y)
	{
		return buffer[index(x, y)];
	}
	
	public int getIndex(int x, int y)
	{
		return index(x, y);
	}
	
	private int index(int x, int y)
	{
		return y * width + x;
	}
	
	public int[] copyBuffer()
	{
		return Arrays.copyOf(buffer, buffer.length);
	}
	
	public int[] borrowBuffer()
	{
		return buffer;
	}
	
	public void setBuffer(int[] buffer)
	{
		this.buffer = buffer;
	}
	
	// Create a RawImage which has direct access to the pixels of the BufferedImage.
	// This could be quite unreliable.
	public static RawImage unwrapBufferedImage(BufferedImage image)
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
		if(image.mask == null)
			return new RawImage(image.width, image.height, image.copyBuffer());
		return new RawImage(image.width, image.height, image.copyBuffer(), image.copyMask());
	}
	
	public void copyRegion(RawImage image)
	{
		if(image == this)
			return;
		if(this.buffer.length != image.buffer.length)
			throw new RuntimeException("Cannot copy from a different sized RawImage");
		
		if(this.mask == null)
		{
			System.arraycopy(image.buffer, 0, buffer, 0, buffer.length);
		}
		else
		{
			for(int i = 0; i < buffer.length; i++)
			{
				if(mask[i])
					this.buffer[i] = image.buffer[i];
			}
		}
	}
	
	public void copyFrom(RawImage image, boolean withMask)
	{
		if(image == this)
			return;
		if(this.buffer.length != image.buffer.length)
			throw new RuntimeException("Cannot copy from a different sized RawImage");
		System.arraycopy(image.buffer, 0, buffer, 0, buffer.length);
		if(withMask)
		{
			if(image.mask == null)
				this.mask = null;
			else if(this.mask == null)
				this.mask = Arrays.copyOf(image.mask, image.mask.length);
			else
				System.arraycopy(image.mask, 0, mask, 0, mask.length);
		}
	}
	
	public void copyMaskFrom(RawImage image)
	{
		if(image == this)
			return;
		
		if(image.mask == null)
			this.mask = null;
		else if(this.mask == null)
			this.mask = Arrays.copyOf(image.mask, image.mask.length);
		else if(this.mask.length != image.mask.length)
			throw new RuntimeException("Cannot copy from a different sized RawImage");
		else
			System.arraycopy(image.mask, 0, mask, 0, mask.length);
	}
	
	public void dispose()
	{
		buffer = null;
		mask = null;
	}
	
	public boolean isMaskEnabled()
	{
		return mask != null;
	}
}
