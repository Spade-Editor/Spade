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

package heroesgrave.paint.gui;

import heroesgrave.paint.image.RawImage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipboardHandler
{
	public static void setImage(RawImage image)
	{
		BufferedImage bufferedImage;
		if(image.isMaskEnabled())
		{
			// TODO Optimise
			boolean[] mask = image.borrowMask();
			int left = image.width;
			int right = -1;
			int top = image.height;
			int bottom = -1;
			for(int j = 0; j < image.height; j++)
			{
				for(int i = 0; i < image.width; i++)
				{
					if(mask[j * image.width + i])
					{
						left = Math.min(left, i);
						right = Math.max(right, i);
						top = Math.min(top, j);
						bottom = Math.max(bottom, j);
					}
				}
			}
			
			int width = right - left + 1;
			int height = bottom - top + 1;
			
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			RawImage buffer = RawImage.unwrapBufferedImage(bufferedImage);
			image = RawImage.copyOf(image);
			image.move(-left, -top);
			for(int j = 0; j < height; j++)
			{
				for(int i = 0; i < width; i++)
				{
					buffer.setPixel(i, j, image.getPixel(i, j));
				}
			}
		}
		else
		{
			bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);
			bufferedImage.setRGB(0, 0, image.width, image.height, image.copyBuffer(), 0, image.width);
		}
		
		TransferableImage transfer = new TransferableImage(bufferedImage);
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(transfer, null);
	}
	
	public static RawImage getImage()
	{
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transfer = clip.getContents(null);
		if(transfer != null && transfer.isDataFlavorSupported(DataFlavor.imageFlavor))
		{
			try
			{
				Image image = (Image) transfer.getTransferData(DataFlavor.imageFlavor);
				BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g = bufferedImage.createGraphics();
				g.drawImage(image, 0, 0, null);
				g.dispose();
				
				return RawImage.unwrapBufferedImage(bufferedImage);
			}
			catch(UnsupportedFlavorException | IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static class TransferableImage implements Transferable
	{
		Image image;
		
		public TransferableImage(Image image)
		{
			this.image = image;
		}
		
		public Object getTransferData(DataFlavor flavour) throws UnsupportedFlavorException, IOException
		{
			if(flavour.equals(DataFlavor.imageFlavor) && image != null)
			{
				return image;
			}
			else
			{
				throw new UnsupportedFlavorException(flavour);
			}
		}
		
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]{DataFlavor.imageFlavor};
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavour)
		{
			for(DataFlavor f : getTransferDataFlavors())
			{
				if(flavour.equals(f))
					return true;
			}
			return false;
		}
	}
}
