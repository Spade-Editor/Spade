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

import static experimental.colorchooser.Channel.*;
import static experimental.colorchooser.ColorUtils.toARGB;
import static experimental.colorchooser.ColorUtils.toHSVA;
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

import experimental.colorchooser.event.ColorEventBroadcaster;
import experimental.colorchooser.event.ColorListener;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorWheel extends JComponent implements MouseMotionListener, MouseListener, ColorListener {
	
	public static final int RADIUS = 64;
	public static final int OFFSET = 3;
	
	private Image buffer;
	
	private int mx, my; // mouse coords
	private double h, s;
	
	private ColorEventBroadcaster parent;
	
	public ColorWheel(ColorEventBroadcaster parent) {
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
		mx = my = RADIUS;
	}
	
	public int getSelectedColor() {
		return toARGB(h, s, 1, 1);
	}
	
	@Override
	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(buffer, OFFSET, OFFSET, null);
		g.setColor(Color.gray);
		g.drawOval(OFFSET, OFFSET, 2 * RADIUS, 2 * RADIUS);
		g.setColor(Color.black);
		g.drawOval(OFFSET + mx - 3, OFFSET + my - 3, 6, 6);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX() - OFFSET;
		int y = e.getY() - OFFSET;
		
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
		
		parent.makeChange(Alpha, 255);
		parent.makeChange(Hue, (int) (h * 1024));
		parent.makeChange(Saturation, (int) (s * 255));
		parent.makeChange(Value, 255);
		
		parent.broadcastChanges(this);
	}
	
	private BufferedImage genWheel() {
		BufferedImage img = new BufferedImage(2 * RADIUS, 2 * RADIUS, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				
				int dx = x - RADIUS;
				int dy = y - RADIUS;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				if (dist <= RADIUS) {
					double a = Math.atan2(dy, dx);
					double h = (a + Math.PI) / (2 * Math.PI);
					double s = dist / Math.sqrt(RADIUS * RADIUS * 2);
					double v = 1;
					img.setRGB(x, y, toARGB(h, s, v, 1));
				}
			}
		return img;
	}
	
	@Override
	public void changeColor(int r, int g, int b, int a) {
		long hsva = toHSVA(r / 255., g / 255., b / 255., 1);
		h = ((hsva >> 32) & 0xFFF) / 1024.;
		s = ((hsva >> 16) & 0xFF) / 255.;
		
		double angle = h * 2 * Math.PI - Math.PI;
		
		mx = RADIUS + (int) Math.round(RADIUS * s * Math.cos(angle));
		my = RADIUS + (int) Math.round(RADIUS * s * Math.sin(angle));
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
