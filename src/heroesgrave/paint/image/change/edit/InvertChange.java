package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.change.IImageChange;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InvertChange extends IImageChange
{
	@Override
	public void write(DataOutputStream out) throws IOException
	{
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
	}
	
	@Override
	public BufferedImage apply(BufferedImage image)
	{
		// Directly accessing the pixels seems to be faster.
		int[] buffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		// int w = image.getWidth();
		// int h = image.getHeight();
		// int[] buffer = image.getRGB(0, 0, w, h, null, 0, w);
		
		for(int i = 0; i < buffer.length; i++)
		{
			buffer[i] ^= 0x00FFFFFF; // I hope this is correct.
		}
		
		// image.setRGB(0, 0, w, h, buffer, 0, w);
		
		// Not sure if this needs to be here for when using the DataBuffer.
		buffer = null;
		
		return image;
	}
}
