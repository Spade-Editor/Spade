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

package heroesgrave.utils.misc;

import java.util.Random;

public class RandomUtils
{
	private static final Random r = new Random();
	
	public static int rInt(int max)
	{
		return rInt(r, max);
	}
	
	public static int rInt(Random r, int max)
	{
		return r.nextInt(max);
	}
	
	public static int rInt(int min, int max)
	{
		return rInt(r, min, max);
	}
	
	public static int rInt(Random r, int min, int max)
	{
		return r.nextInt(max - min + 1) + min;
	}
}