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
				file.createNewFile();
			ImageIO.write(image, format, file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
