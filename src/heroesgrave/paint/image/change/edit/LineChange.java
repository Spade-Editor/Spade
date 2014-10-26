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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Yes, we can implement both an extension of IChange and Serialised.
 * This should be used when the change type stores no extra information.
 */
public class LineChange implements IEditChange, Serialised
{
	private short sx, sy, ex, ey;
	private int c;
	
	public LineChange()
	{
		
	}
	
	public LineChange(short x, short y, int c)
	{
		this.sx = this.ex = x;
		this.sy = this.ey = y;
		this.c = c;
	}
	
	public boolean end(short x, short y)
	{
		if(x == ex && y == ey)
			return false;
		this.ex = x;
		this.ey = y;
		return true;
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.drawLine(sx, sy, ex, ey, c);
	}
	
	@Override
	public LineChange decode()
	{
		return this;
	}
	
	@Override
	public LineChange encode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeShort(sx);
		out.writeShort(sy);
		out.writeShort(ex);
		out.writeShort(ey);
		out.writeInt(c);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		sx = in.readShort();
		sy = in.readShort();
		ex = in.readShort();
		ey = in.readShort();
		c = in.readInt();
	}
	
	@Override
	public boolean isMarker()
	{
		return false;
	}
}
