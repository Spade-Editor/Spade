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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExporterLBIN extends ImageExporter
{
	public String getFileExtension()
	{
		return "lbin";
	}
	
	public String getFileExtensionDescription()
	{
		return "LBIN - Raw Layered Binary Image Data Format";
	}
	
	public void export(Canvas canvas, File destination) throws IOException
	{
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		
		// Write Dimensions
		output.writeInt(canvas.getWidth());
		output.writeInt(canvas.getHeight());
		
		// Recursively write the layers.
		writeLayer(canvas, output);
		
		// XXX: Why do we need an end sequence?
		output.write('E');
		output.write('O');
		output.write('I');
		output.write('D');
		
		output.flush();
		output.close();
	}
	
	public void writeLayer(Canvas canvas, DataOutputStream out) throws IOException
	{
		int[] buf = new int[canvas.getWidth() * canvas.getHeight()];
		byte[] abyte0 = new byte[canvas.getWidth() * canvas.getHeight() * 4];
		
		canvas.getImage().getRGB(0, 0, canvas.getWidth(), canvas.getHeight(), buf, 0, canvas.getWidth());
		
		// Go through ALL the pixels and convert from INT_ARGB to INT_RGBA
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
		}
		
		// Write image as INT_RGBA
		out.write(abyte0);
		
		ArrayList<Canvas> children = canvas.getChildren();
		
		// Write the child count
		out.writeInt(children.size());
		
		// Recursively write the child layers.
		for(int i = 0; i < children.size(); i++)
		{
			writeLayer(children.get(i), out);
		}
	}
}