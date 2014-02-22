package heroesgrave.paint.gui;

import heroesgrave.paint.gui.ToolBox.ToolBoxButton;
import heroesgrave.paint.gui.ToolMenu.ToolMenuItem;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.plugin.PluginManager;
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

import javax.swing.JMenu;

public class Tools
{
	public static Tool DEF;
	
	public JMenu toolsMenu;
	public ToolBox toolbox;
	
	public void addTool(Tool tool, String name, String shortcut)
	{
		toolbox.addButton(new ToolBoxButton(name, tool));
		toolsMenu.add(new ToolMenuItem(name, tool, shortcut));
	}
	
	public void registerTools()
	{
		DEF = new Pixel("Pencil");
		Paint.main.currentTool = DEF;
		
		addTool(DEF, "Pencil", "P");
		addTool(new Brush("Brush"), "Brush", "B");
		addTool(new Eraser("Eraser"), "Eraser", "E");
		addTool(new Picker("Colour Picker"), "Colour Picker", "K");
		addTool(new Fill("Paint Bucket"), "Paint Bucket", "F");
		addTool(new Line("Straight Line"), "Line", "L");
		addTool(new Rectangle("Rectangle"), "Rectangle", "R");
		addTool(new Ellipse("Ellipse"), "Ellipse", "C");
		addTool(new SelectTool("Select"), "Select", "S");
		addTool(new Move("Move"), "Move", "M");
		
		PluginManager.instance.registerTools(this);
	}
}
