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

import static heroesgrave.spade.gui.colorchooser.ColorUtils.toARGB;
import heroesgrave.spade.gui.colorchooser.event.ColourEventBroadcaster;
import heroesgrave.spade.gui.colorchooser.event.ColourListener;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Horizontal slider UI component for ColorChooser
 * 
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColourSlider extends JComponent implements MouseMotionListener, MouseListener, ColourListener
{
	
	private double value;
	private boolean hover;
	private BufferedImage buffer;
	private Color selectedColor = new Color(200, 200, 255);
	private BufferedImage cursorImage, cursorSelected;
	
	private int[] colors;
	private int mode;
	private int channel;
	
	private ColourEventBroadcaster parent;
	
	public ColourSlider(Channel channel, ColourEventBroadcaster parent)
	{
		setSize(73, 18);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		
		setBackground(new Color(0, true));
		setDoubleBuffered(true);
		
		addMouseMotionListener(this);
		addMouseListener(this);
		
		this.parent = parent;
		parent.addColorListener(this);
		
		buffer = new BufferedImage(66, 14, BufferedImage.TYPE_INT_RGB);
		
		colors = new int[3];
		mode = channel.ordinal() / 3;
		this.channel = channel.ordinal() - mode * 3;
		
		prepareCursor();
		
		genGradient();
	}
	
	@Override
	public void paint(Graphics gg)
	{
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(buffer, 3, 1, null);
		
		g.setColor(Color.darkGray);
		g.setColor(MutableColor.getColor(20, 20, 20));
		g.drawRect(2, 0, buffer.getWidth() + 1, buffer.getHeight() + 1);
		
		g.drawImage(hover ? cursorSelected : cursorImage, (int) (value * buffer.getWidth()), 10, null);
	}
	
	private void genGradient()
	{
		int bw = buffer.getWidth();
		int bh = buffer.getHeight();
		Graphics gg = buffer.createGraphics();
		
		if(mode == 0)
		{ // rgb mode
			int r = colors[0];
			int g = colors[1];
			int b = colors[2];
			
			for(int x = 0; x < bw; x++)
			{
				r = channel == 0 ? (int) (x / (double) bw * 255) : r;
				g = channel == 1 ? (int) (x / (double) bw * 255) : g;
				b = channel == 2 ? (int) (x / (double) bw * 255) : b;
				gg.setColor(MutableColor.getColor(r, g, b));
				gg.drawLine(x, 0, x, bh);
			}
		}
		else if(mode == 1)
		{ // hsv mode
			//long hsva = toHSVA(colors[0] / 255., colors[1] / 255., colors[2] / 255., 1);
			double h = colors[0] / 1023.;
			double s = colors[1] / 255.;
			double v = colors[2] / 255.;
			
			for(int x = 0; x < bw; x++)
			{
				h = channel == 0 ? (x / (double) bw) : h;
				s = channel == 1 ? (x / (double) bw) : channel == 0 ? 1 : s;
				v = channel == 2 ? (x / (double) bw) : channel == 0 ? 1 : v;
				gg.setColor(MutableColor.getColor(toARGB(h, s, v, 1)));
				gg.drawLine(x, 0, x, bh);
			}
		}
		else
		{ //alpha mode
			final int size = 8;
			
			gg.setColor(Color.gray);
			gg.fillRect(0, 0, bw, bh);
			
			gg.setColor(Color.white);
			for(int x = 0; x < bw; x += size * 2)
			{
				gg.fillRect(x, 0, size, size);
				gg.fillRect(x + size, size, size, size);
			}
			
			int r = colors[0];
			int g = colors[1];
			int b = colors[2];
			
			for(int x = 0; x < bw; x++)
			{
				gg.setColor(MutableColor.getColor(r, g, b, (int) ((x / (double) bw) * 255)));
				gg.drawLine(x, 0, x, bh);
			}
		}
		gg.dispose();
	}
	
	@Override
	public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
	{
		if(!primary)
			return;
		if(mode == 0)
		{
			colors[0] = r;
			colors[1] = g;
			colors[2] = b;
			value = MathUtils.clamp(colors[channel] / 255., 0, 1);
		}
		else if(mode == 1)
		{
			colors[0] = h;
			colors[1] = s;
			colors[2] = v;
			//long hsva = toHSVA(colors[0] / 255., colors[1] / 255., colors[2] / 255., 1);
			//int h = (int) ((hsva >> 32) & 0xFFF);
			//int s = (int) ((hsva >> 16) & 0xFF);
			//int v = (int) ((hsva >> 8) & 0xFF);
			
			double cc = channel == 0 ? h / 1023. : channel == 1 ? s / 255. : v / 255.;
			
			value = MathUtils.clamp(cc, 0, 1);
		}
		else
		{
			colors[0] = r;
			colors[1] = g;
			colors[2] = b;
			value = MathUtils.clamp(a / 255., 0, 1);
		}
		
		// if this isn't hue, re-make the gradient
		if(!(mode == 1 && channel == 0))
		{
			genGradient();
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		hover = true;
		value = MathUtils.clamp((e.getX() - 3) / (double) (buffer.getWidth()), 0, 1);
		
		int range = 255;
		
		if(mode == 1 && channel == 0)
		{ // if this is hue
			range = 1023;
		}
		
		parent.makeChange(this, Channel.values[channel + mode * 3], (int) (value * range), true);
		parent.broadcastChanges(this);
	}
	
	private void prepareCursor()
	{
		GeneralPath cursor = new GeneralPath();
		cursor.moveTo(0, 6);
		cursor.lineTo(6, 6);
		cursor.lineTo(3, 0);
		cursor.closePath();
		
		cursorImage = new BufferedImage(9, 8, BufferedImage.TYPE_INT_ARGB);
		cursorSelected = new BufferedImage(9, 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = cursorImage.createGraphics();
		Graphics2D gs = cursorSelected.createGraphics();
		
		g.setColor(Color.black);
		g.fill(cursor);
		g.setColor(Color.darkGray);
		g.draw(cursor);
		
		gs.setColor(Color.black);
		gs.fill(cursor);
		gs.setColor(selectedColor);
		gs.draw(cursor);
		
		g.dispose();
		gs.dispose();
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		hover = false;
		repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		hover = e.getX() >= 3 && e.getX() < buffer.getWidth() + 3 && e.getY() >= 0 && e.getY() < buffer.getHeight();
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseDragged(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseMoved(e);
	}
}
