/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.paint.image.doc;

import heroesgrave.paint.gui.LayerManager.LayerNode;
import heroesgrave.paint.main.Paint;

public class LayerMoveOp extends DocumentChange
{
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int MOVE_IN = 3;
	public static final int MOVE_OUT = 4;
	
	private int mode;
	private LayerNode node;
	
	public LayerMoveOp(LayerNode layer, int mode)
	{
		this.node = layer;
		this.mode = mode;
		if(mode > 4 || mode < 1)
			throw new IllegalArgumentException("Valid move modes are 1-4.");
	}
	
	public void apply()
	{
		if(mode == 1)
		{
			LayerNode swap = (LayerNode) node.getPreviousSibling();
			if(swap != null)
			{
				LayerNode parent = (LayerNode) node.getParent();
				parent.canvas.swap(node.canvas, swap.canvas);
				int i = parent.getIndex(node);
				int j = parent.getIndex(swap);
				parent.remove(j);
				parent.insert(node, j);
				parent.insert(swap, i);
			}
		}
		else if(mode == 2)
		{
			LayerNode swap = (LayerNode) node.getNextSibling();
			if(swap != null)
			{
				LayerNode parent = (LayerNode) node.getParent();
				parent.canvas.swap(node.canvas, swap.canvas);
				int i = parent.getIndex(node);
				int j = parent.getIndex(swap);
				parent.remove(i);
				parent.insert(swap, i);
				parent.insert(node, j);
			}
		}
		else if(mode == 3)
		{
			LayerNode newParent = (LayerNode) node.getPreviousSibling();
			if(newParent == null)
				return;
			LayerNode parent = (LayerNode) node.getParent();
			newParent.add(node);
			parent.canvas.removeLayer(node.canvas);
			newParent.canvas.addLayer(node.canvas);
		}
		else
		{
			LayerNode parent = (LayerNode) node.getParent();
			if(parent == null || parent.isRoot())
				return;
			LayerNode newParent = (LayerNode) parent.getParent();
			newParent.insert(node, newParent.getIndex(parent) + 1);
			parent.canvas.removeLayer(node.canvas);
			newParent.canvas.addLayer(node.canvas);
		}
		Paint.main.gui.layers.redrawTree();
	}
	
	public void revert()
	{
		if(mode == 2)
		{
			LayerNode swap = (LayerNode) node.getPreviousSibling();
			if(swap != null)
			{
				LayerNode parent = (LayerNode) node.getParent();
				parent.canvas.swap(node.canvas, swap.canvas);
				int i = parent.getIndex(node);
				int j = parent.getIndex(swap);
				parent.remove(j);
				parent.insert(node, j);
				parent.insert(swap, i);
			}
		}
		else if(mode == 1)
		{
			LayerNode swap = (LayerNode) node.getNextSibling();
			if(swap != null)
			{
				LayerNode parent = (LayerNode) node.getParent();
				parent.canvas.swap(node.canvas, swap.canvas);
				int i = parent.getIndex(node);
				int j = parent.getIndex(swap);
				parent.remove(i);
				parent.insert(swap, i);
				parent.insert(node, j);
			}
		}
		else if(mode == 4)
		{
			LayerNode newParent = (LayerNode) node.getPreviousSibling();
			if(newParent == null)
				return;
			LayerNode parent = (LayerNode) node.getParent();
			newParent.add(node);
			parent.canvas.removeLayer(node.canvas);
			newParent.canvas.addLayer(node.canvas);
		}
		else
		{
			LayerNode parent = (LayerNode) node.getParent();
			if(parent == null || parent.isRoot())
				return;
			LayerNode newParent = (LayerNode) parent.getParent();
			newParent.insert(node, newParent.getIndex(parent) + 1);
			parent.canvas.removeLayer(node.canvas);
			newParent.canvas.addLayer(node.canvas);
		}
		Paint.main.gui.layers.redrawTree();
	}
}