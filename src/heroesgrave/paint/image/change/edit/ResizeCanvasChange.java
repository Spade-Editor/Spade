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
import heroesgrave.paint.image.change.IResizingChange;
import heroesgrave.paint.image.change.SerialisedChange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResizeCanvasChange extends SerialisedChange implements IResizingChange
{
	private short width, height;
	
	public ResizeCanvasChange(int width, int height)
	{
		this.width = (short) width;
		this.height = (short) height;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeShort(width);
		out.writeShort(height);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		width = in.readShort();
		height = in.readShort();
	}
	
	@Override
	public RawImage apply(RawImage image)
	{
		RawImage newImage = new RawImage(width, height);
		int[] buffer = image.borrowBuffer();
		int[] newBuffer = newImage.borrowBuffer();
		int w = Math.min(width, image.width);
		int h = Math.min(height, image.height);
		for(int j = 0; j < h; j++)
		{
			System.arraycopy(buffer, j * image.width, newBuffer, j * width, w);
		}
		return newImage;
	}
	
	@Override
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
}
