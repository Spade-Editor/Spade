/*
 *	Copyright 2013 HeroesGrave
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

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

			Point neighbour = new Point(p.x + 1, p.y);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
			{
				stack.push(neighbour);
			}
			neighbour = new Point(p.x - 1, p.y);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
			{
				stack.push(neighbour);
			}
			neighbour = new Point(p.x, p.y + 1);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
			{
				stack.push(neighbour);
			}
			neighbour = new Point(p.x, p.y - 1);
			if(imageRect.contains(neighbour) && explored.add(neighbour))
			{
				stack.push(neighbour);
			}
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