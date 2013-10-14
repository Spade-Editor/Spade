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

package heroesgrave.paint.tools;

import heroesgrave.paint.main.Paint;

public class MoveSelection extends Tool
{
	private int lx, ly;
	
	public MoveSelection(String name)
	{
		super(name);
	}

	public void onPressed(int x, int y)
	{
		lx = x;
		ly = y;
		Paint.main.selection.startFloating();
	}

	public void onReleased(int x, int y)
	{
		lx = x;
		ly = y;
	}

	public void whilePressed(int x, int y)
	{
		Paint.main.selection.translate(x-lx, y-ly);
		lx = x;
		ly = y;
	}

	@Override
	public void whileReleased(int x, int y)
	{
	}
}