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
import heroesgrave.paint.image.change.IRevEditChange;
import heroesgrave.paint.io.Serialised;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Yes, we can implement both an extension of IChange and Serialised.
 * This should be used when the change type stores no extra information.
 */
public class PixelChange implements IRevEditChange, Serialised
{
	private short x, y;
	private int c, o;
	
	public PixelChange()
	{
		
	}
	
	public PixelChange(short x, short y, int c)
	{
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	@Override
	public void apply(RawImage image)
	{
		o = image.getPixel(x, y);
		image.setPixel(x, y, c);
	}
	
	@Override
	public void revert(RawImage image)
	{
		image.setPixel(x, y, o);
	}
	
	@Override
	public PixelChange decode()
	{
		return this;
	}
	
	@Override
	public PixelChange encode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeShort(x);
		out.writeShort(y);
		out.writeInt(c);
		out.writeInt(o);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		x = in.readShort();
		y = in.readShort();
		c = in.readInt();
		o = in.readInt();
	}
	
	@Override
	public boolean isMarker()
	{
		return false;
	}
}
