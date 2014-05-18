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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JComponent;

import static experimental.colorchooser.ColorUtils.*;

/**
 * Horizontal slider UI component for ColorChooser
 * 
 * @author BurntPizza
 * 
 */
public class ColorSlider extends JComponent implements MouseMotionListener, MouseListener {
	
	private double value;
	private boolean hover;
	private BufferedImage buffer;
	private Color selectedColor = new Color(200, 200, 255);
	private GeneralPath cursor;
	
	private int[][] colors;
	private int mode;
	private int channel;
	private MutableColor c[]; // dumb thing in SunGraphics2D requires non-identical objects for setColor() to actually work, so I use 2
	
	public static enum Channel {
		Red, Green, Blue,
		
		Hue, Saturation, Value;
	}
	
	public ColorSlider(Channel channel) {
		setSize(73, 15);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		
		setBackground(new Color(0, true));
		setDoubleBuffered(true);
		
		addMouseMotionListener(this);
		addMouseListener(this);
		
		buffer = new BufferedImage(67, 11, BufferedImage.TYPE_INT_RGB);
		
		colors = new int[2][3];
		mode = channel.ordinal() % 3;
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
	
	private int lerp(int i1, int i2, double x) {
		return (int) (i1 + (i2 - i1) * x);
	}
	
	private void genGradient() {
		Graphics gg = buffer.createGraphics();
		int w = buffer.getWidth();
		int h = buffer.getHeight();
		
		if (mode == 0) { // rgb mode
			int r1 = colors[0][0];
			int g1 = colors[0][1];
			int b1 = colors[0][2];
			
			int r2 = colors[1][0];
			int g2 = colors[1][1];
			int b2 = colors[1][2];
			
			switch (channel) {
				case 0:
					for (int x = 0; x < w; x++) {
						int ri = lerp(0, 255, x / (double) w);
						int gi = lerp(g1, g2, x / (double) w);
						int bi = lerp(b1, b2, x / (double) w);
						c[x&1].setColor(ri, gi, bi, 255);
						gg.setColor(c[x&1]);
						gg.drawLine(x, 0, x, h);
					}
					break;
				case 1:
					for (int x = 0; x < w; x++) {
						int ri = lerp(r1, r2, x / (double) w);
						int gi = (int) (x / ((double) w) * 255);
						int bi = lerp(b1, b2, x / (double) w);
						c[x&1].setColor(ri, gi, bi, 255);
						gg.setColor(c[x&1]);
						gg.drawLine(x, 0, x, h);
					}
					break;
				case 2:
					for (int x = 0; x < w; x++) {
						int ri = lerp(r1, r2, x / (double) w);
						int gi = lerp(g1, g2, x / (double) w);
						int bi = (int) (x / ((double) w) * 255);
						c[x&1].setColor(ri, gi, bi, 255);
						gg.setColor(c[x&1]);
						gg.drawLine(x, 0, x, h);
					}
					break;
			}
		} else { // hsv mode
			/*long hsva = toHSVA(colors[0] / 255., colors[1] / 255., colors[2] / 255., 1);
			double h = ((hsva >> 24) & 0x1FF) / 360.;
			double s = ((hsva >> 16) & 0xFF) / 255.;
			double v = ((hsva >> 8) & 0xFF) / 255.;
			
			switch (channel) {
				case 0:
					for (int y = 0; y < buffer.getHeight(); y++)
						for (int x = 0; x < buffer.getWidth(); x++) {
							color.setColor(toARGB((x / (double) buffer.getWidth()), s, v, 1));
							gg.setColor(color);
							gg.drawLine(x, y, x, y);
						}
					break;
				case 1:
					for (int y = 0; y < buffer.getHeight(); y++)
						for (int x = 0; x < buffer.getWidth(); x++) {
							color.setColor(toARGB(h, (x / (double) buffer.getWidth()), v, 1));
							gg.setColor(color);
							gg.drawLine(x, y, x, y);
						}
					break;
				case 2:
					for (int y = 0; y < buffer.getHeight(); y++)
						for (int x = 0; x < buffer.getWidth(); x++) {
							color.setColor(toARGB(h, s, (x / (double) buffer.getWidth()), 1));
							gg.setColor(color);
							gg.drawLine(x, y, x, y);
						}
					break;
			}*/
		}
		gg.dispose();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		hover = true;
		value = MathUtils.clamp(e.getX() / (double) buffer.getWidth(), 1, 0);
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
		hover = e.getX() >= 3 && e.getX() < buffer.getWidth() + 3 && e.getY() < buffer.getHeight();
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
	}
	
}
