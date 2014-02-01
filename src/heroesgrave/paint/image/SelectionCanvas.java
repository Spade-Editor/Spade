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

package heroesgrave.paint.image;

import heroesgrave.paint.main.Paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

public class SelectionCanvas extends Canvas
{
	public static enum CombineMode
	{
		ADD, SUBTRACT, INTERSECT, XOR, REPLACE
	}
	
	public static final Color mask_bg = new Color(32, 32, 32, 128);
	Shape clip;
	private int tx, ty, ftx, fty;
	
	public SelectionCanvas(BufferedImage image, Shape clip)
	{
		super("Selection", image);
		this.clip = clip;
	}
	
	public void setTranslation(int x, int y)
	{
		this.tx = ftx + x;
		this.ty = fty + y;
	}
	
	public BufferedImage getBoundedSelection()
	{
		this.image = getImage();
		Rectangle bounds = clip.getBounds();
		BufferedImage image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.translate(-bounds.x, -bounds.y);
		g.setClip(clip);
		g.drawImage(this.image, 0, 0, null);
		g.dispose();
		return image;
	}
	
	public void draw(Graphics2D g, boolean render)
	{
		if(render)
		{
			if(hist.wasChanged())
			{
				this.image = hist.getUpdatedImage();
				this.temp = hist.getUpdatedImage();
			}
			
			g.setComposite(mode);
			g.setColor(mask_bg);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.translate(tx, ty);
			g.setClip(clip);
			g.translate(-tx, -ty);
			if(this == Paint.main.gui.canvas.getCanvas() && Paint.main.gui.canvas.getPreview() != null)
			{
				IFrame prev = Paint.main.gui.canvas.getPreview();
				if(prev instanceof KeyFrame)
				{
					g.drawImage(this.image, 0, 0, null);
					g.drawImage(((KeyFrame) prev).getImage(), 0, 0, getWidth(), getHeight(), null);
				}
				else if(prev instanceof Frame)
				{
					temp = hist.getUpdatedImage();
					((Frame) prev).apply(this.temp);
					g.drawImage(this.temp, 0, 0, null);
				}
			}
			else
			{
				g.drawImage(this.image, 0, 0, null);
			}
		}
		else
		{
			if(hist.wasChanged())
			{
				this.image = hist.getUpdatedImage();
			}
			
			g.setComposite(mode);
			g.translate(tx, ty);
			g.setClip(clip);
			g.translate(-tx, -ty);
			if(this == Paint.main.gui.canvas.getCanvas() && Paint.main.gui.canvas.getPreview() != null)
			{
				IFrame prev = Paint.main.gui.canvas.getPreview();
				if(prev instanceof KeyFrame)
				{
					g.drawImage(this.image, 0, 0, null);
					g.drawImage(((KeyFrame) prev).getImage(), 0, 0, getWidth(), getHeight(), null);
				}
				else if(prev instanceof Frame)
				{
					temp = hist.getUpdatedImage();
					((Frame) prev).apply(this.temp);
					g.drawImage(this.temp, 0, 0, null);
				}
			}
			else
			{
				g.drawImage(this.image, 0, 0, null);
			}
		}
	}
	
	public void finalizeTranslation()
	{
		ftx = tx;
		fty = ty;
	}
}