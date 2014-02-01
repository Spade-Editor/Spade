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

package heroesgrave.utils.io;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.main.Popup;
import heroesgrave.utils.io.exporters.ExporterBIN;
import heroesgrave.utils.io.exporters.ExporterGenericImageIO;
import heroesgrave.utils.io.exporters.ExporterJPEG;
import heroesgrave.utils.io.exporters.ExporterLBIN;
import heroesgrave.utils.io.exporters.ExporterTGA;
import heroesgrave.utils.io.exporters.ExporterZipBIN;
import heroesgrave.utils.io.exporters.ExporterZipLBIN;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public abstract class ImageExporter extends FileFilter
{
	public static ArrayList<ImageExporter> exporters = new ArrayList<ImageExporter>();
	
	public static void registerExporters()
	{
		// Add ALL the exporters!
		exporters.add(new ExporterGenericImageIO("png", "PNG - Portable Network Graphics Image"));
		exporters.add(new ExporterGenericImageIO("bmp", "BMP - Microsoft Bitmap Image"));
		exporters.add(new ExporterGenericImageIO("gif", "GIF - Graphics Interchange Format"));
		exporters.add(new ExporterJPEG());
		exporters.add(new ExporterTGA());
		exporters.add(new ExporterBIN());
		exporters.add(new ExporterZipBIN());
		exporters.add(new ExporterLBIN());
		exporters.add(new ExporterZipLBIN());
	}
	
	/**
	 * Returns the file extension this ImageExporter exports to.<br>
	 * (Lowercase, without the dot!)
	 **/
	public abstract String getFileExtension();
	
	public abstract String getFileExtensionDescription();
	
	public abstract void export(Canvas canvas, File destination) throws IOException;
	
	@Override
	public boolean accept(File f)
	{
		if(f.isDirectory())
			return true;
		
		if(f.getAbsolutePath().endsWith("." + getFileExtension()))
			return true;
		
		return false;
	}
	
	@Override
	public String getDescription()
	{
		return getFileExtensionDescription();
	}
	
	public static ImageExporter get(String extension)
	{
		for(ImageExporter exporter : exporters)
		{
			if(exporter.getFileExtension().equalsIgnoreCase(extension))
				return exporter;
		}
		
		throw new RuntimeException("Image Exporter for the given Format '" + extension + "' could not be found!");
	}
	
	public static void add(ImageExporter exporter)
	{
		if(exporter == null)
			throw new IllegalArgumentException("Input cannot be null!");
		
		exporters.add(exporter);
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
}