// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.image;

import heroesgrave.paint.gui.PaintCanvas;
import heroesgrave.paint.image.change.DocumentChange;
import heroesgrave.paint.io.ImageExporter;
import heroesgrave.paint.io.ImageImporter;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.Metadata;
import heroesgrave.utils.misc.RandomUtils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Document
{
	private LinkedList<DocumentChange> changes = new LinkedList<DocumentChange>();
	private LinkedList<DocumentChange> reverted = new LinkedList<DocumentChange>();
	
	private int width, height;
	private File file;
	private Metadata info;
	private Layer root, current;
	private History history;
	
	private BufferedImage frozen;
	private int frozenTo, lowestChange;
	
	public boolean saved, repaint;
	
	private ArrayList<Layer> flatmap = new ArrayList<Layer>();
	
	public Document(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.frozen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.info = new Metadata();
		this.history = new History(this);
		
		RawImage image = new RawImage(width, height);
		//* Fill the image with gradient + noise.
		for(int i = 0; i < 512; i++)
		{
			for(int j = 0; j < 512; j++)
			{
				int color = 0xff000000;
				color |= (i / 2) << 0;
				color |= (j / 2) << 8;
				color |= RandomUtils.rInt(0xff) << 16;
				image.setPixel(i, j, color);
			}
		}
		/**/
		this.current = this.root = new Layer(this, image, new Metadata());
		this.flatmap.clear();
		root.constructFlatMap(flatmap);
	}
	
	public Document(File f)
	{
		this.info = new Metadata();
		this.history = new History(this);
		this.file = f;
		ImageImporter.loadImage(file.getAbsolutePath(), this);
		this.flatmap.clear();
		root.constructFlatMap(flatmap);
	}
	
	public void reconstructFlatmap()
	{
		Paint.main.gui.layers.redrawTree();
		this.flatmap.clear();
		root.constructFlatMap(flatmap);
	}
	
	public History getHistory()
	{
		return history;
	}
	
	public Layer getRoot()
	{
		return root;
	}
	
	public Layer getCurrent()
	{
		return current;
	}
	
	public void setRoot(Layer root)
	{
		this.current = this.root = root;
		this.reconstructFlatmap();
	}
	
	public void setCurrent(Layer current)
	{
		Paint.main.gui.layers.select(current);
		this.current = current;
	}
	
	public void selected(Layer current)
	{
		this.current = current;
	}
	
	public Metadata getMetadata()
	{
		return info;
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void save()
	{
		final String fileName = this.file.getAbsolutePath();
		
		String extension = "";
		
		int i = fileName.lastIndexOf('.');
		
		if(i > 0)
		{
			extension = fileName.substring(i + 1);
		}
		
		final ImageExporter exporter = ImageExporter.get(extension);
		
		System.out.println("Extension: \"" + extension + "\" Exporter: " + exporter);
		
		final Document doc = this;
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					exporter.export(doc, new File(fileName));
				}
				catch(IOException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred while saving the Image:\n" + e.getLocalizedMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}).start();
	}
	
	public String getDir()
	{
		if(file != null)
			return this.file.getParent();
		else
			return System.getProperty("user.dir");
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public ArrayList<Layer> getFlatMap()
	{
		return flatmap;
	}
	
	/*
	 * This should only be called when loading an image. I need to find another way to do this.
	 */
	public void setDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.frozen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage getRenderedImage()
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		{
			Graphics2D g = image.createGraphics();
			for(Layer l : flatmap)
			{
				l.render(g);
			}
			g.dispose();
		}
		return image;
	}
	
	// BufferedImage:
	// 9 Layers: ~10ms
	
	public void render(Graphics2D g)
	{
		long start = System.nanoTime();
		int index = flatmap.indexOf(current);
		if(Math.min(index, lowestChange) < frozenTo)
		{
			lowestChange = frozenTo = index;
			Graphics2D fg = frozen.createGraphics();
			fg.setBackground(PaintCanvas.TRANSPARENT);
			fg.clearRect(0, 0, width, height);
			for(int i = 0; i < frozenTo; i++)
			{
				flatmap.get(i).render(fg);
			}
		}
		else if(index > frozenTo)
		{
			Graphics2D fg = frozen.createGraphics();
			for(int i = frozenTo; i < index; i++)
			{
				flatmap.get(i).render(fg);
			}
			lowestChange = frozenTo = index;
		}
		g.drawImage(frozen, 0, 0, null);
		for(int i = frozenTo; i < flatmap.size(); i++)
		{
			flatmap.get(i).render(g);
		}
		long end = System.nanoTime();
		System.out.println("Rendering took: " + (end - start) / 1000000 + "ms");
	}
	
	public void changed(Layer layer)
	{
		lowestChange = Math.min(lowestChange, flatmap.indexOf(layer));
		repaint = true;
	}
	
	public void allChanged()
	{
		lowestChange = -1;
		repaint = true;
	}
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void addChange(DocumentChange change)
	{
		history.addChange(-1);
		changes.push(change);
		change.apply(this);
		this.allChanged();
	}
	
	public void revertChange()
	{
		if(changes.isEmpty())
		{
			return;
		}
		DocumentChange change = changes.pop();
		reverted.push(change);
		change.revert(this);
		this.allChanged();
	}
	
	public void repeatChange()
	{
		if(reverted.isEmpty())
		{
			return;
		}
		DocumentChange change = reverted.pop();
		changes.push(change);
		change.apply(this);
		this.allChanged();
	}
}
