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

import heroesgrave.paint.image.Canvas;
import heroesgrave.utils.io.ImageImporter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class ImporterZipLBIN extends ImageImporter
{
	public Canvas read(File file) throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		int width = in.readInt();
		int height = in.readInt();
		int zipLen = in.readInt();
		
		byte[] zipd = new byte[zipLen];
		in.readFully(zipd);
		
		ByteArrayInputStream inImg = new ByteArrayInputStream(zipd);
		GZIPInputStream inImgZip = new GZIPInputStream(inImg);
		
		Canvas root = readLayer(new DataInputStream(inImgZip), width, height, "Background");
		
		// Read end sequence
		in.readInt();
		
		in.close();
		
		return root;
	}
	
	private Canvas readLayer(DataInputStream in, int width, int height, String name) throws IOException
	{
		int[] raw = new int[width * height];
		
		for(int I = 0; I < width * height; I++)
		{
			// get
			int pixel = in.readInt();
			
			// convert
			int R = (pixel >> 24) & 0xff;
			int G = (pixel >> 16) & 0xff;
			int B = (pixel >> 8) & 0xff;
			int A = (pixel) & 0xff;
			
			pixel = 0;
			pixel |= B;
			pixel |= G << 8;
			pixel |= R << 16;
			pixel |= A << 24;
			
			// put
			raw[I] = pixel;
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, width, height, raw, 0, width);
		
		Canvas canvas = new Canvas(name, image);
		
		int childCount = in.readInt();
		
		for(int i = 0; i < childCount; i++)
		{
			canvas.addLayer(readLayer(in, width, height, "Layer" + i));
		}
		
		return canvas;
	}
	
	public String getFormat()
	{
		return "zlbin";
	}
	
	public String getDescription()
	{
		return "ZLBIN - Raw Compressed Layered Binary Image Data Format";
	}
}