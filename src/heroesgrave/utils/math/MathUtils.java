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

package heroesgrave.utils.math;

public class MathUtils
{
	private static final int INT = 16384;
	private static final double FLOOR = INT + 0.0000;
	private static final double CEIL = INT + 0.9999;
	private static final double ROUND = INT + 0.5000;
	
	public static final int floor(double d)
	{
		return (int) (d + FLOOR) - INT;
	}
	
	public static final int ceil(double d)
	{
		return (int) (d + CEIL) - INT;
	}
	
	public static final int round(double d)
	{
		return (int) (d + ROUND) - INT;
	}
	
	public static final int roundToPowerOf(int i, int powerOf)
	{
		int j = 0;
		while(true)
		{
			j++;
			int k = pow(powerOf, j);
			if(k >= i)
				return k;
		}
	}
	
	public static final int pow(int i, int pow)
	{
		if(pow == 0 || i == 0)
			return 1;
		int r = i;
		if(pow > 0)
		{
			for(int j = 0; j < pow; j++)
			{
				r *= i;
			}
		}
		else
		{
			for(int j = 0; j < -pow; j++)
			{
				r /= i;
			}
		}
		return r;
	}
	
	public static final double clamp(double d, double max, double min)
	{
		if(d >= max)
			return max;
		if(d <= min)
			return min;
		return d;
	}
	
	public static final int clamp(int i, int max, int min)
	{
		if(i >= max)
			return max;
		if(i <= min)
			return min;
		return i;
	}
	
	public static final double round(double d, int dp)
	{
		double mult = Math.pow(10, dp);
		d *= mult;
		int i = MathUtils.round(d);
		return i / mult;
	}
	
	public static final int divideAndFloor(double a, double b)
	{
		return floor(a / b);
	}
	
	public static float interp(float x, float y)
	{
		return (x - y) / 2F + y;
	}
	
	public static float difference(float x, float y)
	{
		return Math.abs(x - y);
	}
}