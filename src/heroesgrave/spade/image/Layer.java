// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.image;

import heroesgrave.spade.image.blend.BlendMode;
import heroesgrave.spade.image.change.IChange;
import heroesgrave.spade.image.change.IMaskChange;
import heroesgrave.spade.main.Spade;
import heroesgrave.utils.misc.Metadata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class Layer extends DefaultMutableTreeNode
{
	private Document doc;
	private FreezeBuffer buffer;
	private Metadata info;
	private BlendMode blend;
	private boolean floating, visible;
	
	public Layer(Document doc, Metadata info)
	{
		this(doc, new RawImage(doc.getWidth(), doc.getHeight()), info);
	}
	
	public Layer(Document doc, RawImage image, Metadata info)
	{
		super(info.getOrSet("name", "New Layer"));
		this.doc = doc;
		this.buffer = new FreezeBuffer(image);
		this.info = info;
		this.blend = BlendMode.getBlendMode(info.getOrSet("blend", "Normal"));
	}
	
	public void updateMetadata()
	{
		BlendMode newMode = BlendMode.getBlendMode(info.get("blend"));
		if(newMode != blend)
		{
			blend = newMode;
			doc.repaint = true;
		}
		String newName = info.get("name");
		if(!newName.equals(this.getUserObject()))
		{
			this.setUserObject(newName);
			Spade.main.gui.layers.redrawTree();
		}
	}
	
	public Layer getParentLayer()
	{
		return (Layer) super.getParent();
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public void addLayer(Layer l)
	{
		super.add(l);
		doc.reconstructFlatmap();
		doc.changed(this);
	}
	
	public void addLayer(Layer l, int index)
	{
		super.insert(l, index);
		doc.reconstructFlatmap();
		doc.changed(this);
	}
	
	public int removeLayer(Layer l)
	{
		int index = super.getIndex(l);
		super.remove(l);
		doc.reconstructFlatmap();
		doc.changed(this);
		return index;
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
	
	public void render(Graphics2D g)
	{
		if(!visible)
			return;
		g.setComposite(blend);
		g.drawImage(this.buffer.getFront(), 0, 0, null);
	}
	
	public void constructFlatMap(ArrayList<Layer> flatmap)
	{
		flatmap.add(this);
		
		@SuppressWarnings("unchecked")
		Enumeration<Layer> children = this.children();
		while(children.hasMoreElements())
		{
			((Layer) children.nextElement()).constructFlatMap(flatmap);
		}
	}
	
	public void addChange(IChange change)
	{
		doc.getHistory().addChange(doc.getFlatMap().indexOf(this));
		buffer.addChange(change);
		doc.changed(this);
		if(change instanceof IMaskChange)
		{
			Spade.main.gui.canvas.maskChanged();
		}
	}
	
	public Document getDocument()
	{
		return doc;
	}
	
	public void revertChange()
	{
		if(buffer.revertChange() instanceof IMaskChange)
		{
			Spade.main.gui.canvas.maskChanged();
		}
		doc.changed(this);
	}
	
	public void repeatChange()
	{
		if(buffer.repeatChange() instanceof IMaskChange)
		{
			Spade.main.gui.canvas.maskChanged();
		}
		doc.changed(this);
	}
	
	public void addChangeSilent(IChange change)
	{
		buffer.addChange(change);
		doc.changed(this);
		if(change instanceof IMaskChange)
		{
			Spade.main.gui.canvas.maskChanged();
		}
	}
	
	public BlendMode getBlendMode()
	{
		return blend;
	}
	
	public RawImage getImage()
	{
		return buffer.getImage();
	}
	
	public BufferedImage getBufferedImage()
	{
		return buffer.getFront();
	}
	
	public Layer floating()
	{
		this.floating = true;
		return this;
	}
	
	public boolean isFloating()
	{
		return this.floating;
	}
}
