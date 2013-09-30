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

package heroesgrave.paint.main;

import java.awt.image.BufferedImage;

public class MultiChange implements Change
{
	public Change[] changes;

	public MultiChange(Change... c)
	{
		this.changes = c;
	}

	public BufferedImage apply(BufferedImage image)
	{
		for(int i = 0; i < changes.length; i++)
		{
			changes[i].apply(image);
		}
		return image;
	}

	public BufferedImage revert(BufferedImage image)
	{
		for(int i = changes.length - 1; i >= 0; i--)
		{
			changes[i].revert(image);
		}
		return image;
	}

	public int getSize()
	{
		int size = 0;
		for(Change c : changes)
		{
			size += c.getSize();
		}
		return size;
	}

	public boolean samePos(int x, int y)
	{
		for(Change c : changes)
		{
			if(c.samePos(x, y))
				return true;
		}
		return false;
	}
}
