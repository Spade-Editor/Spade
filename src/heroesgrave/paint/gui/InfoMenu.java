/*
 *	Copyright 2013 HeroesGrave
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
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This should be renamed into "InfoMenuBar". The current name is a -bit- confusing.
 **/
public class InfoMenu
{
	private JLabel scale, saved, tool;
	private JButton reset;
	private MemoryWatcher memoryWatcher;
	private ColourTextPanel left, right;
	
	public JMenuBar createInfoMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.setLayout(new GridLayout(1, 0));
		
		scale = new JLabel("Scale: 100%");
		saved = new JLabel("Saved: Yes");
		tool = new JLabel("Tool: Pencil");
		
		// Check if the MemoryWatcher should be activated.
		if(System.getProperty("DmemoryWatcherFlag") != null)
		{
			memoryWatcher = new MemoryWatcher();
		}
		
		scale.setHorizontalAlignment(SwingConstants.CENTER);
		saved.setHorizontalAlignment(SwingConstants.CENTER);
		tool.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel colourPanel = new JPanel();
		colourPanel.setLayout(new GridLayout(1, 2));
		
		left = new ColourTextPanel();
		right = new ColourTextPanel();
		reset = new JButton("Reset");
		
		// reset creation
		reset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.setLeftColour(0xFF000000);
				Paint.main.setRightColour(0xFFFFFFFF);
			}
		});
		try
		{
			URL url = this.getClass().getResource("/heroesgrave/paint/res/icons/reset.png");
			
			if(url != null)
			{
				reset.setIcon(new ImageIcon(ImageIO.read(url)));
			}
			else
				throw new IOException();
		}
		catch(IOException e1)
		{
			System.err.println("Error: 'Reset' is missing an icon!");
		}
		
		reset.setFocusable(false);
		
		colourPanel.add(reset);
		colourPanel.add(left);
		colourPanel.add(right);
		
		left.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				read(left);
				Paint.main.gui.frame.requestFocus();
			}
		});
		left.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent arg0)
			{
				read(left);
				Paint.main.gui.frame.requestFocus();
			}
		});
		
		right.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				read(right);
				Paint.main.gui.frame.requestFocus();
			}
		});
		right.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent arg0)
			{
				read(right);
				Paint.main.gui.frame.requestFocus();
			}
		});
		
		menuBar.add(tool);
		menuBar.add(colourPanel);
		menuBar.add(scale);
		menuBar.add(saved);
		
		if(memoryWatcher != null)
		{
			menuBar.add(memoryWatcher);
		}
		
		return menuBar;
	}
	
	private void read(ColourTextPanel panel)
	{
		String num = panel.getText();
		
		num = num.replaceAll("[^0-9a-fA-F]", "");
		
		if(num.length() > 6)
		{
			num = num.substring(0, 6);
		}
		
		panel.setText(num);
		
		int c;
		if(num.equals(""))
		{
			c = panel.colour.getRGB();
		}
		else
		{
			c = Integer.decode("0x" + num);
		}
		
		c |= 0xff000000;
		
		if(panel == left)
		{
			Paint.main.setLeftColour(c);
		}
		else if(panel == right)
		{
			Paint.main.setRightColour(c);
		}
	}
	
	public void setScale(float scale)
	{
		this.scale.setText("Scale: " + MathUtils.round(scale * 100) + "%");
	}
	
	public void setSaved(boolean saved)
	{
		this.saved.setText("Saved: " + (saved ? "Yes" : "No"));
	}
	
	public void setTool(Tool tool)
	{
		this.tool.setText("Tool: " + tool.name);
	}
	
	public void setLeftColour(int colour)
	{
		this.left.setColour(colour);
	}
	
	public void setRightColour(int colour)
	{
		this.right.setColour(colour);
	}
	
	public static class ColourTextPanel extends JTextField
	{
		private static final long serialVersionUID = -2244995065320398599L;
		
		public Color colour;
		
		public ColourTextPanel()
		{
			super();
			this.setEditable(true);
		}
		
		public void setColour(int colour)
		{
			this.colour = new Color(colour);
			this.setText(bufferZeros(Integer.toHexString(colour & 0xffffff)));
			this.setBackground(this.colour);
			this.setForeground(new Color(this.colour.getRGB() ^ 0x00ffffff));
			this.repaint();
		}
	}
	
	private static String bufferZeros(String colour)
	{
		while(colour.length() < 6)
		{
			colour = "0" + colour;
		}
		return colour;
	}
}