// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
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

package heroesgrave.spade.gui;

import heroesgrave.spade.editing.Effect;

import com.alee.laf.menu.WebMenu;

public class Effects
{
	public WebMenu effects, generators, operations;
	
	public void addEffect(Effect effect, Character shortcut)
	{
		effects.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addGenerator(Effect effect, Character shortcut)
	{
		generators.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addOperation(Effect effect, Character shortcut)
	{
		operations.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void init()
	{
		
	}
}
