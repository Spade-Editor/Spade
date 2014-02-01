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
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.math.SimplexNoise;

import java.awt.image.BufferedImage;

public class SimplexNoiseOp extends ImageOp
{
	@Override
	public void operation()
	{
		// TODO: Add dialog with sliders to configure the noise!
		noise_gen();
		
	}
	
	public void noise_gen()
	{
		BufferedImage newImage = new BufferedImage(Paint.main.gui.canvas.getWidth(), Paint.main.gui.canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
		SimplexNoise noise = new SimplexNoise(System.nanoTime());
		
		double scale = 1f / 64f;
		
		int OCTAVES = 8;
		double START_SCALE = 0.5f;
		double MULTIPLY_SCALE = 2.125f;
		double REDUCE = 0.3f;
		
		for(int i = 0; i < newImage.getWidth(); i++)
		{
			for(int j = 0; j < newImage.getHeight(); j++)
			{
				int after = 0xFF000000;
				
				double valD = noise.noiseO2(i * scale, j * scale, OCTAVES, START_SCALE, MULTIPLY_SCALE, REDUCE);
				valD = valD * 0.5D + 0.5D;
				int valI = MathUtils.clamp((int) (valD * 256), 255, 0) & 0xFF;
				
				after |= valI << 0;
				after |= valI << 8;
				after |= valI << 16;
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new KeyFrame(newImage));
	}
}