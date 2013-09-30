package heroesgrave.paint.tools;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Stack;

public class Fill extends Tool
{
	public Fill(String name)
	{
		super(name);
	}

	public void onPressed(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;

		Stack<Point> stack = new Stack<Point>();
		HashSet<Point> explored = new HashSet<Point>();
		
		stack.push(new Point(x, y));
		final int c = getColour(x, y);
		if(c == Paint.main.getColour())
			return;
		
		Rectangle imageRect = new Rectangle(0, 0, Paint.main.gui.canvas.getImage().getWidth(), Paint.main.gui.canvas.getImage().getHeight());

		while(!stack.isEmpty())
		{
			Point p = stack.pop();
			
			if(getColour(p.x, p.y) != c)
			{
				continue;
			}
			else
			{
				Paint.main.gui.canvas.bufferChange(new PixelChange(p.x, p.y, Paint.main.getColour()));
			}

			Point neighbour = new Point(p.x+1, p.y);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
				stack.push(neighbour);
			neighbour = new Point(p.x-1, p.y);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
				stack.push(neighbour);
			neighbour = new Point(p.x, p.y+1);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
				stack.push(neighbour);
			neighbour = new Point(p.x, p.y-1);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
				stack.push(neighbour);
		}

		Paint.main.gui.canvas.flushChanges();
	}

	private int getColour(int x, int y)
	{
		return Paint.main.gui.canvas.getImage().getRGB(x, y);
	}

	public void onReleased(int x, int y)
	{

	}

	public void whilePressed(int x, int y)
	{

	}

	public void whileReleased(int x, int y)
	{

	}
}
