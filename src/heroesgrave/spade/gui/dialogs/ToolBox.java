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

package heroesgrave.spade.gui.dialogs;

import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.gui.menus.ToolBoxButton;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;

public class ToolBox
{
	public final static int BUTTON_SIZE = 32;
	
	//private JDialog dialog;
	private WebToolBar toolbar;
	private ButtonGroup buttonGroup;
	
	public ToolBox()
	{
		toolbar = new WebToolBar("Toolbox", WebToolBar.VERTICAL);
		toolbar.setRollover(true);
		toolbar.setFloatable(true);
		toolbar.setFocusable(false);
		toolbar.setToolbarStyle(ToolbarStyle.standalone);
		
		buttonGroup = new ButtonGroup();
	}
	
	public void addButton(ToolBoxButton button, boolean... selected)
	{
		if(selected != null && selected.length > 0)
		{
			button.setSelected(selected[0]);
		}
		buttonGroup.add(button);
		toolbar.add(button);
	}
	
	public WebToolBar getToolbar()
	{
		return toolbar;
	}
	
	public void setSelected(Tool tool)
	{
		Enumeration<AbstractButton> e = buttonGroup.getElements();
		
		while(e.hasMoreElements())
		{
			ToolBoxButton b = (ToolBoxButton) e.nextElement();
			if(b.getTool() == tool)
			{
				b.setSelected(true);
				return;
			}
		}
	}
}
