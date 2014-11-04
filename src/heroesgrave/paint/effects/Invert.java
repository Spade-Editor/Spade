// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.effects;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.edit.InvertChange;

public class Invert extends Effect
{
	// InvertChange takes no data and so can be static.
	private static final InvertChange instance = new InvertChange();
	
	public Invert(String name)
	{
		super(name);
	}
	
	public void perform(Layer layer)
	{
		layer.addChange(instance);
	}
}
