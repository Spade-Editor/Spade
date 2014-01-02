/*
 *	Copyright 2013 HeroesGrave
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

public class BlendMode
{
	public static final BlendMode REPLACE = new BlendMode("Replace");
	public static final BlendMode NORMAL = new Normal();
	
	public final String name;
	
	public BlendMode()
	{
		this("Unnamed Blend Mode");
	}
	
	public BlendMode(String name)
	{
		this.name = name;
	}
	
	public int blend(int src, int dst)
	{
		return src;
	}
	
	public static int getRed(int colour)
	{
		return (colour >> 16) & 0xff;
	}
	
	public static int getGreen(int colour)
	{
		return (colour >> 8) & 0xff;
	}
	
	public static int getBlue(int colour)
	{
		return colour & 0xff;
	}
	
	public static int getAlpha(int colour)
	{
		return (colour >> 24) & 0xff;
	}
	
	public static float getRedf(int colour)
	{
		return ((colour >> 16) & 0xff) / 255f;
	}
	
	public static float getGreenf(int colour)
	{
		return ((colour >> 8) & 0xff) / 255f;
	}
	
	public static float getBluef(int colour)
	{
		return (colour & 0xff) / 255f;
	}
	
	public static float getAlphaf(int colour)
	{
		return ((colour >> 24) & 0xff) / 255f;
	}
	
	public static int multA(int alpha, int f)
	{
		return (alpha * f) / 255;
	}
	
	public static int multC(int colour, int f)
	{
		return ((((((colour >> 16) & 0xFF) * f) / 255) & 0xFF) << 16) | ((((((colour >> 8) & 0xFF) * f) / 255) & 0xFF) << 8)
				| ((((colour & 0xFF) * f) / 255) & 0xFF);
	}
	
	public static int getColour(int colour)
	{
		return colour & 0xffffff;
	}
	
	public static int mix(int alpha, int colour)
	{
		return ((alpha & 0xff) << 24) | (colour & 0xffffff);
	}
	
	public static int mix(int r, int g, int b)
	{
		return ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	}
	
	public static int mix(int a, int r, int g, int b)
	{
		return ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	}
}