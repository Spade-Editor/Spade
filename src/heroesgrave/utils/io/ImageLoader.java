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

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.UnsafeTroughReflectionHelper;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * 
 * NOTE/TODO: Rename this class into something more appropriate, because this is NOT just an ImageLoader!
 * 
 **/
public class ImageLoader
{
	private static final HashMap<String, ImageImporter> importers = new HashMap<String, ImageImporter>();
	
	static
	{
		/// initialize importers?
		
		// Nothing here yet! Feel free to expand by adding new image-importers!
		add(new ImageImporter(){
			@Override
			public BufferedImage read(File file) throws IOException {
				
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				
				int width = in.readInt();
				int height = in.readInt();
				int surfaceArea = width * height;
				int[] raw = new int[surfaceArea];
				
			    final JDialog dlg = new JDialog(Paint.main.gui.frame, "Progress Dialog", true);
			    JProgressBar dpb = new JProgressBar(0, surfaceArea);
			    dlg.add(BorderLayout.CENTER, dpb);
			    dlg.add(BorderLayout.NORTH, new JLabel("Loading..."));
			    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			    dlg.setSize(300, 75);
			    dlg.setLocationRelativeTo(Paint.main.gui.frame);
			    
			    Thread t = new Thread(new Runnable() {
			      public void run() {
			        dlg.setVisible(true);
			      }
			    });
			    t.start();
				
				for(int I = 0; I < surfaceArea; I++)
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
					
				    dpb.setValue(I);
				}
				
				// close progress dialog
				dlg.setVisible(false);
				
				// Read 'EOID'
				in.readInt();
				
				in.close();
				
				BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				
				image.setRGB(0, 0, width, height, raw, 0, width);
				
				return image;
			}

			@Override
			public String getFormat() {
				return "bin";
			}

			@Override
			public String getDescription() {
				return "BIN - Raw Binary Image Data Format";
			}
		});
		
	}
	
	public static void add(ImageImporter exporter) {
		importers.put(exporter.getFormat(), exporter);
	}
	
	private ImageLoader()
	{
		
	}
	
	public static BufferedImage loadImage(String path)
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
					return importer.read(file);
				}
				else
				// If there is NO custom importer, use the default ImageIO.read() method, and prey that it can read it!
				{
					return ImageIO.read(file);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
			throw new RuntimeException("The resource \"" + path + "\" was missing!");
		
		return null;
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
			
			// Throw the exception, so the use is notified that something went terribly wrong!
			UnsafeTroughReflectionHelper.throwException(e);
		}
	}

	public static void addAllImporters(JFileChooser chooser) {
		
		for(Entry<String, ImageImporter> importer : importers.entrySet())
		{
			chooser.addChoosableFileFilter(importer.getValue());
		}
		
	}
}