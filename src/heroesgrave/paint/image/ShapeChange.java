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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class ShapeChange extends Frame
{
	private Shape changeShape;
	private int colour;
	private Stroke stroke;
	private Composite composite;
	private boolean filling = false;
	private boolean antialiasing = false;
	
	public ShapeChange(Shape shape, int colour)
	{
		this.changeShape = shape;
		this.colour = colour;
		if((colour & 0xff000000) != 0xff000000)
			composite = AlphaComposite.getInstance(AlphaComposite.SRC, ((colour >> 24) & 0xff) / 255f);
	}
	
	public ShapeChange(Shape shape, int colour, Stroke stroke)
	{
		this.changeShape = shape;
		this.colour = colour;
		this.stroke = stroke;
		if((colour & 0xff000000) != 0xff000000)
			composite = AlphaComposite.getInstance(AlphaComposite.SRC, ((colour >> 24) & 0xff) / 255f);
	}
	
	public void apply(BufferedImage image)
	{
		Graphics2D g2d = image.createGraphics();
		
		if(antialiasing)
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Stroke oldStroke = null;
		Composite oldComp = null;
		Color oldColor = g2d.getColor();
		
		if(stroke != null)
		{
			oldStroke = g2d.getStroke();
			g2d.setStroke(stroke);
		}
		if(composite != null)
		{
			oldComp = g2d.getComposite();
			g2d.setComposite(composite);
		}
		g2d.setColor(new Color(colour));
		
		if(filling)
			g2d.fill(changeShape);
		else
			g2d.draw(changeShape);
		
		g2d.setColor(oldColor);
		if(composite != null)
			g2d.setComposite(oldComp);
		if(stroke != null)
			g2d.setStroke(oldStroke);
	}
	
	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}
	
	public ShapeChange setFill(boolean fillFlag)
	{
		filling = fillFlag;
		return this;
	}
	
	public ShapeChange setAntialiasing(boolean alias)
	{
		antialiasing = alias;
		return this;
	}
	
	public ShapeChange setComposite(Composite composite)
	{
		this.composite = composite;
		return this;
	}
}