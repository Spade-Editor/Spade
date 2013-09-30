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

public class PixelChange implements Change
{
	public int x, y, o, n;

	public PixelChange(int x, int y, int n)
	{
		this.x = x;
		this.y = y;
		this.n = n;
	}

	public BufferedImage apply(BufferedImage image)
	{
		o = image.getRGB(x, y);
		image.setRGB(x, y, n);
		return image;
	}

	public BufferedImage revert(BufferedImage image)
	{
		image.setRGB(x, y, o);
		return image;
	}

	public int getSize()
	{
		return 4;
	}

	public boolean samePos(int x, int y)
	{
		return (this.x == x && this.y == y);
	}
}
