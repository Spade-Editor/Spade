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

package heroesgrave.utils.io.importers;

import heroesgrave.paint.gui.SimpleModalProgressDialog;
import heroesgrave.paint.image.Canvas;
import heroesgrave.utils.io.ImageImporter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * @author Longor1996
 **/
public class ImporterZipBIN extends ImageImporter
{
	
	@Override
	public Canvas read(File file) throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		int width = in.readInt();
		int height = in.readInt();
		int surfaceArea = width * height;
		int zipLen = in.readInt();
		int[] raw = new int[surfaceArea];
		byte[] zipd = new byte[zipLen];
		SimpleModalProgressDialog DIALOG = new SimpleModalProgressDialog("Working!", "Loading Image...", surfaceArea);
		DIALOG.setMessage("Reading compressed Image-bytes...");
		in.readFully(zipd);
		
		DIALOG.setMessage("Converting to Java2D readable format...");
		
		ByteArrayInputStream inImg = new ByteArrayInputStream(zipd);
		GZIPInputStream inImgZip = new GZIPInputStream(inImg);
		DataInputStream inImgZipData = new DataInputStream(inImgZip);
		
		for(int index = 0, pixel = 0; index < surfaceArea; index++)
		{
			// pixel = RGBA
			pixel = (inImgZipData.read() << 24) | (inImgZipData.read() << 16) | (inImgZipData.read() << 8) | (inImgZipData.read());
			
			// fastest possible conversion from RGBA to ARGB (?)
			int RGB = ((pixel & 0xFFFFFFFF) >> 8) & 0xFFFFFF;
			int A = pixel & 0xFF;
			pixel = (A << 24) | RGB;
			
			// raw[index] = ARGB
			raw[index] = pixel;
			
			if(index % 128 == 0)
			{
				DIALOG.setValue(index);
			}
		}
		
		in.read();///E
		in.read();///O
		in.read();///I
		in.read();///D
		
		DIALOG.setMessage("Done!");
		
		DIALOG.close();
		in.close();
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, width, height, raw, 0, width);
		
		return new Canvas("Background", image);
	}
	
	@Override
	public String getFormat()
	{
		return "zbin";
	}
	
	@Override
	public String getDescription()
	{
		return "ZBIN - Raw Compressed Binary Image Data Format";
	}
	
}