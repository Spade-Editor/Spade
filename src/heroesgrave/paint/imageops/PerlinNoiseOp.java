package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.math.Perlin;
import java.awt.image.BufferedImage;

@SuppressWarnings("deprecation")
public class PerlinNoiseOp extends ImageOp
{
	
	public void operation()
	{
		// TODO: Add dialog with sliders to configure the noise!
		
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Perlin noise = new Perlin(System.currentTimeMillis());
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				
				// TODO: Fix the 'stripe' issue... this Op cannot be used with this strange bug being in place.
				// The Perlin class is PROBABLY bugged!
				double valD = noise.noise3d_simplex4(i, 0, j);
				
				valD = valD * 0.5D + 0.5D;
				int valI = MathUtils.clamp((int)(valD * 256), 255, 0) & 0xFF;
				
				int after = 0xFF000000;
				after |= valI << 0;
				after |= valI << 8;
				after |= valI << 16;
				
				if(i == j){
					after = 0xFFFF00FF;
				}
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
	
}
