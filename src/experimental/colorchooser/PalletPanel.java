/*
 * Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
package experimental.colorchooser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import experimental.colorchooser.event.ColorEventBroadcaster;
import experimental.colorchooser.event.ColorListener;
import static experimental.colorchooser.Channel.*;
import static experimental.colorchooser.ColorUtils.toARGB;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class PalletPanel extends JComponent implements MouseListener, MouseMotionListener, ColorListener {
	
	public static final int SWATCH_SIZE = 16;
	
	private Pallet pallet;
	private Color[] colors; // pallet cache
	private Color bg1, bg2;
	private int si = 0; // selected index
	private int lsi;
	private int mi = -1; // hover index
	private Stroke stroke1, stroke2;
	
	private ColorEventBroadcaster parent;
	
	public PalletPanel(ColorEventBroadcaster parent, Pallet p) {
		super();
		setDoubleBuffered(true);
		setPallet(p);
		setSize(16 * SWATCH_SIZE + 2, 6 * SWATCH_SIZE + 2);
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
		
		stroke1 = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] { SWATCH_SIZE / 4 }, 0f);
		stroke2 = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] { SWATCH_SIZE / 4 }, SWATCH_SIZE / 4);
	}
	
	public void setPallet(Pallet p) {
		pallet = p;
		colors = p.toColorArray();
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(1, 1, getWidth() - 1, getHeight() - 1);
		
		g.setColor(Color.darkGray);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		
		final int off = 1;
		
		for (int y = 0; y < 6; y++)
			for (int x = 0; x < 16; x++) {
				
				int xc = x * SWATCH_SIZE;
				int yc = y * SWATCH_SIZE;
				
				g.setColor(bg1);
				g.fillRect(off + xc, off + yc, SWATCH_SIZE / 2, SWATCH_SIZE / 2);
				g.fillRect(off + xc + SWATCH_SIZE / 2, off + yc + SWATCH_SIZE / 2, SWATCH_SIZE / 2, SWATCH_SIZE / 2);
				
				g.setColor(colors[x + y * 16]);
				g.fillRect(off + xc, off + yc, SWATCH_SIZE, SWATCH_SIZE);
				
				if (mi == x + y * 16 || si == x + y * 16) {
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
	
	public void unselect() {
		si = -1;
	}
	
	public int getSelectedColor() {
		return pallet.colors[si];
	}
	
	@Override
	public void changeColor(int r, int g, int b, int h, int s, int v, int a) {
		si = -1;
		
		
		int argb = toARGB(h / 1024., s / 255., v / 255., 1);
		int ri = (argb >> 16) & 0xFF;
		int gi = (argb >> 8) & 0xFF;
		int bi = (argb >> 0) & 0xFF;
		
		if(ri!=r||gi!=g||bi!=b)
			si = lsi;
		
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].getRed() == r && colors[i].getGreen() == g && colors[i].getBlue() == b && colors[i].getAlpha() == a) {
				si = i;
				break;
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		si = (e.getX() / SWATCH_SIZE) + (e.getY() / SWATCH_SIZE) * 16;
		lsi = si;
		
		parent.makeChange(this, Red, colors[si].getRed());
		parent.makeChange(this, Green, colors[si].getGreen());
		parent.makeChange(this, Blue, colors[si].getBlue());
		parent.makeChange(this, Alpha, colors[si].getAlpha());
		
		parent.broadcastChanges(null);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		mi = -1;
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mi = (e.getX() / SWATCH_SIZE) + (e.getY() / SWATCH_SIZE) * 16;
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
}
