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

import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.imageops.Invert;
import heroesgrave.paint.imageops.Resize;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.tools.Ellipse;
import heroesgrave.paint.tools.Fill;
import heroesgrave.paint.tools.Line;
import heroesgrave.paint.tools.PaintBrush;
import heroesgrave.paint.tools.Picker;
import heroesgrave.paint.tools.Pixel;
import heroesgrave.paint.tools.Rectangle;
import heroesgrave.paint.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ToolMenu
{
	public static JMenu createToolMenu()
	{
		JMenu menu = new JMenu("Tools");

		Tool def = new Pixel("Pencil");
		Paint.main.currentTool = def;

		menu.add(new ToolMenuItem("Pencil", def, "P"));
		menu.add(new ToolMenuItem("Paint Brush", new PaintBrush("Paint Brush"), "B"));
		menu.add(new ToolMenuItem("Line", new Line("Straight Line"), "L"));
		menu.add(new ToolMenuItem("Colour Picker", new Picker("Colour Picker"), "K"));
		menu.add(new ToolMenuItem("Paint Bucket", new Fill("Paint Bucket"), "F"));
		menu.add(new ToolMenuItem("Rectangle", new Rectangle("Rectangle"), "R"));
		menu.add(new ToolMenuItem("Ellipse", new Ellipse("Ellipse"), "E"));
		
		heroesgrave.paint.plugin.PluginManager.instance.registerTools(menu);
		
		return menu;
	}

	public static JMenu createImageMenu()
	{
		JMenu menu = new JMenu("Image");

		menu.add(new ImageMenuItem("Resize Image", new Resize(), "R"));
		menu.add(new ImageMenuItem("Invert Colour", new Invert(), "I"));
		
		heroesgrave.paint.plugin.PluginManager.instance.registerImageOps(menu);
		
		return menu;
	}

	public static class ToolMenuItem extends JMenuItem
	{
		private static final long serialVersionUID = 5766656521451633454L;

		private Tool tool;

		public ToolMenuItem(String name, Tool t, String key)
		{
			super(name + " (" + key + ")");
			Paint.addTool(key, t);
			this.tool = t;
			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Paint.setTool(tool);
				}
			});
		}
	}

	public static class ImageMenuItem extends JMenuItem
	{
		private static final long serialVersionUID = 7018700148731008154L;

		private ImageOp op;

		public ImageMenuItem(String name, ImageOp o, String key)
		{
			super(name + " (Ctrl+Shift+" + key + ")");
			Paint.addImageOp(key, o);
			this.op = o;
			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					op.operation();
				}
			});
		}
	}
}