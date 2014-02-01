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

package heroesgrave.paint.image.blend;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class MaskReplace extends BlendMode
{
	public MaskReplace()
	{
		super("Mask Replace");
	}
	
	public void compose(Raster src, Raster dst, WritableRaster out)
	{
		int w = out.getWidth();
		int h = out.getHeight();
		
		int n = src.getNumBands();
		
		int[] ip = new int[n], op = new int[n];
		
		for(int i = 0; i < w; i++)
		{
			for(int j = 0; j < h; j++)
			{
				if(src.getSample(i, j, 3) == 0)
					dst.getPixel(i, j, ip);
				else
					src.getPixel(i, j, ip);
				System.arraycopy(ip, 0, op, 0, n);
				out.setPixel(i, j, op);
			}
		}
	}
}