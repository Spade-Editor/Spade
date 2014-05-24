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

package heroesgrave.paint.image;

import heroesgrave.paint.gui.Renderer;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.image.change.IRevEditChange;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class FreezeBuffer
{
	private History source;
	private BufferedImage back, front;
	private LinkedList<IEditChange> changes = new LinkedList<IEditChange>();
	private Layer layer;
	private Document doc;
	private boolean rebuffer;
	
	public FreezeBuffer(Layer layer, BufferedImage image)
	{
		this.layer = layer;
		this.doc = layer.getDocument();
		this.back = image;
		this.front =
				new BufferedImage(image.getWidth(), image.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
		this.rebuffer = true;
	}
	
	public void addChange(IEditChange change)
	{
		changes.add(change);
		change.apply(front);
	}
	
	public void revertChange()
	{
		IEditChange change = changes.pollLast();
		if(change instanceof IRevEditChange)
			((IRevEditChange) change).revert(front);
		else
			rebuffer = true;
	}
	
	public void rebuffer()
	{
		{
			Graphics2D g = front.createGraphics();
			g.setBackground(Renderer.TRANSPARENT);
			g.clearRect(0, 0, front.getWidth(), front.getHeight());
			g.drawImage(back, 0, 0, null);
			g.dispose();
		}
		for(IEditChange c : changes)
		{
			c.apply(front);
		}
		rebuffer = false;
	}
	
	public BufferedImage getFront()
	{
		if(rebuffer)
			rebuffer();
		return front;
	}
}
