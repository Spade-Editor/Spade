package heroesgrave.paint.gui;

import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.imageops.Invert;
import heroesgrave.paint.imageops.Resize;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.tools.Ellipse;
import heroesgrave.paint.tools.Fill;
import heroesgrave.paint.tools.Line;
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
		menu.add(new ToolMenuItem("Line", new Line("Straight Line"), "L"));
		menu.add(new ToolMenuItem("Colour Picker", new Picker("Colour Picker"), "K"));
		menu.add(new ToolMenuItem("Paint Bucket", new Fill("Paint Bucket"), "F"));
		menu.add(new ToolMenuItem("Rectangle", new Rectangle("Rectangle"), "R"));
		menu.add(new ToolMenuItem("Ellipse", new Ellipse("Ellipse"), "E"));

		return menu;
	}

	public static JMenu createImageMenu()
	{
		JMenu menu = new JMenu("Image");

		menu.add(new ImageMenuItem("Resize Image", new Resize(), "R"));
		menu.add(new ImageMenuItem("Invert Colour", new Invert(), "I"));

		return menu;
	}

	private static class ToolMenuItem extends JMenuItem
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

	private static class ImageMenuItem extends JMenuItem
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
