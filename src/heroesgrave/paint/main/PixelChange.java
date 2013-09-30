package heroesgrave.paint.main;

import java.awt.image.BufferedImage;

public class PixelChange implements Change
{
	public int x, y, o, n;
	
	public PixelChange(int x, int y, int n)
	{
		this.x = x;
		this.y = y;
		this.n = n;
	}
	
	public BufferedImage apply(BufferedImage image)
	{
		o = image.getRGB(x, y);
		image.setRGB(x, y, n);
		return image;
	}

	public BufferedImage revert(BufferedImage image)
	{
		image.setRGB(x, y, o);
		return image;
	}
	
	public int getSize()
	{
		return 4;
	}
	
	public boolean samePos(int x, int y)
	{
		return (this.x == x && this.y == y);
	}
}
