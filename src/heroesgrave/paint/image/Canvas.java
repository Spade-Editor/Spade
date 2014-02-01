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

import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas
{
	protected ArrayList<Canvas> layers = new ArrayList<Canvas>();
	protected BufferedImage image, temp;
	public String name;
	public BlendMode mode;
	protected History hist;
	
	public Canvas(String name, int width, int height)
	{
		this(name, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	}
	
	public Canvas(String name, BufferedImage image)
	{
		this.image = image;
		this.name = name;
		this.mode = BlendMode.NORMAL;
		hist = new History(image);
	}
	
	public void checkImage()
	{
		this.image = hist.getUpdatedImage();
		for(Canvas c : layers)
			c.checkImage();
	}
	
	public void addLayer(Canvas c)
	{
		if(!layers.contains(c))
			layers.add(c);
	}
	
	public void removeLayer(Canvas canvas)
	{
		layers.remove(canvas);
	}
	
	public void mergeLayer(Canvas canvas)
	{
		this.removeLayer(canvas);
		this.temp = hist.getUpdatedImage();
		canvas.draw(temp.createGraphics(), false);
		hist.addChange(new KeyFrame(this.temp));
	}
	
	public void unmergeLayer(Canvas canvas)
	{
		hist.revertChange();
		this.addLayer(canvas);
		this.image = hist.getUpdatedImage();
	}
	
	public int getRGB(int x, int y)
	{
		return image.getRGB(x, y);
	}
	
	public void addChange(IFrame frame)
	{
		hist.addChange(frame);
	}
	
	public int getWidth()
	{
		return image.getWidth();
	}
	
	public int getHeight()
	{
		return image.getHeight();
	}
	
	public void swap(Canvas a, Canvas b)
	{
		if(layers.contains(a) && layers.contains(b))
		{
			int al = layers.indexOf(a);
			int bl = layers.indexOf(b);
			layers.set(bl, null);
			layers.set(al, b);
			layers.set(bl, a);
			Paint.main.gui.canvas.getPanel().repaint();
		}
	}
	
	public BufferedImage getImage()
	{
		if(hist.wasChanged())
		{
			image = hist.getUpdatedImage();
		}
		return image;
	}
	
	public BufferedImage getFullImage()
	{
		BufferedImage image = hist.getUpdatedImage();
		Graphics2D g = image.createGraphics();
		g.setComposite(mode);
		if(!layers.isEmpty())
		{
			for(int i = layers.size() - 1; i >= 0; i--)
			{
				layers.get(i).draw(g, false);
			}
		}
		return image;
	}
	
	public void draw(Graphics2D g, boolean render)
	{
		if(hist.wasChanged())
		{
			this.image = hist.getUpdatedImage();
		}
		
		g.setComposite(mode);
		
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
		
		if(!layers.isEmpty())
		{
			for(int i = layers.size() - 1; i >= 0; i--)
			{
				layers.get(i).draw(g, render);
			}
		}
	}
	
	public History getHistory()
	{
		return hist;
	}
	
	public void clearHistory()
	{
		hist.clear();
		for(Canvas c : layers)
			c.clearHistory();
	}
	
	public boolean hasChildren()
	{
		return !layers.isEmpty();
	}
	
	public ArrayList<Canvas> getChildren()
	{
		return layers;
	}
	
	public void setBlendMode(BlendMode mode)
	{
		this.mode = mode;
	}
	
	public String toString()
	{
		return name;
	}
	
	public Canvas getParentOf(Canvas c)
	{
		if(layers.contains(c))
			return this;
		for(Canvas cc : layers)
		{
			Canvas p = cc.getParentOf(c);
			if(p != null)
				return p;
		}
		return null;
	}
}