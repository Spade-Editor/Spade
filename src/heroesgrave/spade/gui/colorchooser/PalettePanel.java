// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.gui.colorchooser;

import static heroesgrave.spade.gui.colorchooser.Channel.Alpha;
import static heroesgrave.spade.gui.colorchooser.Channel.Blue;
import static heroesgrave.spade.gui.colorchooser.Channel.Green;
import static heroesgrave.spade.gui.colorchooser.Channel.Red;
import heroesgrave.spade.gui.colorchooser.event.ColourEventBroadcaster;
import heroesgrave.spade.gui.colorchooser.event.ColourListener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class PalettePanel extends JComponent implements MouseListener, MouseMotionListener, ColourListener
{
	enum PaletteMode
	{
		Minimized, Standard, Extended
	}
	
	public static final int SWATCH_SIZE = 16;
	
	private Palette pallet;
	private int[] colors; // pallet cache
	private Color bg1, bg2;
	private int si = 0; // selected index
	private int mi = -1; // hover index
	private Stroke stroke1, stroke2;
	
	private ColourEventBroadcaster parent;
	private PaletteMode mode;
	
	public PalettePanel(ColourEventBroadcaster parent, Palette p)
	{
		super();
		setDoubleBuffered(true);
		setPalette(p);
		setSize(12 * SWATCH_SIZE + 2, 6 * SWATCH_SIZE + 2);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.parent = parent;
		parent.addColorListener(this);
		
		bg1 = Color.gray;
		bg2 = Color.white;
		
		setBackground(bg2);
		
		stroke1 = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{SWATCH_SIZE / 4}, 0f);
		stroke2 = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{SWATCH_SIZE / 4}, SWATCH_SIZE / 4);
	}
	
	public void setPalette(Palette p)
	{
		pallet = p;
		colors = p.standard;
	}
	
	public void setMode(PaletteMode m)
	{
		if(this.mode == m)
			return;
		this.mode = m;
		switch(this.mode)
		{
			case Minimized:
				colors = pallet.minimized;
				setSize(8 * SWATCH_SIZE + 2, 3 * SWATCH_SIZE + 2);
				break;
			case Standard:
				colors = pallet.standard;
				setSize(12 * SWATCH_SIZE + 2, 3 * SWATCH_SIZE + 2);
				break;
			case Extended:
				colors = pallet.extended;
				setSize(12 * SWATCH_SIZE + 2, 6 * SWATCH_SIZE + 2);
				break;
		}
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.clearRect(1, 1, getWidth() - 1, getHeight() - 1);
		
		g.setColor(Color.darkGray);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		
		final int off = 1;
		
		int cols = mode == PaletteMode.Minimized ? 8 : 12;
		int rows = mode == PaletteMode.Extended ? 6 : 3;
		
		for(int y = 0; y < rows; y++)
			for(int x = 0; x < cols; x++)
			{
				int xc = x * SWATCH_SIZE;
				int yc = y * SWATCH_SIZE;
				
				g.setColor(bg1);
				g.fillRect(off + xc, off + yc, SWATCH_SIZE / 2, SWATCH_SIZE / 2);
				g.fillRect(off + xc + SWATCH_SIZE / 2, off + yc + SWATCH_SIZE / 2, SWATCH_SIZE / 2, SWATCH_SIZE / 2);
				
				{
					int color = colors[x + y * cols];
					int a = (color >> 24) & 0xFF;
					int r = (color >> 16) & 0xFF;
					int _g = (color >> 8) & 0xFF;
					int b = (color >> 0) & 0xFF;
					
					g.setColor(new Color(r, _g, b, a));
				}
				g.fillRect(off + xc, off + yc, SWATCH_SIZE, SWATCH_SIZE);
				
				if(mi == x + y * cols || si == x + y * cols)
				{
					Graphics2D gg = (Graphics2D) g;
					Stroke s = gg.getStroke();
					gg.setStroke(stroke1);
					gg.setColor(Color.black);
					gg.drawRect(off + xc, off + yc, SWATCH_SIZE - 1, SWATCH_SIZE - 1);
					gg.setStroke(stroke2);
					gg.setColor(Color.white);
					gg.drawRect(off + xc, off + yc, SWATCH_SIZE - 1, SWATCH_SIZE - 1);
					gg.setStroke(s);
				}
			}
	}
	
	public void unselect()
	{
		si = -1;
	}
	
	public int getSelectedColor()
	{
		return colors[si];
	}
	
	@Override
	public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
	{
		if(!primary)
			return;
		si = -1;
		
		int c = (a << 24) | (r << 16) | (g << 8) | b;
		
		for(int i = 0; i < colors.length; i++)
		{
			if(colors[i] == c)
			{
				si = i;
				break;
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		boolean primary = e.getButton() == MouseEvent.BUTTON1;
		
		si = (e.getX() / SWATCH_SIZE) + (e.getY() / SWATCH_SIZE) * (mode == PaletteMode.Minimized ? 8 : 12);
		
		int c = colors[si];
		parent.makeChange(this, Red, (c >> 16) & 0xFF, primary);
		parent.makeChange(this, Green, (c >> 8) & 0xFF, primary);
		parent.makeChange(this, Blue, c & 0xFF, primary);
		parent.makeChange(this, Alpha, (c >> 24) & 0xFF, primary);
		
		parent.broadcastChanges(null);
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		mi = -1;
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		mi = (e.getX() / SWATCH_SIZE) + (e.getY() / SWATCH_SIZE) * (mode == PaletteMode.Minimized ? 8 : 12);
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}
}
