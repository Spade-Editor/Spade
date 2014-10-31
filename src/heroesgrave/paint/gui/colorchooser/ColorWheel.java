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

import static heroesgrave.paint.gui.colorchooser.Channel.Hue;
import static heroesgrave.paint.gui.colorchooser.Channel.Saturation;
import static heroesgrave.paint.gui.colorchooser.ColorUtils.toARGB;
import static heroesgrave.paint.gui.colorchooser.ColorUtils.toHSVA;
import heroesgrave.paint.gui.colorchooser.event.ColourEventBroadcaster;
import heroesgrave.paint.gui.colorchooser.event.ColourListener;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorWheel extends JComponent implements MouseMotionListener, MouseListener, ColourListener
{
	public static final int RADIUS = 64;
	public static final int OFFSET = 3;
	
	private Image buffer;
	
	private int[][] m = new int[2][2];
	private double h1, s1, h2, s2;
	
	private ColourEventBroadcaster parent;
	
	public ColorWheel(ColourEventBroadcaster parent)
	{
		super();
		setDoubleBuffered(true);
		setBackground(new Color(0, true)); // transparent
		setSize(2 * RADIUS + 1 + 2 * OFFSET, 2 * RADIUS + 1 + 2 * OFFSET);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		addMouseMotionListener(this);
		addMouseListener(this);
		this.parent = parent;
		parent.addColorListener(this);
		buffer = genWheel();
		m[0][0] = m[0][1] = m[1][0] = m[1][1] = RADIUS;
	}
	
	public int getSelectedColor(boolean primary)
	{
		if(primary)
			return toARGB(h1, s1, 1, 1);
		else
			return toARGB(h2, s2, 1, 1);
	}
	
	@Override
	public void paint(Graphics gg)
	{
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(buffer, OFFSET, OFFSET, null);
		g.setColor(Color.gray);
		g.drawOval(OFFSET, OFFSET, 2 * RADIUS, 2 * RADIUS);
		g.setColor(Color.black);
		g.drawOval(OFFSET + m[1][0] - 3, OFFSET + m[1][1] - 3, 6, 6);
		g.setColor(Color.darkGray);
		g.drawOval(OFFSET + m[0][0] - 3, OFFSET + m[0][1] - 3, 6, 6);
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		boolean primary = (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == 0;
		int p = primary ? 1 : 0;
		
		int x = e.getX() - OFFSET;
		int y = e.getY() - OFFSET;
		
		m[p][0] = x;
		m[p][1] = y;
		
		int dx = x - RADIUS;
		int dy = y - RADIUS;
		
		if(Math.sqrt(dx * dx + dy * dy) > RADIUS)
		{
			double a = Math.atan2(dy, dx);
			m[p][0] = RADIUS + (int) Math.round(RADIUS * Math.cos(a));
			m[p][1] = RADIUS + (int) Math.round(RADIUS * Math.sin(a));
		}
		
		dx = m[p][0] - RADIUS;
		dy = m[p][1] - RADIUS;
		double a = Math.atan2(dy, dx);
		
		if(primary)
		{
			h1 = (a + Math.PI) / Math.PI / 2;
			s1 = MathUtils.clamp(Math.sqrt(dx * dx + dy * dy) / Math.sin(Math.PI / 4) / Math.sqrt(RADIUS * RADIUS * 2), 0, 1);
			
			parent.makeChange(this, Hue, (int) (h1 * 1023), true);
			parent.makeChange(this, Saturation, (int) (s1 * 255), true);
		}
		else
		{
			h2 = (a + Math.PI) / Math.PI / 2;
			s2 = MathUtils.clamp(Math.sqrt(dx * dx + dy * dy) / Math.sin(Math.PI / 4) / Math.sqrt(RADIUS * RADIUS * 2), 0, 1);
			
			parent.makeChange(this, Hue, (int) (h2 * 1023), false);
			parent.makeChange(this, Saturation, (int) (s2 * 255), false);
		}
		
		parent.broadcastChanges(this);
	}
	
	private BufferedImage genWheel()
	{
		BufferedImage img = new BufferedImage(2 * RADIUS, 2 * RADIUS, BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < img.getHeight(); y++)
			for(int x = 0; x < img.getWidth(); x++)
			{
				
				int dx = x - RADIUS;
				int dy = y - RADIUS;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				if(dist <= RADIUS)
				{
					double a = Math.atan2(dy, dx);
					double h = (a + Math.PI) / (2 * Math.PI);
					double s = dist / Math.sqrt(RADIUS * RADIUS);
					double v = 1;
					img.setRGB(x, y, toARGB(h, s, v, 1));
				}
			}
		return img;
	}
	
	@Override
	public void changeColor(int r, int g, int b, int hh, int ss, int v, int a, boolean primary)
	{
		if(v != 0)
		{
			long hsva = toHSVA(r / 255., g / 255., b / 255., 1);
			if(primary)
			{
				h1 = ((hsva >> 32) & 0x3FF) / 1023.;
				s1 = ((hsva >> 16) & 0xFF) / 255.;
			}
			else
			{
				h2 = ((hsva >> 32) & 0x3FF) / 1023.;
				s2 = ((hsva >> 16) & 0xFF) / 255.;
			}
		}
		else
		{
			if(primary)
			{
				h1 = hh / 1023.;
				s1 = ss / 255.;
			}
			else
			{
				h2 = hh / 1023.;
				s2 = ss / 255.;
			}
		}
		double angle = (primary ? h1 : h2) * 2 * Math.PI - Math.PI;
		int p = primary ? 1 : 0;
		double s = primary ? s1 : s2;
		
		m[p][0] = RADIUS + (int) Math.round(RADIUS * s * Math.cos(angle));
		m[p][1] = RADIUS + (int) Math.round(RADIUS * s * Math.sin(angle));
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseDragged(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
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
