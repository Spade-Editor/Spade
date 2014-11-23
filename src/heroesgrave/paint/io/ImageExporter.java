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
import heroesgrave.paint.io.exporters.ExporterGenericImageIO;
import heroesgrave.paint.main.Popup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;

public abstract class ImageExporter extends FileFilter
{
	private static final HashMap<String, ImageExporter> exporters = new HashMap<String, ImageExporter>();
	
	static
	{
		add(new ExporterGenericImageIO("png", "PNG - Portable Network Graphics Image"));
	}
	
	/**
	 * Returns the file extension this ImageExporter exports to.<br>
	 * (Lowercase, without the dot!)
	 **/
	public abstract String getFileExtension();
	
	public abstract void save(Document document, File destination) throws IOException;
	
	@Override
	public boolean accept(File f)
	{
		if(f.getAbsolutePath().endsWith("." + getFileExtension()))
			return true;
		
		return false;
	}
	
	public static void add(ImageExporter exporter)
	{
		exporters.put(exporter.getFileExtension(), exporter);
	}
	
	public static ImageExporter get(String extension)
	{
		return exporters.get(extension.toLowerCase());
	}
	
	protected static void writeImage(BufferedImage image, String format, String path)
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
	
	public static void addAllExporters(WebFileChooser chooser)
	{
		for(ImageExporter exporter : exporters.values())
		{
			chooser.addChoosableFileFilter(exporter);
		}
	}
}
