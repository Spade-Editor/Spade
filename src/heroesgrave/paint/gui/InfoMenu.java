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

import heroesgrave.paint.tools.Tool;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InfoMenu
{
	private JLabel scale, saved, tool;
	private ColourPanel colour;
	
	public JMenuBar createInfoMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.setLayout(new GridLayout(1, 0));
		
		scale = new JLabel("Scale: 100%");
		saved = new JLabel("Saved: Yes");
		tool = new JLabel("Tool: Pencil");
		scale.setHorizontalAlignment(SwingConstants.CENTER);
		saved.setHorizontalAlignment(SwingConstants.CENTER);
		tool.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.colour = new ColourPanel();
		
		menuBar.add(tool);
		menuBar.add(colour);
		menuBar.add(scale);
		menuBar.add(saved);
		
		return menuBar;
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
		this.colour.setLeftColour(colour);
	}
	
	public void setRightColour(int colour)
	{
		this.colour.setRightColour(colour);
	}
	
	public static class ColourPanel extends JPanel
	{
		private static final long serialVersionUID = 7541204326016173356L;
		
		private Color leftColour;
		private Color rightColour;
		
		public ColourPanel()
		{
			
		}
		
		public void setLeftColour(int colour)
		{
			this.leftColour = new Color(colour);
			this.repaint();
		}
		
		public void setRightColour(int colour)
		{
			this.rightColour = new Color(colour);
			this.repaint();
		}
		
		public int getLeftColour()
		{
			return (((leftColour.getAlpha() & 0xff) << 24) | ((leftColour.getRed() & 0xff) << 16) | ((leftColour.getGreen() & 0xff) << 8) | (leftColour
					.getBlue() & 0xff));
		}
		
		public int getRightColour()
		{
			return (((rightColour.getAlpha() & 0xff) << 24) | ((rightColour.getRed() & 0xff) << 16) | ((rightColour.getGreen() & 0xff) << 8) | (rightColour
					.getBlue() & 0xff));
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			g.setColor(leftColour);
			g.fillRect(0, 0, this.getWidth() / 2, this.getHeight());
			g.setColor(rightColour);
			g.fillRect(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
		}
	}
}