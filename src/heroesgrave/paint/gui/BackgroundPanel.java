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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.alee.laf.panel.WebPanel;

@SuppressWarnings("serial")
public class BackgroundPanel extends WebPanel implements MouseListener, MouseMotionListener
{
	private int lastButton;
	
	BufferedImage img;
	Rectangle2D rect;
	Renderer renderer;
	
	public BackgroundPanel()
	{
		this.setBackground(new java.awt.Color(0, true));
		this.setLayout(new GridBagLayout()); //GBL without constraints centers canvas.getPanel() automatically
	}
	
	public void activate()
	{
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void setRenderer(Renderer r)
	{
		if(this.renderer != null)
			this.remove(this.renderer);
		this.renderer = r;
		this.add(this.renderer);
		this.renderer.repaint();
	}
	
	public void incZoom()
	{
		if(this.renderer != null)
			this.renderer.incZoom();
	}
	
	public void decZoom()
	{
		if(this.renderer != null)
			this.renderer.decZoom();
	}
	
	public float getZoom()
	{
		return this.renderer.getScale();
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(Menu.DARK_BACKGROUND)
		{
			g.setColor(new Color(0x0f171f));
		}
		else
		{
			g.setColor(new Color(0xDDEEFF));
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		
		super.paint(g);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		short x = (short) MathUtils.floor((e.getX() - renderer.getX()) / getZoom());
		short y = (short) MathUtils.floor((e.getY() - renderer.getY()) / getZoom());
		lastButton = e.getButton();
		Paint.main.currentTool.onPressed(renderer.getDocument().getLayer(), x, y, e.getButton());
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		short x = (short) MathUtils.floor((e.getX() - renderer.getX()) / this.getZoom());
		short y = (short) MathUtils.floor((e.getY() - renderer.getY()) / getZoom());
		lastButton = e.getButton();
		Paint.main.currentTool.onReleased(renderer.getDocument().getLayer(), x, y, e.getButton());
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		short x = (short) MathUtils.floor((e.getX() - renderer.getX()) / getZoom());
		short y = (short) MathUtils.floor((e.getY() - renderer.getY()) / getZoom());
		Paint.main.currentTool.whilePressed(renderer.getDocument().getLayer(), x, y, lastButton);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		short x = (short) MathUtils.floor((e.getX() - renderer.getX()) / getZoom());
		short y = (short) MathUtils.floor((e.getY() - renderer.getY()) / getZoom());
		Paint.main.gui.info.setMouseCoords(x, y);
		Paint.main.currentTool.whileReleased(renderer.getDocument().getLayer(), x, y, lastButton);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
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
