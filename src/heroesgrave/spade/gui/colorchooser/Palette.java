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

package heroesgrave.spade.gui.colorchooser;

/**
 * Class representing a color palette.
 * 
 * 
 * @author BurntPizza, HeroesGrave
 * 
 */
public class Palette
{
	public int[] minimized = new int[8 * 3]; // (24) Third
	public int[] standard = new int[12 * 3]; // (36) Half
	public int[] extended = new int[12 * 6]; // (72) Full
	
	/**
	 * Creates default pallet
	 */
	public static Palette defaultPallet()
	{
		Palette p = new Palette();
		
		int i = 0; // Minimized
		int j = 0; // Standard
		int k = p.standard.length; // Extended
		p.standard[j++] = p.minimized[i++] = 0xffffffff; // White
		p.standard[j++] = 0xffbfbfbf; // Light Grey
		p.standard[j++] = p.minimized[i++] = 0xffff7f7f; // Pale Red
		p.standard[j++] = 0xffffaf7f; // Pale Orange
		p.standard[j++] = p.minimized[i++] = 0xffffdf7f; // Pale Yellow
		p.standard[j++] = 0xffbfff7f; // Pale Lime
		p.standard[j++] = p.minimized[i++] = 0xff7fff9f; // Pale Green
		p.standard[j++] = p.minimized[i++] = 0xff7fffff; // Pale Cyan
		p.standard[j++] = 0xff7fcfff; // Pale Blue
		p.standard[j++] = p.minimized[i++] = 0xff7f9fff; // Pale Indigo
		p.standard[j++] = p.minimized[i++] = 0xffbf7fff; // Pale Purple
		p.standard[j++] = p.minimized[i++] = 0xffff7fdf; // Pale Magenta
		
		p.standard[j++] = p.minimized[i++] = 0xff7f7f7f; // Grey
		p.standard[j++] = 0xff5f5f5f; // Medium-Dark Grey
		p.standard[j++] = p.minimized[i++] = 0xffff0000; // Red
		p.standard[j++] = 0xffff5f00; // Orange
		p.standard[j++] = p.minimized[i++] = 0xffffbf00; // Yellow
		p.standard[j++] = 0xff7fff00; // Lime
		p.standard[j++] = p.minimized[i++] = 0xff00ff3f; // Green
		p.standard[j++] = p.minimized[i++] = 0xff00ffff; // Cyan
		p.standard[j++] = 0xff009fff; // Blue
		p.standard[j++] = p.minimized[i++] = 0xff003fff; // Indigo
		p.standard[j++] = p.minimized[i++] = 0xff7f00ff; // Purple
		p.standard[j++] = p.minimized[i++] = 0xffff00bf; // Magenta
		
		p.standard[j++] = 0xff3f3f3f; // Dark Grey
		p.standard[j++] = p.minimized[i++] = 0xff000000; // Black
		p.standard[j++] = p.minimized[i++] = 0xff7f0000; // Dark Red
		p.standard[j++] = 0xff7f2f00; // Dark Orange
		p.standard[j++] = p.minimized[i++] = 0xff7f5f00; // Dark Yellow
		p.standard[j++] = 0xff3f7f00; // Dark Lime
		p.standard[j++] = p.minimized[i++] = 0xff007f1f; // Dark Green
		p.standard[j++] = p.minimized[i++] = 0xff007f7f; // Dark Cyan
		p.standard[j++] = 0xff004f7f; // Dark Blue
		p.standard[j++] = p.minimized[i++] = 0xff001f7f; // Dark Indigo
		p.standard[j++] = p.minimized[i++] = 0xff3f007f; // Dark Purple
		p.standard[j++] = p.minimized[i++] = 0xff7f005f; // Dark Magenta
		
		System.arraycopy(p.standard, 0, p.extended, 0, p.standard.length);
		
		p.extended[01] = 0xffdfdfdf;
		p.extended[12] = 0xffbfbfbf;
		p.extended[13] = 0xff9f9f9f;
		p.extended[24] = 0xff7f7f7f;
		p.extended[25] = 0xff5f5f5f;
		p.extended[36] = 0xff3f3f3f;
		p.extended[37] = 0xff000000;
		
		k += 2;
		
		p.extended[k++] = 0xff7f3f3f; // Dull Red
		p.extended[k++] = 0xff7f573f; // Dull Orange
		p.extended[k++] = 0xff7f6f3f; // Dull Yellow
		p.extended[k++] = 0xff5f7f3f; // Dull Lime
		p.extended[k++] = 0xff3f7f4f; // Dull Green
		p.extended[k++] = 0xff3f7f7f; // Dull Cyan
		p.extended[k++] = 0xff3f677f; // Dull Blue
		p.extended[k++] = 0xff3f4f7f; // Dull Indigo
		p.extended[k++] = 0xff5f3f7f; // Dull Purple
		p.extended[k++] = 0xff7f3f6f; // Dull Magenta
		
		p.extended[k++] = 0x7fffffff; // Translucent White
		p.extended[k++] = 0x7f7f7f7f; // Translucent Grey
		
		for(i = 0; i < 10; i++)
		{
			p.extended[i + 50] = p.standard[i + 14] ^ 0x80000000;
		}
		k += 10;
		
		p.extended[k++] = 0x7f3f3f3f; // Translucent Dark Grey
		p.extended[k++] = 0x7f000000; // Translucent Black
		
		for(i = 0; i < 10; i++)
		{
			p.extended[i + 62] = p.standard[i + 26] ^ 0x80000000;
		}
		k += 10;
		
		return p;
	}
}
