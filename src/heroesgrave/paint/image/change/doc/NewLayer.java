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

package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IDocChange;
import heroesgrave.paint.image.change.edit.ClearMaskChange;
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
