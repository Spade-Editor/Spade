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
import heroesgrave.utils.misc.Metadata;

public class AnchorLayer implements IDocChange
{
	private Layer layer, old, parent;
	private int index = -1;
	private RawImage image;
	private String name;
	
	public AnchorLayer(Layer layer)
	{
		this(layer, prepareImage(layer));
	}
	
	private static RawImage prepareImage(Layer layer)
	{
		RawImage image = new RawImage(layer.getWidth(), layer.getHeight());
		image.copyFrom(layer.getImage(), true);
		return image;
	}
	
	public AnchorLayer(Layer layer, RawImage image)
	{
		this.old = layer;
		this.parent = layer.getParentLayer();
		this.image = image;
		this.name = layer.getMetadata().get("name");
		if(this.name.equals("Floating Layer"))
			this.name = "New Layer";
	}
	
	public void apply(Document doc)
	{
		Metadata info = new Metadata();
		info.set("name", name);
		
		layer = new Layer(doc, image, info);
		index = parent.removeLayer(old);
		parent.addLayer(layer, index);
		doc.setCurrent(layer);
	}
	
	public void revert(Document doc)
	{
		index = parent.removeLayer(layer);
		parent.addLayer(old, index);
		doc.setCurrent(parent);
	}
	
	public void repeat(Document doc)
	{
		index = parent.removeLayer(old);
		parent.addLayer(layer, index);
		doc.setCurrent(layer);
	}
}
