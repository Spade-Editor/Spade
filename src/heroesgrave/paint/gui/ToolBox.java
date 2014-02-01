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

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.tools.Brush;
import heroesgrave.paint.tools.Ellipse;
import heroesgrave.paint.tools.Eraser;
import heroesgrave.paint.tools.Fill;
import heroesgrave.paint.tools.Line;
import heroesgrave.paint.tools.Move;
import heroesgrave.paint.tools.Picker;
import heroesgrave.paint.tools.Pixel;
import heroesgrave.paint.tools.Rectangle;
import heroesgrave.paint.tools.SelectTool;
import heroesgrave.paint.tools.Tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ToolBox
{
	
	private final int BUTTON_SIZE = 32;
	
	public static Tool DEF;
	
	private JDialog dialog;
	private JPanel panel;
	private ButtonGroup buttonGroup;
	
	public ToolBox(JFrame mainFrame)
	{
		dialog = new CentredJDialog(mainFrame, "Tools");
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setFocusable(false);
		dialog.setAutoRequestFocus(false);
		
		panel = new JPanel();
		
		int gridRows = 2;
		int gridColumns = 5;
		panel.setLayout(new GridLayout(gridRows, gridColumns));
		
		buttonGroup = new ButtonGroup();
		
		DEF = new Pixel("Pencil");
		Paint.main.currentTool = DEF;
		
		addButton(new ToolBoxButton("Pencil", DEF), true);
		addButton(new ToolBoxButton("Colour Picker", new Picker("Colour Picker")));
		addButton(new ToolBoxButton("Brush", new Brush("Brush")));
		addButton(new ToolBoxButton("Paint Bucket", new Fill("Paint Bucket")));
		addButton(new ToolBoxButton("Eraser", new Eraser("Eraser")));
		addButton(new ToolBoxButton("Select", new SelectTool("Select")));
		addButton(new ToolBoxButton("Move", new Move("Move")));
		addButton(new ToolBoxButton("Line", new Line("Straight Line")));
		addButton(new ToolBoxButton("Rectangle", new Rectangle("Rectangle")));
		addButton(new ToolBoxButton("Ellipse", new Ellipse("Ellipse")));
		
		// backgroundPanel used to avoid button resizing and fit to BUTTON_SIZE
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.add(panel);
		dialog.add(backgroundPanel, BorderLayout.CENTER);
		
		dialog.pack();
	}
	
	public JDialog getDialog()
	{
		return dialog;
	}
	
	private void addButton(ToolBoxButton button, boolean... selected)
	{
		if(selected != null && selected.length > 0)
		{
			button.setSelected(selected[0]);
		}
		buttonGroup.add(button);
		panel.add(button);
	}
	
	public void show()
	{
		dialog.setVisible(true);
	}
	
	public void hide()
	{
		dialog.setVisible(false);
	}
	
	public void toggle()
	{
		dialog.setVisible(!dialog.isVisible());
	}
	
	public void dispose()
	{
		dialog.dispose();
	}
	
	public boolean isVisible()
	{
		return dialog.isVisible();
	}
	
	private class ToolBoxButton extends JToggleButton
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
				URL url = this.getClass().getResource("/heroesgrave/paint/res/icons/tools/" + name + ".png");
				
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
					Paint.setTool(((ToolBoxButton) e.getSource()).getTool());
				}
			});
		}
		
		public Tool getTool()
		{
			return tool;
		}
	}
}