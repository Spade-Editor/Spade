// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
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

package heroesgrave.spade.image.change.doc;

import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.change.IDocChange;
import heroesgrave.spade.image.change.edit.ClearMaskChange;
import heroesgrave.spade.main.Spade;
import heroesgrave.utils.misc.Metadata;

public class NewLayer implements IDocChange
{
	private Layer layer, parent;
	private int index = -1;
	private RawImage image;
	private String name;
	private boolean floating;
	
	public NewLayer(Layer parent)
	{
		this(parent, prepareImage(parent));
	}
	
	private static RawImage prepareImage(Layer parent)
	{
		RawImage image = new RawImage(parent.getWidth(), parent.getHeight());
		image.copyMaskFrom(parent.getImage());
		return image;
	}
	
	public NewLayer(Layer parent, RawImage image)
	{
		this(parent, image, "New Layer");
	}
	
	public NewLayer(Layer parent, RawImage image, String name)
	{
		this.parent = parent;
		this.image = image;
		this.name = name;
	}
	
	public NewLayer floating()
	{
		this.floating = true;
		return this;
	}
	
	public void apply(Document doc)
	{
		parent.addChangeSilent(new ClearMaskChange());
		
		Metadata info = new Metadata();
		info.set("name", name);
		
		if(floating)
			layer = new Layer(doc, image, info).floating();
		else
			layer = new Layer(doc, image, info);
		
		Spade.main.gui.layers.setVisible(layer);
		
		parent.addLayer(layer);
		doc.setCurrent(layer);
	}
	
	public void revert(Document doc)
	{
		index = parent.removeLayer(layer);
		doc.setCurrent(parent);
		
		parent.revertChange();
	}
	
	public void repeat(Document doc)
	{
		parent.repeatChange();
		
		parent.addLayer(layer, index);
		doc.setCurrent(layer);
	}
}
