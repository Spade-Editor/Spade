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

package heroesgrave.utils.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader
{
	private ImageLoader()
	{
		
	}
	
	public static BufferedImage loadImage(String path)
	{
		File file = new File(path);
		
		if(file.exists())
		{
			try
			{
				return ImageIO.read(file);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
			throw new RuntimeException("The resource \"" + path + "\" was missing!");
		
		return null;
	}
	
	public static void writeImage(BufferedImage image, String format, String path)
	{
		File file = new File(path);
		
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			ImageIO.write(image, format, file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}