package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResizeCanvasChange extends ResizingChange
{
	private short width, height;
	
	public ResizeCanvasChange(int width, int height)
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
		int[] buffer = image.borrowBuffer();
		int[] newBuffer = newImage.borrowBuffer();
		int w = Math.min(width, image.width);
		int h = Math.min(height, image.height);
		for(int j = 0; j < h; j++)
		{
			System.arraycopy(buffer, j * image.width, newBuffer, j * width, w);
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
