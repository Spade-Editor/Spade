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

package heroesgrave.spade.image.change.doc;

import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.change.IDocChange;
import heroesgrave.spade.image.change.edit.ClearMaskChange;
import heroesgrave.spade.image.change.edit.FillImageChange;
import heroesgrave.spade.main.Spade;
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
		RawImage image = new RawImage(doc.getWidth(), doc.getHeight());
		{
			RawImage parentImage = parent.getImage();
			image.copyMaskFrom(parentImage);
			image.copyRegion(parentImage);
		}
		
		if(!preserveOld)
			parent.addChangeSilent(new FillImageChange(0x00000000));
		parent.addChangeSilent(new ClearMaskChange());
		
		Metadata info = new Metadata();
		info.set("name", "Floating Layer");
		
		layer = new Layer(doc, image, info).floating();
		Spade.main.gui.layers.setVisible(layer);
		parent.addLayer(layer);
		doc.setCurrent(layer);
	}
	
	public void revert(Document doc)
	{
		index = parent.removeLayer(layer);
		doc.setCurrent(parent);
		
		if(!preserveOld)
			parent.revertChange();
		parent.revertChange();
	}
	
	public void repeat(Document doc)
	{
		if(!preserveOld)
			parent.repeatChange();
		parent.repeatChange();
		
		parent.addLayer(layer, index);
		doc.setCurrent(layer);
	}
}
