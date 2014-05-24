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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.tools.Tool;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import com.alee.laf.button.WebToggleButton;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;

public class ToolBox
{
	private final static int BUTTON_SIZE = 32;
	
	//private JDialog dialog;
	private WebToolBar toolbar;
	private ButtonGroup buttonGroup;
	
	public ToolBox()
	{
		toolbar = new WebToolBar("Toolbox");
		toolbar.setRollover(true);
		toolbar.setOrientation(WebToolBar.VERTICAL);
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
	
	public static class ToolBoxButton extends WebToggleButton
	{
		private static final long serialVersionUID = -7985116966168623216L;
		private Tool tool;
		
		public ToolBoxButton(String name, Tool tool)
		{
			super();
			
			setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			setMargin(new Insets(0, 0, 0, 0));
			setToolTipText(name);
			setFocusable(false);
			
			this.tool = tool;
			// TRY to load the icon!
			try
			{
				URL url =
						this.getClass().getResource(
								"/heroesgrave/paint/res/icons/tools/" + name
										+ ".png");
				
				if(url != null)
				{
					this.setIcon(new ImageIcon(ImageIO.read(url)));
				}
				else
				{
					this.setIcon(new ImageIcon(ImageIO
							.read(Paint.questionMarkURL)));
				}
				
			}
			catch(IOException e1)
			{
				System.err.println("Error: Tool '" + name
						+ "' is missing an icon!");
			}
			
			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Paint.setTool(((ToolBoxButton) e.getSource()).getTool());
				}
			});
		}
		
		public Tool getTool()
		{
			return tool;
		}
	}
	
	public void setSelected(Tool tool)
	{
		Enumeration<AbstractButton> e = buttonGroup.getElements();
		
		while(e.hasMoreElements())
		{
			ToolBoxButton b = (ToolBoxButton) e.nextElement();
			if(b.tool == tool)
			{
				b.setSelected(true);
				return;
			}
		}
	}
}