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

package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.io.Serialised;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FloodPathChange implements IEditChange
{
	protected ArrayList<Point> points = new ArrayList<Point>();
	protected int colour;
	
	public FloodPathChange(short x, short y, int colour)
	{
		this(new Point(x, y), colour);
	}
	
	public FloodPathChange(Point p, int colour)
	{
		this.colour = colour;
		points.add(p);
	}
	
	public boolean moveTo(short x, short y)
	{
		return this.moveTo(new Point(x, y));
	}
	
	public boolean moveTo(Point p)
	{
		if(points.get(points.size() - 1).equals(p))
			return false;
		points.add(p);
		return true;
	}
	
	@Override
	public Serial encode()
	{
		short[] data = new short[points.size() * 2];
		int i = 0;
		for(Point p : points)
		{
			data[i++] = (short) p.x;
			data[i++] = (short) p.y;
		}
		return new Serial(data, colour);
	}
	
	@Override
	public void apply(RawImage image)
	{
		for(Point p : points)
		{
			floodFill(image, p.x, p.y, colour);
		}
	}
	
	// XXX: move to RawImage instance method
	// scan line flood fill as described in Wikipedia
	private static void floodFill(RawImage image, int x, int y, int color)
	{
		final int iw = image.width;
		
		if(x < 0 | y < 0 || x >= iw || y >= image.height)
			return;
		
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		int targetColor = buffer[x + y * iw];
		
		if(targetColor == color || mask != null && !mask[x + y * iw])
			return;
		
		int[] stack = new int[1024];
		int head = -1;
		
		stack[++head] = x + y * iw;
		
		while(head >= 0)
		{
			int n = stack[head--];
			if(buffer[n] == targetColor) // mask predicate is guaranteed here
			{
				final int ny = n / iw;
				final int nyo = ny * iw;
				final int nxl = nyo + iw;
				int w = n, e = n;
				// scan out on each side of current pixel
				while(w >= nyo && buffer[w] == targetColor && (mask == null || mask[w]))
					--w;
				while(e < nxl && buffer[e] == targetColor && (mask == null || mask[e]))
					++e;
				// fill in between
				w += 1;
				
				for(int i = w; i < e; ++i)
					buffer[i] = color;
				
				w -= iw;
				e -= iw;
				
				if(ny > 0)
					for(int i = w; i < e; ++i)
						if(buffer[i] == targetColor && (mask == null || mask[i]))
						{
							stack[++head] = i;
							if(head == stack.length - 1)
								stack = Arrays.copyOf(stack, stack.length * 2);
						}
				
				w += iw * 2;
				e += iw * 2;
				
				if(ny < image.height - 1)
					for(int i = w; i < e; ++i)
						if(buffer[i] == targetColor && (mask == null || mask[i]))
						{
							stack[++head] = i;
							if(head == stack.length - 1)
								stack = Arrays.copyOf(stack, stack.length * 2);
						}
			}
		}
	}
	
	public static class Serial implements Serialised
	{
		private short[] points;
		private int colour;
		
		public Serial()
		{
			
		}
		
		public Serial(short[] data, int colour)
		{
			this.colour = colour;
			this.points = data;
		}
		
		@Override
		public FloodPathChange decode()
		{
			FloodPathChange change = new FloodPathChange(points[0], points[1], colour);
			for(int i = 2; i < points.length; i += 2)
				change.moveTo(points[i], points[i + 1]);
			return change;
		}
		
		@Override
		public void write(DataOutputStream out) throws IOException
		{
			out.writeInt(colour);
			out.writeInt(points.length);
			for(short s : points)
				out.writeShort(s);
		}
		
		@Override
		public void read(DataInputStream in) throws IOException
		{
			colour = in.readInt();
			points = new short[in.readInt()];
			for(int i = 0; i < points.length; i++)
				points[i] = in.readShort();
		}
		
		@Override
		public boolean isMarker()
		{
			return false;
		}
	}
}
