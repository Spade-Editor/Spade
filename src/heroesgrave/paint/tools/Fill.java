/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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

import heroesgrave.paint.image.*;
import heroesgrave.paint.main.Paint;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Fill extends Tool
{
	private ArrayList<PixelChange> buffer = new ArrayList<PixelChange>();
	
	public Fill(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getCanvas().getWidth() || y >= Paint.main.gui.canvas.getCanvas().getHeight())
			return;
		
		Stack<Point> stack = new Stack<Point>();
		HashSet<Point> explored = new HashSet<Point>();
		
		Canvas canvas = Paint.main.gui.canvas.getCanvas();
		
		stack.push(new Point(x, y));
		final int c = canvas.getRGB(x, y);
		if((c == Paint.main.getLeftColour() && button == MouseEvent.BUTTON1) || (c == Paint.main.getRightColour() && button == MouseEvent.BUTTON3))
			return;
		
		final int colour;
		if(button == MouseEvent.BUTTON1)
		{
			colour = Paint.main.getLeftColour();
		}
		else if(button == MouseEvent.BUTTON3)
		{
			colour = Paint.main.getRightColour();
		}
		else
		{
			return;
		}
		
		Rectangle imageRect = new Rectangle(0, 0, Paint.main.gui.canvas.getWidth(), Paint.main.gui.canvas.getCanvas().getHeight());
		
		while(!stack.isEmpty())
		{
			Point p = stack.pop();
			
			if(canvas.getRGB(p.x, p.y) != c)
			{
				continue;
			}
			else
			{
				buffer.add(new PixelChange(p.x, p.y, colour));
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
		
		PixelChange[] fl = new PixelChange[buffer.size()];
		buffer.toArray(fl);
		buffer.clear();
		
		/* was  Paint.addChange(new MultiChange(fl));
		 * 
		 * now uses a keyframe to 'flush' the canvas history.
		 * Reason: eliminates latent painting lag due to drawing the
		 * potentially large MultiChange every canvas update for the
		 * next MAX_FRAMES brush strokes.
		*/
		BufferedImage image = Paint.main.gui.canvas.getCanvas().getHistory().getUpdatedImage();
		new MultiChange(fl).apply(image);
		Paint.addChange(new KeyFrame(image));
	}
	
	public void onReleased(int x, int y, int button)
	{
		
	}
	
	public void whilePressed(int x, int y, int button)
	{
		
	}
}