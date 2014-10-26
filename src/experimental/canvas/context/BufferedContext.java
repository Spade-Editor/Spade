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
package experimental.canvas.context;

import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import experimental.colorchooser.ColorUtils;

/**
 * * insert description here *
 * 
 * @author BurntPizza
 * 
 */
public class BufferedContext {
	
	protected final BufferedImage image;
	protected final int[] buffer; // using INT_ARGB
	protected final int[] clipBuffer;
	protected final int w, h;
	
	/**
	 * Background color
	 */
	protected int bgr, bgg, bgb, bga;
	
	/**
	 * Current painting color
	 */
	// note: they are premultiplied and half-clamped, except alpha
	protected int cr, cg, cb, ca, cc;
	protected int crp, cgp, cbp;
	
	/**
	 * Creates a blank context of width and height
	 */
	public BufferedContext(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		w = width;
		h = height;
		buffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		clipBuffer = new int[buffer.length];
	}
	
	/**
	 * Creates a new context wrapping image
	 */
	public BufferedContext(BufferedImage image) {
		if (image.getType() != BufferedImage.TYPE_INT_ARGB)
			throw new IllegalArgumentException("Images to be wrapped must be of type INT_ARGB");
		
		this.image = image;
		w = image.getWidth();
		h = image.getHeight();
		buffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		clipBuffer = new int[buffer.length];
	}
	
	public void setColor(int argb) {
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = argb & 0xFF;
		
		setColor(r, g, b, a);
	}
	
	public void setColor(Color color) { // MutableColor works here
		setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public void setColor(int r, int g, int b, int a) {
		cr = mult(r, a);
		cg = mult(g, a);
		cb = mult(b, a);
		
		crp = (cr << 16) - 0xFF0000;
		cgp = (cg << 8) - 0x00FF00;
		cbp = cb - 0x0000FF;
		
		cc = ColorUtils.pack(cr, cg, cb, ca);
		
		ca = a;
	}
	
	public void setColor(int r, int g, int b) {
		setColor(r, g, b, 255);
	}
	
	public void setBackground(int argb) {
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = argb & 0xFF;
		setBackground(r, g, b, a);
	}
	
	public void setBackground(Color color) {
		setBackground(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public void setBackground(int r, int g, int b, int a) {
		bgr = mult(r, a);
		bgg = mult(g, a);
		bgb = mult(b, a);
		bga = a;
	}
	
	public void setBackground(int r, int g, int b) {
		setBackground(r, g, b, 255);
	}
	
	public void drawPixel(int x, int y) {
		drawPixel(x + y * w);
	}
	
	protected void drawPixel(int i) {
		final int src = buffer[i];
		
		// get channels
		int a = src & 0xFF000000;
		int r = src & 0x00FF0000;
		int g = src & 0x0000FF00;
		int b = src & 0x000000FF;
		
		// add src and current color (premultiplied with alpha), then clamp
		r += crp;
		g += cgp;
		b += cbp;
		
		r &= r >> 8;
		g &= g >> 8;
		b &= b >> 8;
		
		r += 0xFF0000;
		g += 0x00FF00;
		b += 0x0000FF;
		
		// store
		buffer[i] = a | r | g | b;
	}
	
	/**
	 * Overwrites the pixel at (x, y) to the current color.
	 */
	public void setPixel(int x, int y) {
		setPixel(x, y, cc);
	}
	
	public void setPixel(int x, int y, int value) {
		setPixelAtIndex(x + y * w, value);
	}
	
	protected void setPixelAtIndex(int i, int value) {
		buffer[i] = value;
	}
	
	/**
	 * interesting unsigned-byte-ish multiply
	 * integer equivalent of
	 * 
	 * C = A * B where A, B are both in the range [0, 1]
	 * 
	 * but instead it's in the range [0, 255]
	 * 
	 * Examples:
	 * mult(255, 255) = 255 // 1.0 * 1.0 = 1.0
	 * mult(255, 128) = 128 // 1.0 * 0.5 = 0.5
	 * mult(128, 128) = 64 // 0.5 * 0.5 = 0.25
	 */
	private int mult(int a, int b) {
		a = b * a + 0x80;
		return (a >> 8) + a >> 8;
	}
	
	// clamps to 255
	private int clamp(int x) {
		x -= 0xFF;
		x &= x >> 8;
		x += 0xFF;
		return x;
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		//* insert brensenham here *
	}
	
	public void drawRect(int x, int y, int w, int h) {
		//* do clipping *
		drawLine(x, y, x + w, y); // top side
		drawLine(x, y + h, x + w, y + h); // bottom side
		drawLine(x, y + 1, x, y + h - 1); // left side
		drawLine(x + w, y + 1, x + w, y + h - 1); // right side
	}
	
	public void fillRect(int x, int y, int w, int h) {
		//* do clipping *
		
		for (; y < y + h; y++) {
			drawLine(x, y, x + w, y);
		}
	}
	
	/**
	 * Clears the specified region to the current background color.
	 * Clips automatically.
	 */
	public void clearRect(int x, int y, int w, int h) {
		// clip to edges
		final int nx = MathUtils.clamp(x, this.w, 0);
		final int len = MathUtils.clamp(w, this.w - (nx - x), 0);
		final int mh = MathUtils.clamp(y + h, this.h, 0);
		final int bg = ColorUtils.pack(bgr, bgg, bgb, bga);
		for (; y < mh; y++) {
			final int np = this.w * y + nx;
			Arrays.fill(buffer, np, np + len, bg); // might replace this with tmpBuffer + arraycopy
		}
	}
	
	/**
	 * Copies the specified region of source to the same region of this image.
	 * Clips automatically.
	 * Requires that source be of type INT_ARGB.
	 */
	// needs testing to verify the clipping is correct
	public void copyRect(BufferedImage source, int x, int y, int w, int h) {
		if (source.getType() != BufferedImage.TYPE_INT_ARGB)
			throw new IllegalArgumentException("Images to be copied must be of type INT_ARGB");
		
		final int[] srcBuffer = ((DataBufferInt) source.getRaster().getDataBuffer()).getData();
		// clip to edges
		final int nx = MathUtils.clamp(x, Math.min(this.w, source.getWidth()), 0);
		final int len = MathUtils.clamp(w, Math.min(this.w - (nx - x), source.getWidth() - (nx - x)), 0);
		final int mh = MathUtils.clamp(y + h, Math.min(this.h, source.getHeight()), 0);
		for (; y < mh; y++) {
			System.arraycopy(srcBuffer, source.getWidth() * y + nx, buffer, this.w * y + nx, len);
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
