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

import heroesgrave.utils.math.MathUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ColourNumberFilter extends DocumentFilter
{
	public void insertString(FilterBypass fb, int off, String str, AttributeSet att) throws BadLocationException
	{
		int v = Integer.parseInt(str.replaceAll("\\D++", ""));
		v = MathUtils.clamp(v, 255, 0);
		fb.insertString(off, "" + v, att);
	}
	
	public void replace(FilterBypass fb, int off, int len, String str, AttributeSet att) throws BadLocationException
	{
		int v = Integer.parseInt(str.replaceAll("\\D++", ""));
		v = MathUtils.clamp(v, 255, 0);
		fb.replace(off, len, "" + v, att);
	}
}