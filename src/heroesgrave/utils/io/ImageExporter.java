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


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

public abstract class ImageExporter extends FileFilter {
	public static ArrayList<ImageExporter> exporters = new ArrayList<ImageExporter>();
	
	static
	{
		exporters.add(new ImageExporter()
		{
			@Override public String getFileExtension() {
				return "png";
			}
			
			@Override public void exportImage(BufferedImage image, File destination) {
				ImageLoader.writeImage(image, getFileExtension().toUpperCase(), destination.getAbsolutePath());
			}

			@Override
			public String getFileExtensionDescription() {
				return "PNG - Portable Network Graphics Image";
			}
		});
		
		exporters.add(new ImageExporter()
		{
			@Override public String getFileExtension() {
				return "bmp";
			}
			
			@Override public void exportImage(BufferedImage image, File destination) {
				ImageLoader.writeImage(image, getFileExtension().toUpperCase(), destination.getAbsolutePath());
			}

			@Override
			public String getFileExtensionDescription() {
				return "BMP - Microsoft Bitmap Image";
			}
		});
		
		exporters.add(new ImageExporter()
		{
			@Override public String getFileExtension() {
				return "gif";
			}
			
			@Override public void exportImage(BufferedImage image, File destination) {
				ImageLoader.writeImage(image, getFileExtension().toUpperCase(), destination.getAbsolutePath());
			}

			@Override
			public String getFileExtensionDescription() {
				return "GIF - Graphics Interchange Format";
			}
		});
		
		exporters.add(new ImageExporter()
		{
			@Override public String getFileExtension() {
				return "jpeg";
			}
			
			@Override public void exportImage(BufferedImage imageIn, File destination) {
				// 'Color Corruption' Fix
				BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB); 
				Graphics g = imageOut.getGraphics();
				g.drawImage(imageIn, 0, 0, null);
				
				ImageLoader.writeImage(imageOut, getFileExtension().toUpperCase(), destination.getAbsolutePath());
			}

			@Override
			public String getFileExtensionDescription() {
				return "JPEG - JPEG File Interchange Format";
			}
		});
		
		exporters.add(new ImageExporter()
		{
			/**
			 * BIN Image Exporter.
			 * 
			 * Format is as follows:
			 * signed_int32 width
			 * signed_int32 height
			 * INT_RGBA[width*height] imageData
			 **/
			
			@Override public String getFileExtension() {
				return "bin";
			}
			
			@Override public void exportImage(BufferedImage bufferedImage, File destination) throws IOException {
				DataOutputStream output = new DataOutputStream(new FileOutputStream(destination));
				
				// Get width and height.
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getHeight();
				
				// Write width and height as int32 (Signed 32-Bit Integer).
				output.writeInt(width);
				output.writeInt(height);
				
				// Buffer the Image-Data
				int ai[] = new int[width * height];
				byte abyte0[] = new byte[width * height * 4];
				bufferedImage.getRGB(0, 0, width, height, ai, 0, width);
				
				// Go trough ALL the pixels and convert from INT_ARGB to INT_RGBA
				for (int k = 0; k < ai.length; k++)
				{
					int A = (ai[k] >> 24) & 0xff;
					int R = (ai[k] >> 16) & 0xff;
					int G = (ai[k] >> 8) & 0xff;
					int B = ai[k] & 0xff;
					
					abyte0[(k * 4) + 0] = (byte) R;//R
					abyte0[(k * 4) + 1] = (byte) G;//G
					abyte0[(k * 4) + 2] = (byte) B;//B
					abyte0[(k * 4) + 3] = (byte) A;//A
				}
				
				// Write image as INT_RGBA
				output.write(abyte0);
				
				// Write End Sequence
				output.write('E');
				output.write('O');
				output.write('I');
				output.write('D');
				
				// Done!
				output.close();
			}
			
			@Override
			public String getFileExtensionDescription() {
				return "BIN - Raw Binary Image Data Format";
			}
		});
		
		exporters.add(new ImageExporter()
		{
			@Override public String getFileExtension() {
				return "tga";
			}
			
			@Override public void exportImage(BufferedImage image, File destination) throws IOException {
				DataOutputStream out = new DataOutputStream(new FileOutputStream(destination));
				boolean writeAlpha = image.getTransparency() != BufferedImage.OPAQUE;
				
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
				out.writeShort(flipEndian((short) image.getWidth()));
				out.writeShort(flipEndian((short) image.getHeight()));
				
				if (writeAlpha)
				{
					out.writeByte((byte) 32);
					// Image Descriptor (can't be 0 since we're using 32-bit TGAs)
					// needs to not have 0x20 set to indicate it's not a flipped image
					out.writeByte((byte) 1);
				} else {
					out.writeByte((byte) 24);
					// Image Descriptor (must be 0 since we're using 24-bit TGAs)
					// needs to not have 0x20 set to indicate it's not a flipped image
					out.writeByte((byte) 0);
				}
				
				// Write out the image data
				Color c;
				
				for (int y = 0; y < image.getHeight(); y++)
				{
					for (int x = 0; x < image.getWidth(); x++)
					{
						c = new Color(image.getRGB(x, y));
						
						out.writeByte((byte) (c.getBlue()));
						out.writeByte((byte) (c.getGreen()));
						out.writeByte((byte) (c.getRed()));
						
						if (writeAlpha) {
							out.writeByte((byte) (c.getAlpha()));
						}
					}
				}
				
				out.close();
			}
			
			private short flipEndian(short signedShort) {
				int input = signedShort & 0xFFFF;
				return (short) (input << 8 | (input & 0xFF00) >>> 8);
			}
			
			@Override
			public String getFileExtensionDescription() {
				return "TGA - Tagged Image File Format";
			}
		});
		
	}
	
	/**
	 * Returns the file extension this ImageExporter exports to.<br>
	 * (Lowercase, without the dot!)
	 **/
	public abstract String getFileExtension();
	
	public abstract String getFileExtensionDescription();
	
	public abstract void exportImage(BufferedImage image, File destination) throws IOException ;
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		if(f.getAbsolutePath().endsWith("." + getFileExtension()))
			return true;
		
		return false;
	}
	
	@Override
	public String getDescription() {
		return getFileExtensionDescription();
	}

	public static ImageExporter get(String extension) {
		
		for(ImageExporter exporter : exporters){
			if(exporter.getFileExtension().equalsIgnoreCase(extension)){
				return exporter;
			}
		}
		
		throw new RuntimeException("Image Exporter for the given Format '"+extension+"' could not be found!");
	}

	public static void add(ImageExporter exporter) {
		if(exporter == null)
			throw new IllegalArgumentException("Input cannot be null!");
		
		exporters.add(exporter);
	}

}