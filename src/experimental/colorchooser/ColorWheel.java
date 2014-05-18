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

import static experimental.colorchooser.ColorUtils.*;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import experimental.colorchooser.event.ColorEventBroadcaster;
import experimental.colorchooser.event.ColorEvent;
import experimental.colorchooser.event.ColorListener;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorWheel extends JComponent implements MouseMotionListener, MouseListener, ColorListener {
	
	public static final int RADIUS = 64;
	
	private BufferedImage buffer;
	
	private int mx, my; // mouse coords
	private double h, s;
	
	private ColorEventBroadcaster parent;
	
	public ColorWheel(ColorEventBroadcaster parent) {
		super();
		setDoubleBuffered(true);
		setBackground(new Color(0, true)); // transparent
		setSize(2 * RADIUS + 1, 2 * RADIUS + 1);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		addMouseMotionListener(this);
		addMouseListener(this);
		this.parent = parent;
		parent.addColorListener(this);
		buffer = genWheel();
		mx = my = RADIUS;
	}
	
	public int getSelectedColor() {
		return toARGB(h, s, 1, 1);
	}
	
	@Override
	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(buffer, 0, 0, null);
		g.setColor(Color.gray);
		g.drawOval(0, 0, 2 * RADIUS, 2 * RADIUS);
		g.setColor(Color.black);
		g.drawOval(mx - 3, my - 3, 6, 6);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		mx = x;
		my = y;
		
		int dx = x - RADIUS;
		int dy = y - RADIUS;
		
		if (Math.sqrt(dx * dx + dy * dy) > RADIUS) {
			double a = Math.atan2(dy, dx);
			mx = RADIUS + (int) Math.round(RADIUS * Math.cos(a));
			my = RADIUS + (int) Math.round(RADIUS * Math.sin(a));
		}
		
		dx = mx - RADIUS;
		dy = my - RADIUS;
		double a = Math.atan2(dy, dx);
		
		h = (a + Math.PI) / Math.PI / 2;
		s = MathUtils.clamp(Math.sqrt(dx * dx + dy * dy) / Math.sin(Math.PI / 4) / Math.sqrt(RADIUS * RADIUS * 2), 1, 0);
		
		int rgb = getSelectedColor();
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb >> 0) & 0xFF;
		
		ColorEvent ev = new ColorEvent(this, r, g, b, 1, Channel.Red, Channel.Green, Channel.Blue, Channel.Hue, Channel.Saturation, Channel.Value);
		
		parent.broadcastEvent(ev);
		
		repaint();
	}
	
	private BufferedImage genWheel() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		for (int y = 0; y < getHeight(); y++)
			for (int x = 0; x < getWidth(); x++) {
				
				int dx = x - RADIUS;
				int dy = y - RADIUS;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				if (dist <= RADIUS) {
					double a = Math.atan2(dy, dx);
					double h = (a + Math.PI) / (2 * Math.PI);
					double s = dist / Math.sqrt(RADIUS * RADIUS * 2);
					double v = 1;
					g.setColor(new Color(toARGB(h, s, v, 1)));
					g.drawLine(x, y, x, y);
				}
			}
		
		g.dispose();
		return img;
	}
	
	@Override
	public void colorChanged(ColorEvent e) {
		
		if (!e.changedChannels.contains(Channel.Value) || (e.changedChannels.contains(Channel.Red) && e.changedChannels.contains(Channel.Green) && e.changedChannels.contains(Channel.Blue))) {
			long hsva = toHSVA(e.r / 255., e.g / 255., e.b / 255., 1);
			if (e.changedChannels.contains(Channel.Hue)) {
				h = ((hsva >> 32) & 0xFFF) / 1024.;
			}
			if (e.changedChannels.contains(Channel.Saturation)) {
				s = ((hsva >> 16) & 0xFF) / 255.;
			}
			
			double a = h * 2 * Math.PI - Math.PI;
			
			mx = RADIUS + (int) Math.round(RADIUS * s * Math.cos(a));
			my = RADIUS + (int) Math.round(RADIUS * s * Math.sin(a));
			
			repaint();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
