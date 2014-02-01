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

package heroesgrave.utils.io.exporters;

import heroesgrave.paint.image.Canvas;
import heroesgrave.utils.io.ImageExporter;

import java.io.File;
import java.io.IOException;

/**
 * GENERIC Exporter.
 * 
 * This exporter uses the ImageIO API to export images. It can only handle the registered formats of the JRE.
 * 
 * @author Longor1996 & /ORACLE/
 **/
public class ExporterGenericImageIO extends ImageExporter
{
	public final String format;
	public final String description;
	
	public ExporterGenericImageIO(String format, String description)
	{
		this.format = format;
		this.description = description;
	}
	
	@Override
	public String getFileExtension()
	{
		return format;
	}
	
	@Override
	public String getFileExtensionDescription()
	{
		return description;
	}
	
	@Override
	public void export(Canvas canvas, File destination) throws IOException
	{
		writeImage(canvas.getFullImage(), getFileExtension().toUpperCase(), destination.getAbsolutePath());
	}
}