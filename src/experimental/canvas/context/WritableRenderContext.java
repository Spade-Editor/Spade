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

import java.awt.image.BufferedImage;

/**
 * Interface representing the graphical context of the Canvas.
 * 
 * This interface in particular represents a context that is write-only.
 * It specifies common drawing primitives used to render graphics.
 * 
 * @author BurntPizza
 * 
 */
public interface WritableRenderContext {
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void drawPixel(int x, int y);
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawLine(int x1, int y1, int x2, int y2);
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void drawRectangle(int x, int y, int w, int h);
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param r
	 */
	public void drawCircle(int x, int y, int r);
	
	/**
	 * 
	 * @param img
	 * @param x
	 * @param y
	 */
	public void drawImage(BufferedImage img);
}
