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

import heroesgrave.paint.image.Document;
import heroesgrave.paint.io.ImageImporter;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.Popup;
import heroesgrave.paint.plugin.PluginManager;
import heroesgrave.utils.misc.NumberFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;

import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.menu.MenuBarStyle;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.rootpane.WebDialog;

public class Menu
{
	// Pixel-Grid toggle
	public static boolean GRID_ENABLED = false;
	// Dark/Light Background toggle
	public static boolean DARK_BACKGROUND = false;
	
	public static WebMenuBar createMenuBar()
	{
		// M.E.I. MenuBar
		WebMenuBar menuBar = new WebMenuBar();
		menuBar.setMenuBarStyle(MenuBarStyle.standalone);
		
		// Main Menus
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		menuBar.add(createViewMenu());
		
		// Editing Menus
		menuBar.add((Paint.main.effects.operations = new WebMenu("Image")));
		menuBar.add((Paint.main.tools.toolsMenu = new WebMenu("Tools")));
		menuBar.add((Paint.main.effects.effects = new WebMenu("Effects")));
		menuBar.add((Paint.main.effects.generators = new WebMenu("Generators")));
		
		// Info Menus
		menuBar.add(createWindowMenu());
		menuBar.add(createHelpMenu());
		
		return menuBar;
	}
	
