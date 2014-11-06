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
import heroesgrave.paint.image.change.IDocChange;

public class DeleteLayer implements IDocChange
{
	private Layer layer, parent;
	private int index = -1;
	
	public DeleteLayer(Layer layer)
	{
		this.layer = layer;
	}
	
	public void apply(Document doc)
	{
		parent = layer.getParentLayer();
		index = parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(parent);
	}
	
	public void revert(Document doc)
	{
		parent.addLayer(layer, index);
		doc.reconstructFlatmap();
		doc.setCurrent(layer);
	}
	
	public void repeat(Document doc)
	{
		parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(parent);
	}
}
