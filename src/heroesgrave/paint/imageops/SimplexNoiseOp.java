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

package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.math.SimplexNoise;

import java.awt.image.BufferedImage;

public class SimplexNoiseOp extends ImageOp
{
	public void operation()
	{
		// TODO: Add dialog with sliders to configure the noise!
		
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		SimplexNoise noise = new SimplexNoise(System.currentTimeMillis());
		
		double scale = 0.125;
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				int before = old.getRGB(i, j);
				int after = before;
				
				double valD = noise.noiseO(i * scale, j * scale, 8, 1 , 0.25);
				valD = valD * 0.5D + 0.5D;
				int valI = MathUtils.clamp((int)(valD * 256),255,0) & 0xFF;
				
				after &= 0xFF000000;
				
				after |= valI << 0;
				after |= valI << 8;
				after |= valI << 16;
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
	
}