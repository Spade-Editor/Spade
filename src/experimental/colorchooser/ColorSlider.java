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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import experimental.colorchooser.event.ColorComponenet;
import experimental.colorchooser.event.ColorEvent;
import experimental.colorchooser.event.ColorListener;
import static experimental.colorchooser.ColorUtils.*;

/**
 * Horizontal slider UI component for ColorChooser
 * 
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorSlider extends JComponent implements MouseMotionListener, MouseListener, ColorListener, ColorComponenet {
	
	private double value;
	private boolean hover;
	private BufferedImage buffer;
	private Color selectedColor = new Color(200, 200, 255);
	private GeneralPath cursor;
	
	private int[] colors;
	private int mode;
	private int channel;
	private MutableColor c[]; // dumb thing in SunGraphics2D requires non-identical objects for setColor() to actually work, so I use 2
	
	private List<ColorListener> listeners;
	
	public ColorSlider(Channel channel) {
		setSize(73, 15);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		
		setBackground(new Color(0, true));
		setDoubleBuffered(true);
		
		addMouseMotionListener(this);
		addMouseListener(this);
		
		listeners = new ArrayList<>();
		
		buffer = new BufferedImage(67, 11, BufferedImage.TYPE_INT_RGB);
		
		colors = new int[3];
		mode = channel.ordinal() / 3;
		this.channel = channel.ordinal() - mode * 3;
		
		c = new MutableColor[2];
		c[0] = new MutableColor();
		c[1] = new MutableColor();
		
		cursor = new GeneralPath();
		cursor.moveTo(0, 6);
		cursor.lineTo(6, 6);
		cursor.lineTo(3, 0);
		cursor.closePath();
		
		genGradient();
	}
	
	@Override
	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(buffer, 3, 0, null);
		
		g.translate(value * buffer.getWidth(), 8);
		g.setColor(Color.black);
		g.fill(cursor);
		g.setColor(hover ? selectedColor : Color.darkGray);
		g.draw(cursor);
	}
	
	private void genGradient() {
		Graphics gg = buffer.createGraphics();
		int w = buffer.getWidth();
		int bh = buffer.getHeight();
		
		if (mode == 0) { // rgb mode
			int r = colors[0];
			int g = colors[1];
			int b = colors[2];
			
			for (int x = 0; x < w; x++) {
				r = channel == 0 ? (int) (x / (double) w * 255) : r;
				g = channel == 1 ? (int) (x / (double) w * 255) : g;
				b = channel == 2 ? (int) (x / (double) w * 255) : b;
				gg.setColor(c[x & 1].setColor(r, g, b, 255));
				gg.drawLine(x, 0, x, bh);
			}
		} else { // hsv mode
			long hsva = toHSVA(colors[0] / 255., colors[1] / 255., colors[2] / 255., 1);
			double h = ((hsva >> 24) & 0x1FF) / 360.;
			double s = ((hsva >> 16) & 0xFF) / 255.;
			double v = ((hsva >> 8) & 0xFF) / 255.;
			
			for (int x = 0; x < w; x++) {
				h = channel == 0 ? (x / (double) w) : h;
				s = channel == 1 ? (x / (double) w) : channel == 0 ? 1 : s;
				v = channel == 2 ? (x / (double) w) : channel == 0 ? 1 : v;
				gg.setColor(c[x & 1].setColor(toARGB((x / (double) w), s, v, 1)));
				gg.drawLine(x, 0, x, bh);
			}
		}
		gg.dispose();
	}
	
	@Override
	public void colorChanged(ColorEvent e) {
		colors[0] = e.r;
		colors[1] = e.g;
		colors[2] = e.b;
	}
	
	@Override
	public void addColorListener(ColorListener c) {
		listeners.add(c);
	}
	
	@Override
	public void removeColorListener(ColorListener c) {
		listeners.remove(c);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		hover = true;
		value = MathUtils.clamp(e.getX() / (double) (buffer.getWidth() - 1), 1, 0);
		
		int rgb = buffer.getRGB(MathUtils.clamp(e.getX(), buffer.getWidth() - 1, 0), 1);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb >> 0) & 0xFF;
		ColorEvent ev = new ColorEvent(this, r, g, b, Channel.values[channel + mode * 3]);
		
		for (ColorListener c : listeners)
			c.colorChanged(ev);
		
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		hover = false;
		repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		hover = e.getX() >= 3 && e.getX() < buffer.getWidth() + 3 && e.getY() >= 0 && e.getY() < buffer.getHeight();
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseMoved(e);
	}
	
}
