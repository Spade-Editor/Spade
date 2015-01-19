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
import heroesgrave.spade.image.change.IResizingChange;

public class DocResize implements IDocChange
{
	private int oldWidth, oldHeight;
	private IResizingChange change;
	
	public DocResize(IResizingChange change)
	{
		this.change = change;
	}
	
	@Override
	public void apply(Document doc)
	{
		oldWidth = doc.getWidth();
		oldHeight = doc.getHeight();
		for(Layer l : doc.getFlatMap())
		{
			l.addChangeSilent(change);
		}
		doc.resize(change.getWidth(), change.getHeight());
	}
	
	@Override
	public void revert(Document doc)
	{
		for(Layer l : doc.getFlatMap())
		{
			l.revertChange();
		}
		doc.resize(oldWidth, oldHeight);
	}
	
	@Override
	public void repeat(Document doc)
	{
		for(Layer l : doc.getFlatMap())
		{
			l.repeatChange();
		}
		doc.resize(change.getWidth(), change.getHeight());
	}
}
