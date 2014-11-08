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
import heroesgrave.paint.image.change.IImageChange;
import heroesgrave.paint.image.change.SerialisedChange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SetMaskChange extends SerialisedChange implements IImageChange
{
	private boolean[] mask;
	
	public SetMaskChange()
	{
		
	}
	
	public SetMaskChange(boolean[] mask)
	{
		this.mask = mask;
	}
	
	@Override
	public RawImage apply(RawImage image)
	{
		image.setMask(mask);
		return image;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeInt(mask.length);
		for(int i = 0; i < mask.length; i++)
			out.writeBoolean(mask[i]);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		mask = new boolean[in.readInt()];
		for(int i = 0; i < mask.length; i++)
			mask[i] = in.readBoolean();
	}
}
