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

package heroesgrave.paint.main;

import heroesgrave.paint.gui.GUIManager;
import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.tools.Tool;
import heroesgrave.utils.app.Application;
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.io.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class Paint extends Application
{
	public static Paint main = new Paint();

	public GUIManager gui;
	public l19.pluginSys.PluginManager pluginManager;

	public File openFile;
	public File openDir;

	public Tool currentTool;

	private int colour = 0x00000000;

	public boolean saved = true;

	private static HashMap<String, Tool> tools = new HashMap<String, Tool>();
	private static HashMap<String, ImageOp> imageOps = new HashMap<String, ImageOp>();

	public void init()
	{
		pluginManager = l19.pluginSys.PluginManager.instance(this);
		gui = new GUIManager();
		setColour(0xFF000000);
	}

	public void update()
	{
		gui.info.setSaved(saved);
		gui.info.setScale(gui.canvas.getScale());
		gui.setFile(openFile);
	}

	public void render()
	{

	}

	public void dispose()
	{

	}

	public void newImage(final int width, final int height)
	{
		if(!saved)
		{
			final JDialog newImage = new JDialog();
			newImage.setTitle("Save current image?");
			newImage.setAlwaysOnTop(true);
			newImage.setAutoRequestFocus(true);
			newImage.setLayout(new BorderLayout());

			JButton save = new JButton("Save & Create New Image");
			JButton dispose = new JButton("Create new image without saving");
			JButton cancel = new JButton("Don't create new image");

			newImage.add(save, BorderLayout.NORTH);
			newImage.add(dispose, BorderLayout.CENTER);
			newImage.add(cancel, BorderLayout.SOUTH);

			newImage.pack();
			newImage.setResizable(false);
			newImage.setVisible(true);
			newImage.setLocationRelativeTo(null);

			save.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Paint.save();
					newImage.dispose();
					createImage(width, height);
				}
			});
			dispose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newImage.dispose();
					createImage(width, height);
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newImage.dispose();
				}
			});
		}
		else
		{
			createImage(width, height);
		}
	}

	private void createImage(int width, int height)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, width, height);
		g.dispose();
		gui.canvas.setImage(image);
		this.openFile = null;
	}

	public static void addTool(String key, Tool tool)
	{
		tools.put(key.toLowerCase(), tool);
	}

	public static void addImageOp(String key, ImageOp op)
	{
		imageOps.put(key.toLowerCase(), op);
	}

	public static Tool getTool(String key)
	{
		return tools.get(key.toLowerCase());
	}

	public static ImageOp getImageOp(String key)
	{
		return imageOps.get(key.toLowerCase());
	}

	public static void main(String[] args)
	{
		IOUtils.setMainClass(Paint.class);
		Application.launch(main);
	}

	public static void addChange(Change change)
	{
		main.gui.canvas.addChange(change);
	}

	public static void setTool(Tool tool)
	{
		main.currentTool = tool;
		main.gui.info.setTool(tool);
	}

	public static void save()
	{
		if(Paint.main.openFile != null)
		{
			String fileName = Paint.main.openFile.getAbsolutePath();
			if(fileName.endsWith(".png"))
			{
				ImageLoader.writeImage(Paint.main.gui.canvas.getImage(), "PNG", fileName);
			}
			else
			{
				ImageLoader.writeImage(Paint.main.gui.canvas.getImage(), "PNG", fileName + ".png");
				Paint.main.openFile = new File(fileName + ".png");
			}
			main.saved = true;
		}
		else
		{
			saveAs();
		}
	}

	public static void saveAs()
	{
		JFileChooser chooser = new JFileChooser(Paint.main.openDir);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileFilter()
		{
			public boolean accept(File f)
			{
				if(f.isDirectory())
					return true;
				String name = f.getAbsolutePath();
				if(name.endsWith(".png"))
					return true;
				return false;
			}

			public String getDescription()
			{
				return "Supported image files (.png)";
			}
		});

		int returned = chooser.showSaveDialog(new CentredJDialog());

		if(returned == JFileChooser.APPROVE_OPTION)
		{
			Paint.main.openFile = chooser.getSelectedFile();
			Paint.main.openDir = Paint.main.openFile.getParentFile();
			String fileName = Paint.main.openFile.getAbsolutePath();
			if(fileName.endsWith(".png"))
			{
				ImageLoader.writeImage(Paint.main.gui.canvas.getImage(), "PNG", fileName);
			}
			else
			{
				ImageLoader.writeImage(Paint.main.gui.canvas.getImage(), "PNG", fileName + ".png");
				Paint.main.openFile = new File(fileName + ".png");
			}
			main.saved = true;
		}
	}

	public void setColour(int c)
	{
		gui.chooser.setColour(c);
		gui.info.setColour(c);
		this.colour = c;
	}

	public int getColour()
	{
		return this.colour;
	}
}