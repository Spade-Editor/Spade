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

package heroesgrave.paint.image;

import heroesgrave.paint.image.CanvasManager.CanvasRenderer;
import heroesgrave.paint.image.SelectionCanvas.CombineMode;
import heroesgrave.paint.image.doc.DeselectedOp;
import heroesgrave.paint.image.doc.PasteOp;
import heroesgrave.paint.image.doc.SelectedOp;
import heroesgrave.paint.main.Paint;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class SelectionManager
{
	private boolean floating;
	private SelectionCanvas selection;
	
	/**
	 * Create the selection layer.
	 */
	public void create(Shape clip, CombineMode mode)
	{
		Area area = new Area(clip);
		if(selection != null && mode != CombineMode.REPLACE)
			area = new Area(selection.clip);
		
		if(floating)
			drop();
		
		switch(mode)
		{
			case ADD:
				area.add(new Area(clip));
				break;
			case SUBTRACT:
				area.subtract(new Area(clip));
				break;
			case XOR:
				area.exclusiveOr(new Area(clip));
				break;
			case INTERSECT:
				area.intersect(new Area(clip));
				break;
			default:
				break;
		}
		
		BufferedImage image = new BufferedImage(Paint.main.gui.canvas.getWidth(), Paint.main.gui.canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setClip(area);
		g.drawImage(Paint.main.gui.canvas.getCanvas().getImage(), 0, 0, null);
		
		selection = new SelectionCanvas(image, area);
		selection.setBlendMode(Paint.main.gui.canvas.getCanvas().mode);
		
		BufferedImage i2 = Paint.main.gui.canvas.getCanvas().getImage();
		Graphics2D g2 = i2.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setClip(area);
		g2.setColor(CanvasRenderer.TRANSPARENT);
		g2.setComposite(AlphaComposite.Src);
		g2.fillRect(0, 0, Paint.main.gui.canvas.getWidth(), Paint.main.gui.canvas.getHeight());
		g2.dispose();
		Paint.addChange(new KeyFrame(i2));
		Paint.main.history.addChange(new SelectedOp(selection, Paint.main.gui.canvas.getCanvas()));
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public void paste(BufferedImage clipboard)
	{
		if(floating)
			drop();
		
		Shape area = new Rectangle2D.Float(0, 0, clipboard.getWidth(), clipboard.getHeight());
		BufferedImage image = new BufferedImage(Paint.main.gui.canvas.getWidth(), Paint.main.gui.canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setClip(area);
		g.drawImage(clipboard, 0, 0, null);
		
		selection = new SelectionCanvas(image, area);
		Paint.main.history.addChange(new PasteOp(selection, Paint.main.gui.canvas.getCanvas()));
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public void setFloating(boolean floating)
	{
		this.floating = floating;
	}
	
	/**
	 * Deselect and merge down the selection layer.
	 */
	public void drop()
	{
		if(!floating)
			return;
		floating = false;
		Canvas parent = Paint.main.gui.canvas.getParentOf(selection);
		if(parent == null)
		{
			selection = null;
			return;
		}
		Paint.main.history.addChange(new DeselectedOp(selection, parent));
	}
	
	public boolean isSelection(Canvas canvas)
	{
		return canvas == selection;
	}
	
	public SelectionCanvas getSelection()
	{
		return selection;
	}
	
	public void setSelection(SelectionCanvas canvas)
	{
		this.selection = canvas;
	}
}