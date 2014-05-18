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

import java.awt.Color;

/**
 * Class representing a color pallet.
 * 
 * 
 * @author BurntPizza
 * 
 */
public class Pallet {
	
	public int[] colors;
	
	/**
	 * Creates default pallet
	 */
	public static Pallet defaultPallet() {
		
		Pallet p = new Pallet();
		
		p.colors = new int[6 * 16]; // 6 rows of 16
		
		p.colors[0 + 0 * 16] = packf(0, 0, 0, 1);
		p.colors[1 + 0 * 16] = packf(.125, .125, .125, 1);
		
		p.colors[0 + 1 * 16] = packf(1, 1, 1, 1);
		p.colors[1 + 1 * 16] = packf(.25, .25, .25, 1);
		
		p.colors[0 + 2 * 16] = packf(.5, .5, .5, 1);
		p.colors[1 + 2 * 16] = packf(.75, .75, .75, 1);
		
		// gen other colors
		for (int y = 0; y < 3; y++)
			for (int x = 2; x < 16; x++) {
				double h = (x - 2) / 14.;
				double s = y == 2 ? .5 : 1;
				double v = y == 1 ? .5 : 1;
				p.colors[x + y * 16] = toARGB(h, s, v, 1);
			}
		
		// copy to bottom 3 rows, changing alpha
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 16; x++)
				p.colors[x + y * 16 + 3 * 16] = (p.colors[x + y * 16] & 0x00FFFFFF) | ((192 - y * 48) << 24);
		
		return p;
		
	}
	
	public Color[] toColorArray() {
		Color[] c = new Color[colors.length];
		
		for(int i=0;i<c.length;i++) {
			int color = colors[i];
			
			int a = (color >> 24) & 0xFF;
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = (color >> 0) & 0xFF;
			
			c[i] = new Color(r,g,b,a);
		}
		
		return c;
	}
	
	private static int pack(int r, int g, int b, int a) {
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}
	
	private static int packf(double r, double g, double b, double a) {
		return ((((int) (a * 255)) & 0xFF) << 24) | ((((int) (r * 255)) & 0xFF) << 16) | ((((int) (g * 255)) & 0xFF) << 8) | (((int) (b * 255)) & 0xFF);
	}
	
	private static int toARGB(double h, double s, double v, double a) {
		
		double C = v * s;
		double H = h * 6;
		double X = C * (1 - Math.abs(H % 2 - 1));
		
		double ri = 0, gi = 0, bi = 0;
		
		if (H < 1) {
			ri = C;
			gi = X;
		} else if (H < 2) {
			ri = X;
			gi = C;
		} else if (H < 3) {
			gi = C;
			bi = X;
		} else if (H < 4) {
			gi = X;
			bi = C;
		} else if (H < 5) {
			ri = X;
			bi = C;
		} else {
			ri = C;
			bi = X;
		}
		
		double m = v - C;
		
		int r = (int) ((ri + m) * 255);
		int g = (int) ((gi + m) * 255);
		int b = (int) ((bi + m) * 255);
		
		return pack(r, g, b, (int) (a * 255));
	}
	
	private static long toHSVA(double r, double g, double b, double a) {
		
		double M = Math.max(r, Math.max(g, b));
		double m = Math.min(r, Math.min(g, b));
		double delta = M - m;
		
		double h, s, v;
		
		s = delta / (M + 1e-20);
		v = M;
		
		double hue;
		if (r == M)
			hue = (g - b) / (delta + 1e-20);
		else if (g == M)
			hue = 2 + (b - r) / (delta + 1e-20);
		else
			hue = 4 + (r - g) / (delta + 1e-20);
		if (hue < 0)
			hue += 6;
		h = hue * (1. / 6);
		
		long l = (((int) (h * 360)) << 24) | (((int) (s * 255)) << 16) | (((int) (v * 255)) << 8) | ((int) (a * 255));
		
		return l;
	}
}
