/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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

package heroesgrave.paint.imageops;

import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;

public class FlipHoriz extends ImageOp
{
	public void operation()
	{
		BufferedImage image = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				newImage.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
			}
		}
		
		Paint.addChange(new KeyFrame(newImage));
	}
}