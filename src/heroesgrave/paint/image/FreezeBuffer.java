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

import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.image.change.IRevEditChange;
import heroesgrave.paint.image.change.Marker;
import heroesgrave.paint.io.Serialised;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class FreezeBuffer
{
	private class OldBuffer
	{
		int order; // Also equals number of markers.
		BufferedImage image;
		
		OldBuffer(int order, BufferedImage image)
		{
			this.order = order;
			this.image = image;
		}
	}
	
	public static final int MAXIMUM = 4;
	public static final int MAXIMUM2 = MAXIMUM + MAXIMUM / 2;
	private History source;
	private LinkedList<OldBuffer> oldBuffers = new LinkedList<OldBuffer>();
	private BufferedImage front, back;
	private LinkedList<IEditChange> changes = new LinkedList<IEditChange>();
	private LinkedList<IEditChange> reverted = new LinkedList<IEditChange>();
	private LinkedList<Serialised> oldChanges = new LinkedList<Serialised>();
	private Layer layer;
	private Document doc;
	private boolean rebuffer;
	
	public FreezeBuffer(Layer layer, BufferedImage image)
	{
		this.layer = layer;
		this.doc = layer.getDocument();
		this.back = image;
		this.oldBuffers.push(new OldBuffer(1, back));
		this.oldChanges.push(new Marker());
		this.front = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.rebuffer = true;
	}
	
	private void pushOldBuffer(OldBuffer buffer)
	{
		if(this.oldBuffers.isEmpty())
		{
			this.oldBuffers.push(buffer);
			return;
		}
		OldBuffer top = this.oldBuffers.pop();
		if(top.order == buffer.order)
		{
			top.order *= 2;
			pushOldBuffer(top);
		}
		else
		{
			this.oldBuffers.push(top);
			this.oldBuffers.push(buffer);
		}
	}
	
	private boolean popOldBuffer()
	{
		if(oldBuffers.isEmpty())
			return false;
		OldBuffer top = this.oldBuffers.peek();
		if(top.order == 1)
		{
			oldBuffers.pop();
			this.back = top.image;
			Serialised s;
			while((s = oldChanges.poll()) != null)
			{
				if(s instanceof Marker)
					break;
				else
				{
					this.changes.push((IEditChange) s.decode());
				}
			}
			return true;
		}
		else
		{
			int i = top.order;
			LinkedList<Serialised> toReturn = new LinkedList<Serialised>();
			Serialised s;
			while(i * 2 > top.order)
			{
				if((s = oldChanges.poll()) == null)
				{
					throw new IllegalStateException("Not enough markers");
				}
				else if(s instanceof Marker)
				{
					i--;
				}
				toReturn.push(s);
			}
			BufferedImage image = new BufferedImage(front.getWidth(), front.getHeight(), BufferedImage.TYPE_INT_ARGB);
			image.setData(top.image.getData());
			while(i > 0)
			{
				if((s = oldChanges.poll()) == null)
				{
					throw new IllegalStateException("Not enough markers");
				}
				else if(s instanceof Marker)
				{
					i--;
				}
				else
				{
					((IEditChange) s.decode()).apply(image);
				}
				toReturn.push(s);
			}
			top.order /= 2;
			this.oldBuffers.push(new OldBuffer(top.order, image));
			while(!toReturn.isEmpty())
			{
				this.oldChanges.push(toReturn.pop());
			}
			return popOldBuffer();
		}
	}
	
	public void printBuffers()
	{
		System.out.print("Buffers: ");
		for(OldBuffer buf : this.oldBuffers)
		{
			System.out.printf("%d, ", buf.order);
		}
		System.out.println();
	}
	
	public void addChange(IEditChange change)
	{
		changes.add(change);
		change.apply(front);
		if(changes.size() >= MAXIMUM)
		{
			pushOldBuffer(new OldBuffer(1, back));
			
			this.back = this.front;
			this.front = new BufferedImage(front.getWidth(), front.getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.rebuffer = true;
			
			oldChanges.push(new Marker());
			while(!changes.isEmpty())
				oldChanges.push(changes.removeFirst().encode());
			printBuffers();
		}
	}
	
	public void revertChange()
	{
		if(changes.isEmpty())
		{
			this.front = this.back;
			if(!popOldBuffer())
				return;
		}
		IEditChange change = changes.pollLast();
		reverted.push(change);
		if(change instanceof IRevEditChange)
			((IRevEditChange) change).revert(front);
		else
			rebuffer = true;
	}
	
	public void repeatChange()
	{
		if(reverted.isEmpty())
		{
			return;
		}
		IEditChange change = reverted.pop();
		changes.add(change);
		change.apply(front);
		if(changes.size() >= MAXIMUM2)
		{
			pushOldBuffer(new OldBuffer(1, back));
			
			this.back = this.front;
			this.front = new BufferedImage(front.getWidth(), front.getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.rebuffer = true;
			
			oldChanges.push(new Marker());
			while(!changes.isEmpty())
				oldChanges.push(changes.removeFirst().encode());
			printBuffers();
		}
	}
	
	public void rebuffer()
	{
		front.setData(back.getData());
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
