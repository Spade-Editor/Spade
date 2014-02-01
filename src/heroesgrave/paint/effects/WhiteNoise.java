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

package heroesgrave.paint.effects;

import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;
import java.util.Random;

public class WhiteNoise extends ImageOp
{
	public void operation()
	{
		// TODO: Implement a GUI to make it possible to modify the 'chance' value.
		
		BufferedImage old = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Random random = new Random();
		int chance = 50;
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				int before = old.getRGB(i, j);
				int after = before;
				
				int rand = random.nextInt(256) & 0xFF;
				
				if(random.nextFloat() * 100 > chance)
				{
					after &= 0xFF000000;
					
					after |= rand << 0;
					after |= rand << 8;
					after |= rand << 16;
				}
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new KeyFrame(newImage));
	}
}