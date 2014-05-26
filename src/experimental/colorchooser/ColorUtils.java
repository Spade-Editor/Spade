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

/**
 * @author BurntPizza
 * 
 */
public enum ColorUtils {
	
	;
	
	/**
	 * 
	 */
	public static int pack(int r, int g, int b, int a) {
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}
	
	/**
	 * 
	 */
	public static int packf(double r, double g, double b, double a) {
		return ((((int) (a * 255)) & 0xFF) << 24) | ((((int) (r * 255)) & 0xFF) << 16) | ((((int) (g * 255)) & 0xFF) << 8) | (((int) (b * 255)) & 0xFF);
	}
	
	/**
	 * 
	 */
	public static int toARGB(double h, double s, double v, double a) {
		
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
	
	/**
	 * 
	 */
	public static long toHSVA(double r, double g, double b, double a) {
		
		double M = Math.max(r, Math.max(g, b));
		double m = Math.min(r, Math.min(g, b));
		double delta = M - m;
		
		double h, s, v;
		
		s = delta / (M + 1e-20);
		v = M;
		
		double hue = 6;
		if (r == M)
			hue = ((g - b) / (delta + 1e-20)) % 6.;
		else if (g == M)
			hue = 2 + (b - r) / (delta + 1e-20);
		else
			hue = 4 + (r - g) / (delta + 1e-20);
		
		h = hue / 6;
		
		long l = (((long) (h * 1024)) << 32) | (((int) (s * 255)) << 16) | (((int) (v * 255)) << 8) | ((int) (a * 255));
		
		return l;
	}
}
