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
import java.awt.image.BufferedImage;

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

	public void setColour(int colour)
	{
		this.colour.setColour(colour);
	}

	public static class ColourPanel extends JPanel
	{
		private static final long serialVersionUID = 7541204326016173356L;

		private BufferedImage image;

		public ColourPanel()
		{

		}

		public void setColour(int colour)
		{
			if(image == null)
			{
				image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			}
			Graphics g = image.getGraphics();
			g.setColor(new Color(colour));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			this.repaint();
		}

		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawImage(image, 0, 0, null);
		}
	}
}
