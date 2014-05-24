package heroesgrave.paint.image.change;

import heroesgrave.paint.io.Serialised;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RectChange extends IEditChange implements Serialised
{
	private short x1, y1, x2, y2;
	private int colour;
	
	public RectChange(short x1, short y1, short x2, short y2, int colour)
	{
		this.colour = colour;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void moveTo(short x, short y)
	{
		this.x2 = x;
		this.y2 = y;
	}
	
	@Override
	public void apply(BufferedImage image)
	{
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(colour, true));
		g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1),
				Math.abs(y2 - y1));
	}
	
	@Override
	public RectChange encode()
	{
		return this;
	}
	
	@Override
	public RectChange decode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeInt(colour);
		out.writeShort(x1);
		out.writeShort(y1);
		out.writeShort(x2);
		out.writeShort(y2);
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		colour = in.readInt();
		x1 = in.readShort();
		y1 = in.readShort();
		x2 = in.readShort();
		y2 = in.readShort();
	}
}
