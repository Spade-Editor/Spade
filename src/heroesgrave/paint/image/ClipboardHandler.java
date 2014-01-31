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
	
	public static void write(BufferedImage image)
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
