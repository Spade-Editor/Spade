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

import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.math.Perlin;

import java.awt.image.BufferedImage;

@SuppressWarnings("deprecation")
public class PerlinNoiseOp extends ImageOp
{
	
	public void operation()
	{
		// TODO: Add dialog with sliders to configure the noise!
		
		BufferedImage old = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Perlin noise = new Perlin(System.currentTimeMillis());
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				
				// TODO: Fix the 'stripe' issue... this Op cannot be used with this strange bug being in place.
				// The Perlin class is PROBABLY bugged!
				double valD = noise.noise3d_simplex4(i, 0, j);
				
				valD = valD * 0.5D + 0.5D;
				int valI = MathUtils.clamp((int) (valD * 256), 255, 0) & 0xFF;
				
				int after = 0xFF000000;
				after |= valI << 0;
				after |= valI << 8;
				after |= valI << 16;
				
				if(i == j)
				{
					after = 0xFFFF00FF;
				}
				
				newImage.setRGB(i, j, after);
			}
		}
		
		//Paint.addChange(new StoredImageChange(newImage));
	}
	
}