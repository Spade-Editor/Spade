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
import heroesgrave.paint.image.change.IResizingChange;

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
