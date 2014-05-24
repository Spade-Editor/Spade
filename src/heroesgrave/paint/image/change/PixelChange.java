package heroesgrave.paint.image.change;

import heroesgrave.paint.io.Serialised;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Yes, we can implement both an extension of IChange and Serialised.
 * This should be used when the change type stores no extra information.
 */
public class PixelChange extends IRevEditChange implements Serialised
{
	private short x, y;
	private int c, o;
	
	public PixelChange()
	{
		
	}
	
	public PixelChange(short x, short y, int c)
	{
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	@Override
	public void apply(BufferedImage image)
	{
		o = image.getRGB(x, y);
		image.setRGB(x, y, c);
	}
	
	@Override
	public void revert(BufferedImage image)
	{
		image.setRGB(x, y, o);
	}
	
	@Override
	public PixelChange decode()
	{
		return this;
	}
	
	@Override
	public PixelChange encode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeShort(x);
		out.writeShort(y);
		out.writeInt(c);
		out.writeInt(o);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		x = in.readShort();
		y = in.readShort();
		c = in.readInt();
		o = in.readInt();
	}
}
