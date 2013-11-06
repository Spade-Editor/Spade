package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.math.SimplexNoise;

import java.awt.image.BufferedImage;

public class SimplexNoiseOp extends ImageOp
{
	public void operation()
	{
		// TODO: Add dialog with sliders to configure the noise!
		
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		SimplexNoise noise = new SimplexNoise(System.currentTimeMillis());
		
		double scale = 0.125;
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				int before = old.getRGB(i, j);
				int after = before;
				
				double valD = noise.noiseO(i * scale, j * scale, 8, 1 , 0.25);
				valD = valD * 0.5D + 0.5D;
				int valI = MathUtils.clamp((int)(valD * 256),255,0) & 0xFF;
				
				after &= 0xFF000000;
				
				after |= valI << 0;
				after |= valI << 8;
				after |= valI << 16;
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
	
}
