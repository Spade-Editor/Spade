package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;

public class FlipHoriz extends ImageOp
{
	public void operation()
	{
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				newImage.setRGB(i, j, old.getRGB(old.getWidth() - i - 1,j));
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
}
