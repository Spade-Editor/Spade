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

import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.main.Spade;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.alee.laf.button.WebToggleButton;
import com.alee.laf.menu.WebMenuItem;
import com.alee.managers.popup.WebPopup;

@SuppressWarnings("serial")
public class ToolBoxButton extends WebToggleButton
{
	private Tool tool;
	
	private WebPopup menu;
	
	public ToolBoxButton(String name, Tool tool)
	{
		super();
		setPreferredSize(new Dimension(ToolBox.BUTTON_SIZE, ToolBox.BUTTON_SIZE));
		setMargin(new Insets(0, 0, 0, 0));
		setToolTipText(name);
		setFocusable(false);
		
		menu = tool.createOptions();
		
		this.tool = tool;
		// TRY to load the icon!
		try
		{
			URL url = tool.getClass().getResource("/res/icons/tools/" + name + ".png");
			
			if(url != null)
			{
				ImageIcon icon = new ImageIcon(ImageIO.read(url));
				this.setIcon(icon);
				((WebMenuItem) menu.getComponent(0)).setIcon(icon);
			}
			else
			{
				ImageIcon icon = new ImageIcon(ImageIO.read(Spade.questionMarkURL));
				this.setIcon(icon);
				((WebMenuItem) menu.getComponent(0)).setIcon(icon);
			}
			
		}
		catch(IOException e1)
		{
			System.err.println("Error: Tool '" + name + "' is missing an icon!");
		}
		
		final ToolBoxButton self = this;
		this.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(!Spade.setTool(self.getTool()))
				{
					if(!menu.isShowing())
					{
						menu.packPopup();
						menu.showAsPopupMenu(self);
					}
					else
					{
						menu.hidePopup();
						Spade.main.gui.frame.requestFocus();
					}
				}
			}
		});
		
	}
	
	public Tool getTool()
	{
		return tool;
	}
}