	private static WebMenu createHelpMenu()
	{
		WebMenu help = new WebMenu("Help/Info");
		
		WebMenuItem pluginManager = new WebMenuItem("Plugin Viewer", GUIManager.getIcon("plugin_viewer"));
		pluginManager.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				PluginManager.instance.showPluginViewer();
			}
		});
		
		WebMenuItem about = new WebMenuItem("About...", GUIManager.getIcon("about"));
		about.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.about.setVisible(true);
				Paint.main.gui.about.setLocationRelativeTo(Paint.main.gui.frame);
			}
		});
		
		help.add(pluginManager);
		help.add(about);
		
		return help;
	}
	
	private static WebMenu createDialogsMenu()
	{
		WebMenu dialogs = new WebMenu("Manage Dialogs");
		
		WebMenuItem colourChooser = new WebMenuItem("Colour Chooser (F5)", GUIManager.getIcon("colour_chooser"));
		
		colourChooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.chooser.toggle();
			}
		});
		
		WebMenuItem layerManager = new WebMenuItem("Layer Manager (F6)", GUIManager.getIcon("layer_manager"));
		
		layerManager.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.layers.toggle();
			}
		});
		
		dialogs.add(colourChooser);
		dialogs.add(layerManager);
		
		return dialogs;
	}
	
	private static WebMenu createWindowMenu()
	{
		WebMenu window = new WebMenu("Window");
		
		window.add(createDialogsMenu());
		
		return window;
	}
	
	private static WebMenu createFileMenu()
	{
		WebMenu file = new WebMenu("File");
		
		WebMenuItem newFile = new WebMenuItem("New (Ctrl+N)", GUIManager.getIcon("new"));
		WebMenuItem load = new WebMenuItem("Open (Ctrl+O)", GUIManager.getIcon("open"));
		WebMenuItem save = new WebMenuItem("Save (Ctrl+S)", GUIManager.getIcon("save"));
		final WebMenuItem saveAs = new WebMenuItem("Save As", GUIManager.getIcon("save_as"));
		WebMenuItem exit = new WebMenuItem("Exit", GUIManager.getIcon("exit"));
		
		file.add(newFile);
		file.add(load);
		file.add(save);
		file.add(saveAs);
		file.add(exit);
		
		newFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showNewDialog();
			}
		});
		
		load.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showOpenMenu();
			}
		});
		
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new Thread(new Runnable()
				{
					public void run()
					{
						Paint.save(Paint.getDocument());
					}
				}).start();
			}
		});
		
		saveAs.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new Thread(new Runnable()
				{
					public void run()
					{
						Paint.saveAs(Paint.getDocument());
					}
				}).start();
			}
		});
		
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.closeAllDocuments();
			}
		});
		
		return file;
	}
	
	public static void showOpenMenu()
	{
		final WebFileChooser chooser = new WebFileChooser(Paint.getDir());
		chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				if(f.isDirectory())
					return true;
				String name = f.getAbsolutePath();
				if(name.endsWith(".png"))
					return true;
				if(name.endsWith(".jpg"))
					return true;
				if(name.endsWith(".bmp"))
					return true;
				return false;
			}
			
			@Override
			public String getDescription()
			{
				return "ImageIO supported import formats (.png, .jpg, .bmp)";
			}
		});
		chooser.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				if(f.isDirectory())
					return true;
				String name = f.getAbsolutePath();
				if(name.endsWith(".png"))
					return true;
				if(name.endsWith(".jpg"))
					return true;
				if(name.endsWith(".bmp"))
					return true;
				
				int i = name.lastIndexOf('.');
				if(i < 0)
					return false;
				
				return ImageImporter.get(name.substring(i + 1)) != null;
			}
			
			@Override
			public String getDescription()
			{
				return "All supported import formats";
			}
		});
		
		// Add ALL the custom image-importers!
		ImageImporter.addAllImporters(chooser);
		
		int returned = chooser.showOpenDialog(new WebDialog(Paint.main.gui.frame, "Load Image"));
		
		if(returned == WebFileChooser.APPROVE_OPTION)
		{
			// If a Image takes too long, the application might crash.
			// By running the actual loading process in another thread, the AWT-Event Thread can continue working while the image is being loaded.
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Document doc = new Document(chooser.getSelectedFile());
					Paint.addDocument(doc);
					Paint.setDocument(doc);
				}
			}).start();
		}
	}
	
	public static void showNewDialog()
	{
		final JDialog dialog = new WebDialog(Paint.main.gui.frame, "New Image");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		
		dialog.getContentPane().add(panel);
		
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		
		dialog.setTitle("New Image");
		
		final JTextField width = new JTextField("800");
		final JTextField height = new JTextField("600");
		((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberFilter());
		((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberFilter());
		width.setColumns(8);
		height.setColumns(8);
		
		JLabel wl = new JLabel("Width: ");
		wl.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel hl = new JLabel("Height: ");
		hl.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton create = new JButton("Create");
		JButton cancel = new JButton("Cancel");
		
		create.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				int w = Integer.parseInt(width.getText());
				int h = Integer.parseInt(height.getText());
				if(w > 8192 || h > 8192 || w == 0 || h == 0)
				{
					Popup.show("Invalid Image Size", "The image dimensions must be more than 0 and less than 8192");
				}
				else
				{
					Paint.newImage(w, h);
				}
			}
		});
		
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
			}
		});
		
		panel.add(wl);
		panel.add(width);
		panel.add(hl);
		panel.add(height);
		panel.add(create);
		panel.add(cancel);
		
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(Paint.main.gui.frame);
	}
	
	private static WebMenu createEditMenu()
	{
		WebMenu edit = new WebMenu("Edit");
		
		WebMenuItem undo = new WebMenuItem("Undo (Ctrl+Z)", GUIManager.getIcon("undo"));
		WebMenuItem redo = new WebMenuItem("Redo (Ctrl+Y)", GUIManager.getIcon("redo"));
		
		undo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(Paint.getDocument() != null)
					Paint.getDocument().getHistory().revertChange();
			}
		});
		
		redo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(Paint.getDocument() != null)
					Paint.getDocument().getHistory().repeatChange();
			}
		});
		
		edit.add(undo);
		edit.add(redo);
		
		return edit;
	}
	
	private static WebMenu createViewMenu()
	{
		WebMenu view = new WebMenu("View");
		
		WebMenuItem zoomIn = new WebMenuItem("Zoom In (Ctrl++)", GUIManager.getIcon("zoom_inc"));
		WebMenuItem zoomOut = new WebMenuItem("Zoom Out (Ctrl+-)", GUIManager.getIcon("zoom_dec"));
		WebMenuItem grid = new WebMenuItem("Toggle Grid (Ctrl+G)", GUIManager.getIcon("toggle_grid"));
		WebMenuItem darkDraw = new WebMenuItem("Toggle Dark Background", GUIManager.getIcon("toggle_dark_bg"));
		
		zoomIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Paint.main.gui.canvas.incZoom();
			}
		});
		
		zoomOut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Paint.main.gui.canvas.decZoom();
			}
		});
		
		grid.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GRID_ENABLED = !GRID_ENABLED;
				//Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		darkDraw.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DARK_BACKGROUND = !DARK_BACKGROUND;
				if(Paint.getDocument() != null)
					Paint.main.gui.repaint();
			}
		});
		
		view.add(zoomIn);
		view.add(zoomOut);
		view.add(grid);
		view.add(darkDraw);
		
		return view;
	}
	
	public static class NumberTextField extends JTextField
	{
		private static final long serialVersionUID = -8289311655265709303L;
		
		public NumberTextField(String text)
		{
			super(text);
			
			setColumns(8);
			((AbstractDocument) getDocument()).setDocumentFilter(new NumberFilter());
		}
		
		public int get()
		{
			if(this.getText().isEmpty())
				return -1;
			
			return Integer.valueOf(this.getText());
		}
	}
}
