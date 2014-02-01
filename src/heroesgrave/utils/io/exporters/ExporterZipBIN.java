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

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * BIN Image Exporter.
 * 
 * Format is as follows:
 * signed_int32 width
 * signed_int32 height
 * INT_RGBA[width*height] imageData
 * 
 * @author Longor1996
 **/
public class ExporterZipBIN extends ImageExporter
{
	@Override
	public String getFileExtension()
	{
		return "zbin";
	}
	
	@Override
	public void export(Canvas canvas, File destination) throws IOException
	{
		BufferedImage image = canvas.getFullImage();
		
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		
		output.writeInt(canvas.getWidth());
		output.writeInt(canvas.getHeight());
		
		// Buffer the Image-Data
		int[] buf = new int[image.getWidth() * image.getHeight()];
		byte[] abyte0 = new byte[canvas.getWidth() * canvas.getHeight() * 4];
		
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), buf, 0, image.getWidth());
		
		SimpleModalProgressDialog DIALOG = new SimpleModalProgressDialog("Saving...", "Saving Image...", buf.length + 1);
		
		// Go trough ALL the pixels and convert from INT_ARGB to INT_RGBA
		for(int k = 0; k < buf.length; k++)
		{
			int A = (buf[k] >> 24) & 0xff;
			int R = (buf[k] >> 16) & 0xff;
			int G = (buf[k] >> 8) & 0xff;
			int B = buf[k] & 0xff;
			
			abyte0[(k * 4) + 0] = (byte) R;//R
			abyte0[(k * 4) + 1] = (byte) G;//G
			abyte0[(k * 4) + 2] = (byte) B;//B
			abyte0[(k * 4) + 3] = (byte) A;//A
			
			if(k % 128 == 0)
			{
				DIALOG.setValue(k);
			}
		}
		
		DIALOG.setMessage("COMPRESSING!");
		
		// Compress Image Data!
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream outZ = new GZIPOutputStream(out);
		outZ.write(abyte0);
		outZ.flush();
		outZ.finish();
		outZ.close();
		out.close();
		
		// Write length of the compressed image data
		output.writeInt(out.size());
		
		// Write image as COMPRESSED_INT_RGBA
		output.write(out.toByteArray());
		
		// Write End Sequence
		output.write('E');
		output.write('O');
		output.write('I');
		output.write('D');
		
		// Done!
		output.flush();
		output.close();
		DIALOG.close();
	}
	
	@Override
	public String getFileExtensionDescription()
	{
		return "ZBIN - Raw Compressed Binary Image Data Format";
	}
}