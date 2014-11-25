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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.alee.laf.menu.WebMenuItem;

@SuppressWarnings("serial")
public class ToolMenuItem extends WebMenuItem
{
	private Tool tool;
	
	public ToolMenuItem(String name, Tool t, Character key)
	{
		super(key == null ? (name) : (name + " (" + key + ")"));
		
		// This is here, so some Tools don't have to have a key assigned. We can't have key-code's for ALL the Tools! It's impossible!
		if(key != null)
		{
			Spade.addTool(key, t);
		}
		
		this.tool = t;
		
		// TRY to load the icon!
		try
		{
			URL url = tool.getClass().getResource("/res/icons/tools/" + name + ".png");
			
			if(url != null)
			{
				this.setIcon(new ImageIcon(ImageIO.read(url)));
			}
			else
			{
				this.setIcon(new ImageIcon(ImageIO.read(Spade.questionMarkURL)));
			}
			
		}
		catch(IOException e1)
		{
			System.err.println("Error: Tool '" + name + "' is missing an icon!");
		}
		
		this.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Spade.setTool(tool);
			}
		});
	}
}
