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

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.main.Paint;

public class DeselectedOp extends DocumentChange
{
	private Canvas canvas, parent;
	
	public DeselectedOp(Canvas c, Canvas p)
	{
		this.canvas = c;
		this.parent = p;
	}
	
	public void apply()
	{
		parent.mergeLayer(canvas);
		Paint.main.gui.canvas.selection.setFloating(false);
		Paint.main.gui.canvas.select(parent);
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public void revert()
	{
		parent.unmergeLayer(canvas);
		Paint.main.gui.canvas.selection.setFloating(true);
		Paint.main.gui.canvas.select(canvas);
		Paint.main.gui.canvas.getPanel().repaint();
	}
}