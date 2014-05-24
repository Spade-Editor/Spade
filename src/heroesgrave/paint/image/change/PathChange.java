package heroesgrave.paint.image.change;

import heroesgrave.paint.io.Serialised;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PathChange extends IEditChange
{
	private ArrayList<Point> points = new ArrayList<Point>();
	private int colour;
	
	public PathChange(short x, short y, int colour)
	{
		this(new Point(x, y), colour);
	}
	
	public PathChange(Point p, int colour)
	{
		this.colour = colour;
		points.add(p);
	}
	
	public void moveTo(short x, short y)
	{
		this.moveTo(new Point(x, y));
	}
	
	public void moveTo(Point p)
	{
		points.add(p);
	}
	
	@Override
	public SerialPathChange encode()
	{
		short[] data = new short[points.size() * 2];
		int i = 0;
		for(Point p : points)
		{
			data[i++] = (short) p.x;
			data[i++] = (short) p.y;
		}
		return new SerialPathChange(data, colour);
	}
	
	@Override
	public void apply(BufferedImage image)
	{
		Point p1 = points.get(0);
		Point p2;
		
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(colour, true));
		
		for(int i = 1; i < points.size(); i++)
		{
			p2 = points.get(i);
			
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
			
			p1 = p2;
		}
	}
	
	public static class SerialPathChange implements Serialised
	{
		private short[] points;
		private int colour;
		
		public SerialPathChange()
		{
			
		}
		
		public SerialPathChange(short[] data, int colour)
		{
			this.colour = colour;
			this.points = data;
		}
		
		@Override
		public PathChange decode()
		{
			PathChange change = new PathChange(points[0], points[1], colour);
			for(int i = 2; i < points.length; i += 2)
				change.moveTo(points[i], points[i + 1]);
			return change;
		}
		
		@Override
		public void write(DataOutputStream out) throws IOException
		{
			out.writeInt(colour);
			out.writeInt(points.length);
			for(short s : points)
				out.writeShort(s);
		}
		
		@Override
		public void read(DataInputStream in) throws IOException
		{
			colour = in.readInt();
			points = new short[in.readInt()];
			for(int i = 0; i < points.length; i++)
				points[i] = in.readShort();
		}
	}
}
