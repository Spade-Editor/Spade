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
import heroesgrave.paint.main.Popup;
import heroesgrave.paint.plugin.PluginManager;
import heroesgrave.utils.io.ImageImporter;
import heroesgrave.utils.misc.NumberFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;

public class Menu
{
	/**
	 * Single boolean flag for the visibility toggle of the Pixel-Grid.
	 **/
	public static boolean GRID_ENABLED = false;
	public static boolean DARK_BACKGROUND = false;
	
	public static JMenuBar createMenuBar()
	{
		// M.E.I. MenuBar
		JMenuBar menuBar = new JMenuBar();
		
		// Main Menu's
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		menuBar.add(createViewMenu());
		
		// Editing Menu's
		menuBar.add(ToolMenu.createImageMenu());
		menuBar.add(ToolMenu.createToolMenu());
		menuBar.add(ToolMenu.createEffectMenu());
		
		// Info Menu's
		menuBar.add(createWindowMenu());
		menuBar.add(createHelpMenu());
		
		return menuBar;
	}
	
	private static JMenu createHelpMenu()
	{
		JMenu help = new JMenu("Help/Info");
		
		JMenuItem pluginManager = new JMenuItem("Plugin Viewer", GUIManager.getIcon("plugin_viewer"));
		pluginManager.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				PluginManager.instance.showPluginManager();
			}
		});
		
		JMenuItem about = new JMenuItem("About...", GUIManager.getIcon("about"));
		about.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.about.setVisible(true);
			}
		});
		
		help.add(pluginManager);
		help.add(about);
		
		return help;
	}
	
	private static JMenu createDialogsMenu()
	{
		JMenu dialogs = new JMenu("Manage Dialogs");
		
		JMenuItem colourChooser = new JMenuItem("Colour Chooser (F5)", GUIManager.getIcon("colour_chooser"));
		
		colourChooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.chooser.toggle();
			}
		});
		
		JMenuItem layerManager = new JMenuItem("Layer Manager (F6)", GUIManager.getIcon("layer_manager"));
		
		layerManager.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.layers.toggle();
			}
		});
		
		JMenuItem toolBox = new JMenuItem("ToolBox (F4)", GUIManager.getIcon("toolbox"));
		toolBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.toolBox.toggle();
			}
		});
		
		dialogs.add(toolBox);
		dialogs.add(colourChooser);
		dialogs.add(layerManager);
		
		return dialogs;
	}
	
	private static JMenu createWindowMenu()
	{
		JMenu window = new JMenu("Window");
		
		window.add(createDialogsMenu());
		
		return window;
	}
	
	private static JMenu createFileMenu()
	{
		JMenu file = new JMenu("File");
		
		JMenuItem newFile = new JMenuItem("New (Ctrl+N)", GUIManager.getIcon("new"));
		JMenuItem load = new JMenuItem("Open (Ctrl+O)", GUIManager.getIcon("open"));
		JMenuItem save = new JMenuItem("Save (Ctrl+S)", GUIManager.getIcon("save"));
		final JMenuItem saveAs = new JMenuItem("Save As", GUIManager.getIcon("save"));
		JMenuItem exit = new JMenuItem("Exit", GUIManager.getIcon("exit"));
		
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
				showNewMenu();
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
				Paint.save();
			}
		});
		
		saveAs.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.saveAs();
			}
		});
		
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.displayCloseDialogue();
			}
		});
		
		return file;
	}
	
	public static void showOpenMenu()
	{
		final JFileChooser chooser = new JFileChooser(Paint.main.openDir);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
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
				return false;
			}
			
			@Override
			public String getDescription()
			{
				return "ImageIO Supported import image formats (.png, .jpg, .bmp)";
			}
		});
		
		// Add ALL the custom image-importers!
		ImageImporter.addAllImporters(chooser);
		
		int returned = chooser.showOpenDialog(new CentredJDialog(Paint.main.gui.frame, "Load Image"));
		
		if(returned == JFileChooser.APPROVE_OPTION)
		{
			Paint.main.openFile = chooser.getSelectedFile();
			Paint.main.openDir = Paint.main.openFile.getParentFile();
			
			// If a Image takes too long, the application might crash.
			// By running the actual loading process in another thread, the AWT-Event Thread can continue working while the image is being loaded.
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Paint.main.gui.canvas.setRoot(ImageImporter.loadImage(chooser.getSelectedFile().getAbsolutePath()));
				}
			}).start();
		}
	}
	
	public static void showNewMenu()
	{
		final JDialog dialog = new CentredJDialog(Paint.main.gui.frame, "New Image");
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
					Paint.main.newImage(w, h);
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
	}
	
	private static JMenu createEditMenu()
	{
		JMenu edit = new JMenu("Edit");
		
		JMenuItem undo = new JMenuItem("Undo (Ctrl+Z)", GUIManager.getIcon("undo"));
		JMenuItem redo = new JMenuItem("Redo (Ctrl+Y)", GUIManager.getIcon("redo"));
		JMenuItem clear = new JMenuItem("Clear History", GUIManager.getIcon("clear_history"));
		
		undo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.history.revertChange();
			}
		});
		
		redo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.history.repeatChange();
			}
		});
		
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.history.clearHistory();
			}
		});
		
		edit.add(undo);
		edit.add(redo);
		edit.add(clear);
		
		return edit;
	}
	
	private static JMenu createViewMenu()
	{
		JMenu view = new JMenu("View");
		
		JMenuItem zoomIn = new JMenuItem("Zoom In (Ctrl++)", GUIManager.getIcon("zoom_inc"));
		JMenuItem zoomOut = new JMenuItem("Zoom Out (Ctrl+-)", GUIManager.getIcon("zoom_dec"));
		JMenuItem grid = new JMenuItem("Toggle Grid (Ctrl+G)", GUIManager.getIcon("toggle_grid"));
		JMenuItem darkDraw = new JMenuItem("Toggle Dark Background", GUIManager.getIcon("toggle_dark_bg"));
		
		zoomIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.canvas.incZoom();
			}
		});
		
		zoomOut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.canvas.decZoom();
			}
		});
		
		grid.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GRID_ENABLED = !GRID_ENABLED;
				Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		darkDraw.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DARK_BACKGROUND = !DARK_BACKGROUND;
				Paint.main.gui.frame.repaint();
			}
		});
		
		view.add(zoomIn);
		view.add(zoomOut);
		view.add(grid);
		view.add(darkDraw);
		
		return view;
	}
	
	public static class CentredJDialog extends JDialog
	{
		private static final long serialVersionUID = 2628868597000831164L;
		
		public CentredJDialog(JFrame frame, String title)
		{
			super(frame, title);
		}
		
		@Override
		public void setVisible(boolean b)
		{
			super.setVisible(b);
			if(b)
			{
				this.setLocationRelativeTo(null);
			}
		}
	}
	
	public static class CentredJLabel extends JLabel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -782420829240440738L;
		
		public CentredJLabel(String label)
		{
			super(label);
			this.setHorizontalAlignment(JLabel.CENTER);
		}
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