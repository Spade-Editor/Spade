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

package heroesgrave.spade.gui.menus;

import heroesgrave.spade.editing.Effect;
import heroesgrave.spade.main.Spade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.alee.laf.menu.WebMenuItem;
import com.alee.managers.hotkey.HotkeyData;

@SuppressWarnings("serial")
public class EffectMenuItem extends WebMenuItem
{
	private Effect effect;
	
	public EffectMenuItem(String name, Effect e, Character key)
	{
		this(name, e, key, null);
	}
	
	public EffectMenuItem(String name, Effect e, Character key, String toolTip)
	{
		super(name);
		
		// This is here, so some ImageOps don't have to have a key assigned. We can't have key-code's for ALL the ImageOp's! It's impossible!
		if(key != null)
		{
			HotkeyData shortcut = new HotkeyData(true, false, true, KeyEvent.getExtendedKeyCodeForChar(Character.toLowerCase(key)));
			super.setAccelerator(shortcut);
			Spade.addEffect(key, e);
		}
		
		// If there is a ToolTip Text given over the Constructor, use it.
		if(toolTip != null)
		{
			this.setToolTipText(toolTip);
		}
		
		this.effect = e;
		
		this.setIcon(effect.getIcon());
		
		this.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(Spade.getDocument() != null)
				{
					effect.perform(Spade.getDocument().getCurrent());
					Spade.main.gui.repaint();
				}
			}
		});
	}
}
