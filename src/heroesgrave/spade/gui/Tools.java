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

package heroesgrave.spade.gui;

import heroesgrave.spade.editing.Pencil;
import heroesgrave.spade.editing.Picker;
import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.gui.dialogs.ToolBox;
import heroesgrave.spade.gui.menus.ToolBoxButton;
import heroesgrave.spade.gui.menus.ToolMenuItem;
import heroesgrave.spade.main.Spade;

import javax.swing.JMenu;

public class Tools
{
	public static Tool DEF;
	
	public JMenu toolsMenu;
	public ToolBox toolbox;
	
	public void addTool(Tool tool, Character shortcut)
	{
		toolbox.addButton(new ToolBoxButton(tool.name, tool));
		toolsMenu.add(new ToolMenuItem(tool.name, tool, shortcut));
	}
	
	public void init()
	{
		DEF = new Pencil("Pencil");
		Spade.main.currentTool = DEF;
		addTool(DEF, 'P');
		addTool(new Picker("Colour Picker"), 'K');
	}
}
