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
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.main.Paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.alee.laf.menu.WebMenuItem;

public class ToolMenu
{
	@SuppressWarnings("serial")
	public static class ToolMenuItem extends WebMenuItem
	{
		private Tool tool;
		
		public ToolMenuItem(String name, Tool t, Character key)
		{
			super(key == null ? (name) : (name + " (" + key + ")"));
			
			// This is here, so some Tools don't have to have a key assigned. We can't have key-code's for ALL the Tools! It's impossible!
			if(key != null)
			{
				Paint.addTool(key, t);
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
					this.setIcon(new ImageIcon(ImageIO.read(Paint.questionMarkURL)));
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
					Paint.setTool(tool);
				}
			});
		}
	}
	
	@SuppressWarnings("serial")
	public static class EffectMenuItem extends WebMenuItem
	{
		private Effect effect;
		
		public EffectMenuItem(String name, Effect e, Character key)
		{
			this(name, e, key, null);
		}
		
		public EffectMenuItem(String name, Effect e, Character key, String toolTip)
		{
			super(key == null ? (name) : (name + " (Ctrl+Shift+" + key + ")"));
			
			// This is here, so some ImageOps don't have to have a key assigned. We can't have key-code's for ALL the ImageOp's! It's impossible!
			if(key != null)
			{
				Paint.addEffect(key, e);
			}
			
			// If there is a ToolTip Text given over the Constructor, use it.
			if(toolTip != null)
			{
				this.setToolTipText(toolTip);
			}
			
			// 
			this.effect = e;
			
			// TRY to load the icon!
			try
			{
				URL url = effect.getClass().getResource("/res/icons/effects/" + name + ".png");
				
				if(url != null)
				{
					this.setIcon(new ImageIcon(ImageIO.read(url)));
				}
				else
				{
					this.setIcon(new ImageIcon(ImageIO.read(Paint.questionMarkURL)));
				}
			}
			catch(IOException e1)
			{
				System.err.println("Error: Effect '" + name + "' is missing an icon!");
			}
			
			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(Paint.getDocument() != null)
					{
						effect.perform(Paint.getDocument().getCurrent());
						Paint.main.gui.repaint();
					}
				}
			});
		}
	}
}
