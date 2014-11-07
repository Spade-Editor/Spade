package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResizeImageChange extends ResizingChange
{
	private short width, height;
	
	public ResizeImageChange(int width, int height)
	{
		this.width = (short) width;
		this.height = (short) height;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeShort(width);
		out.writeShort(height);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		width = in.readShort();
		height = in.readShort();
	}
	
	@Override
	public RawImage apply(RawImage image)
	{
		RawImage newImage = new RawImage(width, height);
		for(int j = 0; j < height; j++)
		{
			for(int i = 0; i < width; i++)
			{
				newImage.setPixel(i, j, image.getPixel((i * image.width) / width, (j * image.height) / height));
			}
		}
		return newImage;
	}
	
	@Override
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
}
