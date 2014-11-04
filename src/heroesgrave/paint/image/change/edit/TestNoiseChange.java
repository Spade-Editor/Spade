// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IGeneratorChange;
import heroesgrave.utils.misc.RandomUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class TestNoiseChange extends IGeneratorChange
{
	public long seed;
	
	public TestNoiseChange()
	{
		this.seed = RandomUtils.rLong();
	}
	
	public void write(DataOutputStream out) throws IOException
	{
	}
	
	public void read(DataInputStream in) throws IOException
	{
	}
	
	public RawImage generate(int width, int height)
	{
		RawImage image = new RawImage(width, height);
		Random random = new Random(seed);
		
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				int color = 0xff000000;
				color |= ((i * 256) / width) << 0;
				color |= ((j * 256) / height) << 8;
				color |= random.nextInt(0xff) << 16;
				image.setPixel(i, j, color);
			}
		}
		
		return image;
	}
}
