/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.paint.image.blend;

import java.awt.AlphaComposite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Normal extends BlendMode
{
	public Normal()
	{
		super("Normal");
	}
	
	public CompositeContext createContext(ColorModel arg0, ColorModel arg1, RenderingHints arg2)
	{
		return AlphaComposite.SrcOver.createContext(arg0, arg1, arg2);
	}
	
	@Override
	public void compose(Raster src, Raster dst, WritableRaster out)
	{
		
	}
}