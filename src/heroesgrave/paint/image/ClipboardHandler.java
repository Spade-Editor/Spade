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

package heroesgrave.paint.image;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipboardHandler
{
	public static BufferedImage paste()
	{
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		
		try
		{
			if(t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor))
			{
				return (BufferedImage) t.getTransferData(DataFlavor.imageFlavor);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void copy(BufferedImage image)
	{
		if(image == null)
		{
			throw new IllegalArgumentException("Image can't be null");
		}
		
		TransferableImage transferable = new TransferableImage(image);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
	}
	
	private static class TransferableImage implements Transferable
	{
		private BufferedImage image;
		
		public TransferableImage(BufferedImage image)
		{
			this.image = image;
		}
		
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			if(isDataFlavorSupported(flavor))
			{
				return image;
			}
			throw new UnsupportedFlavorException(flavor);
		}
		
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]{DataFlavor.imageFlavor};
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return flavor == DataFlavor.imageFlavor;
		}
	}
}