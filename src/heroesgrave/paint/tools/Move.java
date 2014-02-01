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

import heroesgrave.paint.image.CanvasManager.CanvasRenderer;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.image.SelectionCanvas;
import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Move extends Tool
{
	private int sx, sy;
	private BufferedImage image, origin;
	private boolean hovering;
	
	public Move(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		if(hovering)
		{
			hovering = false;
		}
		else
		{
			sx = x;
			sy = y;
			origin = Paint.main.gui.canvas.getCanvas().getImage();
			image = new BufferedImage(origin.getWidth(), origin.getHeight(), BufferedImage.TYPE_INT_ARGB);
			move(0, 0, image, origin);
			hovering = true;
			Paint.main.gui.canvas.getCanvas().addChange(new KeyFrame(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB)));
		}
	}
	
	public void onReleased(int x, int y, int button)
	{
		if(!hovering)
		{
			Paint.main.gui.canvas.getCanvas().getHistory().revertChange();
			move(x - sx, y - sy, image, origin);
			Paint.main.gui.canvas.applyPreview();
			image = null;
			origin = null;
			SelectionCanvas c = Paint.main.gui.canvas.selection.getSelection();
			if(c == Paint.main.gui.canvas.getCanvas())
			{
				c.finalizeTranslation();
			}
		}
	}
	
	public void whilePressed(int x, int y, int button)
	{
		move(x - sx, y - sy, image, origin);
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public void whileReleased(int x, int y, int button)
	{
		if(hovering)
		{
			move(x - sx, y - sy, image, origin);
			Paint.main.gui.canvas.getPanel().repaint();
		}
	}
	
	public static void move(int x, int y, BufferedImage image, BufferedImage origin)
	{
		Graphics2D g = image.createGraphics();
		g.setBackground(CanvasRenderer.TRANSPARENT);
		g.clearRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(origin, x, y, null);
		
		Paint.main.gui.canvas.preview(new KeyFrame(image));
		SelectionCanvas c = Paint.main.gui.canvas.selection.getSelection();
		if(c == Paint.main.gui.canvas.getCanvas())
		{
			c.setTranslation(x, y);
		}
	}
	
	public static void do_move(int x, int y)
	{
		BufferedImage image = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		newImage.createGraphics().drawImage(image, x, y, null);
		
		Paint.addChange(new KeyFrame(newImage));
	}
}