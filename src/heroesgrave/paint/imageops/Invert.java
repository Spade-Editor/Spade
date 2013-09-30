package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;

public class Invert extends ImageOp
{
	public void operation()
	{
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				int c = old.getRGB(i, j) & 0x00FFFFFF;
				int a = c & 0xFF000000;
				c = ~c;
				newImage.setRGB(i, j, (c | a));
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
}
