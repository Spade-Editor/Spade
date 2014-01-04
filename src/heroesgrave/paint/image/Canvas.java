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

package heroesgrave.paint.image;

import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas
{
	private ArrayList<Canvas> layers = new ArrayList<Canvas>();
	private BufferedImage image;
	public String name;
	public BlendMode mode;
	private History hist;
	
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
	
	public void changeImage(BufferedImage image)
	{
		this.image = image;
		hist.addChange(new KeyFrame(image));
	}
	
	public void addLayer(Canvas c)
	{
		layers.add(c);
	}
	
	public void removeLayer(Canvas canvas)
	{
		layers.remove(canvas);
	}
	
	public int getRGB(int x, int y)
	{
		return image.getRGB(x, y);
	}
	
	public void addChange(IFrame frame)
	{
		hist.addChange(frame);
	}
	
	public void fullChange(KeyFrame frame)
	{
		hist.addChange(frame);
		for(Canvas c : layers)
			c.fullChange(frame);
	}
	
	public void revertChange()
	{
		hist.revertChange();
	}
	
	public void repeatChange()
	{
		hist.repeatChange();
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
		return image;
	}
	
	public BufferedImage getFullImage()
	{
		BufferedImage image = hist.getUpdatedImage();
		Graphics2D g = image.createGraphics();
		g.setComposite(mode);
		g.drawImage(this.image, 0, 0, null);
		if(!layers.isEmpty())
		{
			for(int i = layers.size() - 1; i >= 0; i--)
			{
				layers.get(i).draw(g);
			}
		}
		return image;
	}
	
	public void draw(Graphics2D g)
	{
		if(hist.wasChanged())
		{
			this.image = hist.getUpdatedImage();
		}
		
		g.setComposite(mode);
		g.drawImage(this.image, 0, 0, null);
		
		if(!layers.isEmpty())
		{
			for(int i = layers.size() - 1; i >= 0; i--)
			{
				layers.get(i).draw(g);
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
}