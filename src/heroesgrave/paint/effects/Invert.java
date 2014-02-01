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

public class Invert extends ImageOp
{
	public void operation()
	{
		BufferedImage image = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		// channel buffer var's
		int channel_argb;
		int channel_rgb;
		int channel_a;
		
		// channel mask's
		int MASK_RGB = 0xFFFFFF;
		int MASK_ALPHA = 0xFF000000;
		
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				// get pixel
				channel_argb = image.getRGB(i, j);
				
				// split ARGB into RGB and A
				channel_rgb = channel_argb & MASK_RGB;
				channel_a = channel_argb & MASK_ALPHA;
				
				// flip the colors
				channel_rgb = 0xFFFFFF ^ channel_rgb;
				
				// set
				newImage.setRGB(i, j, (channel_rgb | channel_a));
			}
		}
		
		Paint.addChange(new KeyFrame(newImage));
	}
}