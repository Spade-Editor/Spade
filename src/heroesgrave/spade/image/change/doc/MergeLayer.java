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
import heroesgrave.spade.image.blend.BlendMode;
import heroesgrave.spade.image.change.IDocChange;
import heroesgrave.spade.image.change.edit.SetImageChange;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MergeLayer implements IDocChange
{
	private Layer layer, parent;
	private int index = -1;
	
	public MergeLayer(Layer layer)
	{
		this.layer = layer;
	}
	
	@Override
	public void apply(Document doc)
	{
		this.parent = layer.getParentLayer();
		this.index = parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(parent);
		
		BufferedImage image = new BufferedImage(doc.getWidth(), doc.getHeight(), BufferedImage.TYPE_INT_ARGB);
		BlendMode mode = layer.getBlendMode();
		{
			Graphics2D g = image.createGraphics();
			g.drawImage(parent.getBufferedImage(), 0, 0, null);
			g.setComposite(mode);
			g.drawImage(layer.getBufferedImage(), 0, 0, null);
			g.dispose();
		}
		int[] buffer = RawImage.fromBufferedImage(image).borrowBuffer();
		parent.addChangeSilent(new SetImageChange(buffer));
	}
	
	@Override
	public void revert(Document doc)
	{
		parent.revertChange();
		
		parent.addLayer(layer, index);
		doc.reconstructFlatmap();
		doc.setCurrent(layer);
	}
	
	@Override
	public void repeat(Document doc)
	{
		parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(parent);
		
		parent.repeatChange();
	}
}
