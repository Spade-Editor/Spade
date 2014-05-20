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
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class MutableColor extends Color {
	
	private int value;
	
	public MutableColor() {
		this(0, 0, 0);
	}
	
	public MutableColor(int r, int g, int b) {
		this(r, g, b, 255);
	}
	
	public MutableColor(int r, int g, int b, int a) {
		super(r, g, b, a);
		setColor(r, g, b, a);
	}
	
	public MutableColor setColor(int r, int g, int b) {
		setColor(r, g, b, 255);
		return this;
	}
	
	public MutableColor setColor(int r, int g, int b, int a) {
		value = pack(r, g, b, a);
		return this;
	}
	
	public MutableColor setColor(int argb) {
		value = argb;
		return this;
	}
	
	public int getRed() {
		return (getRGB() >> 16) & 0xFF;
	}
	
	public int getGreen() {
		return (getRGB() >> 8) & 0xFF;
	}
	
	public int getBlue() {
		return (getRGB() >> 0) & 0xFF;
	}
	
	public int getAlpha() {
		return (getRGB() >> 24) & 0xff;
	}
	
	public int getRGB() {
		return value;
	}
}
