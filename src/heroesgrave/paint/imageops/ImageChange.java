package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Change;

import java.awt.image.BufferedImage;

public class ImageChange implements Change
{
	private BufferedImage newImage, oldImage;
	
	public ImageChange(BufferedImage newImage)
	{
		this.newImage = newImage;
	}
	
	public BufferedImage apply(BufferedImage image)
	{
		oldImage = image;
		
		return newImage;
	}
	
	public BufferedImage revert(BufferedImage image)
	{
		return oldImage;
	}

	public int getSize()
	{
		return oldImage.getWidth()*oldImage.getHeight();
	}

	public boolean samePos(int x, int y)
	{
		return false;
	}
}
