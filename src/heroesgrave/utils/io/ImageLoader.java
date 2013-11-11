/*
 *	Copyright 2013 HeroesGrave & Longor1996
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

import heroesgrave.paint.main.Popup;
import heroesgrave.utils.io.importers.ImporterBIN;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 * 
 * NOTES:
 * TODO: Rename this class into something more appropriate, because this is NOT just an ImageLoader!
 * TODO: Make it, so the ImageLoader automatically converts the image into a more appropriate FASTER format for image editing. (INT_ARGB!)
 * 
 **/
public class ImageLoader
{
	private static final HashMap<String, ImageImporter> importers = new HashMap<String, ImageImporter>();
	
	static
	{
		// Set this to FALSE, because it is NOT faster to use this!
		ImageIO.setUseCache(false);
		
		/// initialize importers?
		
		// Nothing here yet! Feel free to expand by adding new image-importers!
		add(new ImporterBIN());
		
	}
	
	public static void add(ImageImporter exporter)
	{
		importers.put(exporter.getFormat(), exporter);
	}
	
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
				String fileName = file.getAbsolutePath();
				String extension = "";
				
				int i = fileName.lastIndexOf('.');
				
				if(i > 0)
				{
					extension = fileName.substring(i + 1);
				}
				
				// Get the ImageImporter
				ImageImporter importer = importers.get(extension);
				
				// If there is a custom importer for the given format, use the custom importer...
				if(importer != null)
					return importer.read(file);
				else
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
			Popup.show("Save Image", "An error occured while trying to save the image in " + format + " format to " + path + ".");
		}
	}
	
	public static void addAllImporters(JFileChooser chooser)
	{
		
		for(Entry<String, ImageImporter> importer : importers.entrySet())
		{
			chooser.addChoosableFileFilter(importer.getValue());
		}
		
	}
}