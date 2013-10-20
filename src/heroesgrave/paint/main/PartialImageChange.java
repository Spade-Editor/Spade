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

public class PartialImageChange implements Change
{
	private short x, y;
	private BufferedImage oldPart, newPart;
	
	public PartialImageChange(int x, int y, BufferedImage change)
	{
		this.x = (short) x;
		this.y = (short) y;
		this.newPart = change;
	}

	public BufferedImage apply(BufferedImage image)
	{
		oldPart = image.getSubimage(x, y, newPart.getWidth(), newPart.getHeight());
		image.getGraphics().drawImage(newPart, x, y, null);
		return image;
	}
	
	public BufferedImage revert(BufferedImage image)
	{
		image.getGraphics().drawImage(oldPart, x, y, null);
		return image;
	}
	
	public int getSize()
	{
		return 1 + (oldPart.getWidth()*oldPart.getHeight());
	}
	
	public boolean samePos(int x, int y)
	{
		return false;
	}
}