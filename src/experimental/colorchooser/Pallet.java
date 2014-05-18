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

import static experimental.colorchooser.ColorUtils.*;

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
}
