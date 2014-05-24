package heroesgrave.paint.image;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class RawImage
{
	private int[] buffer;
	public final int width, height;
	
	public RawImage(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.buffer = new int[width * height];
	}
	
	public void setPixel(int x, int y, int c)
	{
		buffer[index(x, y)] = c;
	}
	
	public int getPixel(int x, int y)
	{
		return buffer[index(x, y)];
	}
	
	private int index(int x, int y)
	{
		return y * width + x;
	}
	
	public int[] copyBuffer()
	{
		return Arrays.copyOf(buffer, buffer.length);
	}
	
	public int[] getBuffer()
	{
		return buffer;
	}
	
	public void setBuffer(int[] buffer)
	{
		this.buffer = buffer;
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage image =
				new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, width, height, buffer, 0, width);
		return image;
	}
}
