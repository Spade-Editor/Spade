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

import heroesgrave.paint.gui.Renderer;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.utils.misc.Metadata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class Layer extends DefaultMutableTreeNode
{
	private Document doc;
	private BufferedImage frozen; // All above layers frozen together.
	private FreezeBuffer buffer;
	private Metadata info;
	private boolean changed = true; // If this layer needs to be redrawn.
	private ArrayList<Layer> children = new ArrayList<Layer>();
	private BlendMode blend;
	private Layer parent;
	
	public Layer(Document doc, RawImage image, Metadata info)
	{
		this(doc, image.toBufferedImage(), info);
	}
	
	public Layer(Document doc, BufferedImage image, Metadata info)
	{
		super(info.getOrSet("name", "New Layer"));
		this.doc = doc;
		this.buffer = new FreezeBuffer(this, image);
		this.frozen = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.info = info;
		this.blend = BlendMode.NORMAL;
	}
	
	public Layer setParent(Layer parent)
	{
		this.parent = parent;
		return this;
	}
	
	public Layer getParent()
	{
		return parent;
	}
	
	public void addLayer(Layer l)
	{
		children.add(l);
		this.changed = true;
	}
	
	public void removeLayer(Layer l)
	{
		children.remove(l);
		this.changed = true;
	}
	
	public int getWidth()
	{
		return doc.getWidth();
	}
	
	public int getHeight()
	{
		return doc.getHeight();
	}
	
	public Metadata getMetadata()
	{
		return info;
	}
	
	public boolean childChanged()
	{
		for(Layer l : children)
		{
			if(l.refresh())
				return true;
		}
		return false;
	}
	
	private boolean refresh()
	{
		return this.isChanged() || this.childChanged();
	}
	
	public void render(Graphics2D g)
	{
		if(this.refresh())
		{
			Graphics2D cg = frozen.createGraphics();
			cg.setBackground(Renderer.TRANSPARENT);
			cg.clearRect(0, 0, getWidth(), getHeight());
			cg.drawImage(this.buffer.getFront(), 0, 0, null);
			for(Layer l : children)
			{
				l.render(cg);
			}
			cg.dispose();
			this.changed = false;
		}
		
		g.setComposite(blend);
		g.drawImage(this.frozen, 0, 0, null);
	}
	
	public void constructFlatMap(ArrayList<Layer> flatmap)
	{
		for(Layer layer : children)
		{
			layer.constructFlatMap(flatmap);
		}
		flatmap.add(this);
	}
	
	public boolean isChanged()
	{
		return changed;
	}
	
	public void addChange(IEditChange change)
	{
		doc.getHistory().addChange(doc.getFlatMap().indexOf(this));
		buffer.addChange(change);
		this.changed = true;
	}
	
	public Document getDocument()
	{
		return doc;
	}
	
	public void revertChange()
	{
		buffer.revertChange();
		this.changed = true;
	}
	
	public void repeatChange()
	{
		buffer.repeatChange();
		this.changed = true;
	}
}
