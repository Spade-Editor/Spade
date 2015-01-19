// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.image.blend;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Set;

public abstract class BlendMode implements Composite, CompositeContext
{
	private static HashMap<String, BlendMode> modes = new HashMap<String, BlendMode>();
	public static final BlendMode NORMAL = new Normal();
	
	static
	{
		addBlendMode(NORMAL);
	}
	
	public final String name;
	
	public BlendMode(String name)
	{
		this.name = name;
	}
	
	public abstract void compose(Raster src, Raster dst, WritableRaster out);
	
	public void dispose()
	{
		
	}
	
	public CompositeContext createContext(ColorModel arg0, ColorModel arg1, RenderingHints arg2)
	{
		return this;
	}
	
	public String toString()
	{
		return name;
	}
	
	public static void addBlendMode(BlendMode mode)
	{
		modes.put(mode.name, mode);
	}
	
	public static BlendMode getBlendMode(String s)
	{
		return modes.get(s);
	}
	
	public static Set<String> getBlendModeNames()
	{
		return modes.keySet();
	}
}
