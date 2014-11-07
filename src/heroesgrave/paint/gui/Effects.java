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

package heroesgrave.paint.gui;

import heroesgrave.paint.editing.Effect;
import heroesgrave.paint.editing.ResizeCanvas;
import heroesgrave.paint.editing.ResizeImage;
import heroesgrave.paint.gui.ToolMenu.EffectMenuItem;

import com.alee.laf.menu.WebMenu;

public class Effects
{
	public WebMenu effects, generators, operations;
	
	public void addEffect(Effect effect, String shortcut)
	{
		effects.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addGenerator(Effect effect, String shortcut)
	{
		generators.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addOperation(Effect effect, String shortcut)
	{
		operations.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void init()
	{
		addOperation(new ResizeImage("Resize Image"), "R");
		addOperation(new ResizeCanvas("Resize Canvas"), "C");
	}
}
