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

package heroesgrave.paint.io;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage;
import heroesgrave.utils.misc.Metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;

public abstract class ImageImporter extends FileFilter
{
	private static final HashMap<String, ImageImporter> importers = new HashMap<String, ImageImporter>();
	
	static
	{
		// Set this to FALSE, because it is NOT faster to use this!
		ImageIO.setUseCache(false);
	}
	
	/**
	 * Reads an Image.
	 * @throws IOException 
	 **/
	public abstract void load(File file, Document doc) throws IOException;
	
	public abstract String getFileExtension();
	
	@Override
	public boolean accept(File f)
	{
		if(f.isDirectory())
			return true;
		
		if(f.getAbsolutePath().endsWith("." + getFileExtension()))
			return true;
		
		return false;
	}
	
	public static void add(ImageImporter importer)
	{
		importers.put(importer.getFileExtension(), importer);
	}
	
	public static ImageImporter get(String extension)
	{
		return importers.get(extension);
	}
	
	public static void loadImage(String path, Document doc)
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
				{
					importer.load(file, doc);
				}
				else
				{
					RawImage image = RawImage.fromBufferedImage(ImageIO.read(file));
					doc.setDimensions(image.width, image.height);
					doc.setRoot(new Layer(doc, image, new Metadata()));
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
			throw new RuntimeException("The resource \"" + path + "\" was missing!");
	}
	
	public static void addAllImporters(WebFileChooser chooser)
	{
		for(ImageImporter importer : importers.values())
		{
			chooser.addChoosableFileFilter(importer);
		}
	}
}
