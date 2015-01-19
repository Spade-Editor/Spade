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
import heroesgrave.spade.image.change.IDocChange;

public class MetadataChange implements IDocChange
{
	private Layer layer;
	private String key, oldValue, newValue;
	
	public MetadataChange(Layer layer, String key, String value)
	{
		this.layer = layer;
		this.key = key;
		this.newValue = value;
		this.oldValue = layer.getMetadata().get(key);
	}
	
	public void apply(Document doc)
	{
		layer.getMetadata().set(key, newValue);
		layer.updateMetadata();
	}
	
	public void revert(Document doc)
	{
		layer.getMetadata().set(key, oldValue);
		layer.updateMetadata();
	}
	
	public void repeat(Document doc)
	{
		apply(doc);
	}
}
