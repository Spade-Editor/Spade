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
package experimental.colorchooser.event;

import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import experimental.colorchooser.Channel;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorEvent extends EventObject {
	
	public final int r, g, b, a;
	public final List<Channel> changedChannels;
	
	public ColorEvent(Object source, int r, int g, int b, int a, Channel... changedChannels) {
		super(source);
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.changedChannels = Arrays.asList(changedChannels);
	}
}
