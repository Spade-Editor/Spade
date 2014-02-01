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

import heroesgrave.paint.gui.SimpleModalProgressDialog;
import heroesgrave.paint.image.Canvas;
import heroesgrave.utils.io.ImageExporter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TGA Exporter.
 * 
 * @author Longor1996 & /INTERNET/
 **/
public class ExporterTGA extends ImageExporter
{
	@Override
	public String getFileExtension()
	{
		return "tga";
	}
	
	@Override
	public void export(Canvas canvas, File destination) throws IOException
	{
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		
		// ID Length
		out.writeByte((byte) 0);
		
		// Color Map
		out.writeByte((byte) 0);
		
		// Image Type
		out.writeByte((byte) 2);
		
		// Color Map - Ignored
		out.writeShort(flipEndian((short) 0));
		out.writeShort(flipEndian((short) 0));
		out.writeByte((byte) 0);
		
		// X, Y Offset
		out.writeShort(flipEndian((short) 0));
		out.writeShort(flipEndian((short) 0));
		
		// Width, Height, Depth
		out.writeShort(flipEndian((short) canvas.getWidth()));
		out.writeShort(flipEndian((short) canvas.getHeight()));
		
		out.writeByte((byte) 32);
		// Image Descriptor (can't be 0 since we're using 32-bit TGAs)
		// needs to not have 0x20 set to indicate it's not a flipped image
		out.writeByte((byte) 1);
		
		int[] buf = new int[canvas.getWidth() * canvas.getHeight()];
		canvas.getFullImage().getRGB(0, 0, canvas.getWidth(), canvas.getHeight(), buf, 0, canvas.getWidth());
		
		// Write out the image data
		int c;
		SimpleModalProgressDialog DIALOG = new SimpleModalProgressDialog("Saving...", "Saving Image...", buf.length + 1);
		
		for(int y = canvas.getHeight() - 1, count = 0; y >= 0; y--)
		{
			for(int x = 0; x < canvas.getWidth(); x++, count++)
			{
				c = buf[x + y * canvas.getWidth()];
				
				out.writeByte((byte) (c & 0xff));
				out.writeByte((byte) ((c >> 8) & 0xff));
				out.writeByte((byte) ((c >> 16) & 0xff));
				out.writeByte((byte) ((c >> 24) & 0xff));
				
				if(count % 128 == 0)
				{
					DIALOG.setValue(count);
				}
			}
		}
		
		out.flush();
		out.close();
		DIALOG.close();
	}
	
	private short flipEndian(short signedShort)
	{
		int input = signedShort & 0xFFFF;
		return (short) (input << 8 | (input & 0xFF00) >>> 8);
	}
	
	@Override
	public String getFileExtensionDescription()
	{
		return "TGA - Tagged Image File Format";
	}
}