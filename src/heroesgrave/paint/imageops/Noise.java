package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Noise extends ImageOp
{
	public void operation()
	{
		// TODO: Implement a GUI to make it possible to modify the 'chance' value.
		
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Random random = new Random();
		int chance = 100;
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				int before = old.getRGB(i, j);
				int after = before;
				
				int rand = random.nextInt(256) & 0xFF;
				
				if(random.nextFloat() * 100 > chance)
				{
					after &= 0xFF000000;
					
					after |= rand << 0;
					after |= rand << 8;
					after |= rand << 16;
				}
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
}
