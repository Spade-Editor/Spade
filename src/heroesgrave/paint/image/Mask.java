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

import heroesgrave.paint.image.RawImage.MaskMode;
import heroesgrave.utils.math.MathUtils;

import java.util.Arrays;

public class Mask
{
	private int width, height;
	private boolean[] mask;
	private boolean enabled;
	
	public Mask(int width, int height)
	{
		this.mask = new boolean[width * height];
	}
	
	public boolean[] getRaw()
	{
		return mask;
	}
	
	public boolean get(int i)
	{
		return !enabled || mask[i];
	}
	
	public void set(int i, boolean value)
	{
		if(enabled)
		{
			mask[i] = value;
		}
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean value)
	{
		this.enabled = value;
	}
	
	public int length()
	{
		return mask.length;
	}
	
	// Mask Manipulation
	
	public void fillMask(MaskMode mode)
	{
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
	
	public void move(int dx, int dy)
	{
		final int offset = dx + dy * width;
		if(Math.abs(dx) >= width || Math.abs(dy) >= height)
		{
			Arrays.fill(mask, false);
			return;
		}
		else if(offset == 0)
			return;
		
		int src = 0;
		int dst = offset;
		if(offset < 0)
		{
			src = -offset;
			dst = 0;
		}
		final int len = mask.length - (src + dst);
		
		// First translate the mask.
		System.arraycopy(mask, src, mask, dst, len);
		// Then do bounds clipping.
		Arrays.fill(mask, 0, dst, false); // Cut out top.
		Arrays.fill(mask, mask.length - src, mask.length, false); // Cut out bottom
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
	}
}
