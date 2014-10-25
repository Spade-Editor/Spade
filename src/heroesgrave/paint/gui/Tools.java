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

import heroesgrave.paint.gui.ToolBox.ToolBoxButton;
import heroesgrave.paint.gui.ToolMenu.ToolMenuItem;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.plugin.PluginManager;
import heroesgrave.paint.tools.FillRectangle;
import heroesgrave.paint.tools.Pencil;
import heroesgrave.paint.tools.Rectangle;
import heroesgrave.paint.tools.Tool;

import javax.swing.JMenu;

public class Tools
{
	public static Tool DEF;
	
	public JMenu toolsMenu;
	public ToolBox toolbox;
	
	public void addTool(Tool tool, String shortcut)
	{
		toolbox.addButton(new ToolBoxButton(tool.name, tool));
		toolsMenu.add(new ToolMenuItem(tool.name, tool, shortcut));
	}
	
	public void registerTools()
	{
		DEF = new Pencil("Pencil");
		Paint.main.currentTool = DEF;
		
		addTool(DEF, "P");
		addTool(new Rectangle("Rectangle"), "R");
		addTool(new FillRectangle("Fill Rectangle"), "F");
		/*
		addTool(new Brush("Brush"), "Brush", "B");
		addTool(new Eraser("Eraser"), "Eraser", "E");
		addTool(new Picker("Colour Picker"), "Colour Picker", "K");
		addTool(new Fill("Paint Bucket"), "Paint Bucket", "F");
		addTool(new Line("Straight Line"), "Line", "L");
		addTool(new Ellipse("Ellipse"), "Ellipse", "C");
		addTool(new SelectTool("Select"), "Select", "S");
		addTool(new Move("Move"), "Move", "M");
		*/
		PluginManager.instance.registerTools(this);
	}
}
