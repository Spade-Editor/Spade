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
import heroesgrave.paint.image.change.edit.FillImageChange;
import heroesgrave.utils.misc.Metadata;

public class FloatLayer implements IDocChange
{
	private Layer layer, parent;
	private int index = -1;
	private boolean preserveOld;
	
	public FloatLayer(Layer parent, boolean preserveOld)
	{
		this.parent = parent;
		this.preserveOld = preserveOld;
	}
	
	public void apply(Document doc)
	{
		RawImage image = new RawImage(parent.getWidth(), parent.getHeight());
		image.copyMaskFrom(parent.getImage());
		image.copyRegion(parent.getImage());
		
		if(!preserveOld)
			parent.addChangeSilent(new FillImageChange(0x00000000));
		parent.addChangeSilent(new ClearMaskChange());
		
		Metadata info = new Metadata();
		info.set("name", "Floating Layer");
		
		layer = new Layer(doc, image, info);
		parent.addLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(layer);
	}
	
	public void revert(Document doc)
	{
		index = parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(parent);
		
		if(!preserveOld)
			parent.revertChange();
		parent.revertChange();
	}
	
	public void repeat(Document doc)
	{
		parent.addLayer(layer, index);
		doc.reconstructFlatmap();
		doc.setCurrent(layer);
		
		if(!preserveOld)
			parent.repeatChange();
		parent.repeatChange();
	}
}
