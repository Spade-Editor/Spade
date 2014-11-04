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

package heroesgrave.paint.gui.colorchooser;

import static heroesgrave.paint.gui.colorchooser.Channel.Alpha;
import static heroesgrave.paint.gui.colorchooser.Channel.Blue;
import static heroesgrave.paint.gui.colorchooser.Channel.Green;
import static heroesgrave.paint.gui.colorchooser.Channel.Red;
import heroesgrave.paint.gui.colorchooser.event.ColourEventBroadcaster;
import heroesgrave.paint.gui.colorchooser.event.ColourListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorIndicator extends JComponent implements MouseListener, ColourListener
{
	public static final int SIZE = 64;
	
	private ColourEventBroadcaster parent;
	private HexColourField hexField;
	private MutableColor primary, secondary;
	
	public ColorIndicator(ColourEventBroadcaster parent, HexColourField hexField)
	{
		super();
		this.hexField = hexField;
		this.parent = parent;
		parent.addColorListener(this);
		
		setSize(52, 52);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		
		setBackground(new Color(0, true));
		setDoubleBuffered(true);
		
		addMouseListener(this);
		
		primary = new MutableColor();
		secondary = new MutableColor();
		
	}
	
	public MutableColor getPrimary()
	{
		return primary;
	}
	
	public MutableColor getSecondary()
	{
		return secondary;
	}
	
	public void setPrimary(int r, int g, int b, int a)
	{
		primary.setColor(r, g, b, a);
	}
	
	public void setSecondary(int r, int g, int b, int a)
	{
		secondary.setColor(r, g, b, a);
	}
	
	public void flip()
	{
		int color = primary.getRGB();
		primary.setColor(secondary.getRGB());
		secondary.setColor(color);
		repaint();
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.drawRect(SIZE / 4 + 1, SIZE / 4 + 1, SIZE / 2 + 1, SIZE / 2 + 1);
		g.setColor(Color.black);
		g.drawRect(SIZE / 4, SIZE / 4, SIZE / 2 + 3, SIZE / 2 + 3);
		
		final int off = 2;
		
		for(int y = 0; y < SIZE / 2; y += 16)
			for(int x = 0; x < SIZE / 2; x += 16)
			{
				g.setColor(Color.gray);
				g.fillRect(off + x, off + y, 8, 8);
				g.fillRect(off + x + 8, off + y + 8, 8, 8);
				
				g.fillRect(off + x + SIZE / 4, off + y + SIZE / 4, 8, 8);
				g.fillRect(off + x + 8 + SIZE / 4, off + y + 8 + SIZE / 4, 8, 8);
				
				g.setColor(Color.white);
				g.fillRect(off + x + 8, off + y, 8, 8);
				g.fillRect(off + x, off + y + 8, 8, 8);
				
				g.fillRect(off + x + 8 + SIZE / 4, off + y + SIZE / 4, 8, 8);
				g.fillRect(off + x + SIZE / 4, off + y + 8 + SIZE / 4, 8, 8);
			}
		
		g.setColor(secondary);
		g.fillRect(18, 36, SIZE / 2, 14);
		g.fillRect(36, 18, 14, 18);
		
		g.setColor(primary);
		g.fillRect(2, 2, SIZE / 2, SIZE / 2);
		
		g.setColor(Color.white);
		g.drawRect(1, 1, SIZE / 2 + 1, SIZE / 2 + 1);
		g.setColor(Color.black);
		g.drawRect(0, 0, SIZE / 2 + 3, SIZE / 2 + 3);
	}
	
	@Override
	public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
	{
		if(primary)
			this.primary.setColor(r, g, b, a);
		else
			this.secondary.setColor(r, g, b, a);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		boolean swap = (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0;
		
		if(swap)
		{
			MutableColor tmp = primary;
			primary = secondary;
			secondary = tmp;
			parent.makeChange(this, Red, secondary.getRed(), false);
			parent.makeChange(this, Green, secondary.getGreen(), false);
			parent.makeChange(this, Blue, secondary.getBlue(), false);
			parent.makeChange(this, Alpha, secondary.getAlpha(), false);
			parent.makeChange(this, Red, primary.getRed(), true);
			parent.makeChange(this, Green, primary.getGreen(), true);
			parent.makeChange(this, Blue, primary.getBlue(), true);
			parent.makeChange(this, Alpha, primary.getAlpha(), true);
			
			parent.broadcastChanges(this);
		}
		else
		{
			int x = e.getX();
			int y = e.getY();
			
			if(x >= 2 && y >= 2 && x < 34 && y < 34)
			{
				hexField.showFor(true, this);
			}
			else if(((x >= 36 && y >= 18) || (x >= 18 && y >= 36)) && x < 50 && y < 50)
			{
				hexField.showFor(false, this);
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
}
