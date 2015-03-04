// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.gui.menus;

import heroesgrave.spade.gui.GUIManager;
import heroesgrave.spade.gui.dialogs.GridEffectDialog;
import heroesgrave.spade.gui.misc.ClipboardHandler;
import heroesgrave.spade.gui.misc.WeblafWrapper;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.io.ImageImporter;
import heroesgrave.spade.io.ReadableFileFilter;
import heroesgrave.spade.main.Popup;
import heroesgrave.spade.main.Spade;
import heroesgrave.spade.plugin.PluginManager;
import heroesgrave.utils.misc.NumberFilter;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;

import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;

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
		//menuBar.setMenuBarStyle(MenuBarStyle.standalone);
		
		// Main Menus
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		menuBar.add(createViewMenu());
		
		// Editing Menus
		menuBar.add((Spade.main.effects.operations = new WebMenu("Image")));
		menuBar.add((Spade.main.tools.toolsMenu = new WebMenu("Tools")));
		menuBar.add((Spade.main.effects.effects = new WebMenu("Effects")));
		menuBar.add((Spade.main.effects.generators = new WebMenu("Generators")));
		
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
				Spade.main.gui.about.setVisible(true);
				Spade.main.gui.about.setLocationRelativeTo(Spade.main.gui.frame);
			}
		});
		
		help.add(pluginManager);
		help.add(about);
		
		return help;
	}
	
	private static WebMenu createDialogsMenu()
	{
		WebMenu dialogs = new WebMenu("Manage Dialogs");
		
		WebMenuItem colourChooser = new WebMenuItem("Colour Chooser", GUIManager.getIcon("colour_chooser"));
		colourChooser.setAccelerator(Hotkey.F5);
		
		colourChooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Spade.main.gui.chooser.toggle();
			}
		});
		
		WebMenuItem layerManager = new WebMenuItem("Layer Manager", GUIManager.getIcon("layer_manager"));
		layerManager.setAccelerator(Hotkey.F6);
		
		layerManager.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Spade.main.gui.layers.toggle();
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
		
		WebMenuItem newFile = new WebMenuItem("New", GUIManager.getIcon("new"));
		newFile.setAccelerator(Hotkey.CTRL_N);
		WebMenuItem load = new WebMenuItem("Open", GUIManager.getIcon("open"));
		load.setAccelerator(Hotkey.CTRL_O);
		WebMenuItem save = new WebMenuItem("Save", GUIManager.getIcon("save"));
		save.setAccelerator(Hotkey.CTRL_S);
		final WebMenuItem saveAs = new WebMenuItem("Save As", GUIManager.getIcon("save_as"));
		saveAs.setAccelerator(new HotkeyData(true, true, false, KeyEvent.VK_S));
		WebMenuItem exit = new WebMenuItem("Exit", GUIManager.getIcon("exit"));
		
		file.add(newFile);
		file.add(load);
		file.add(new WebSeparator());
		file.add(save);
		file.add(saveAs);
		file.add(new WebSeparator());
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
						Spade.save(Spade.getDocument());
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
						Spade.saveAs(Spade.getDocument());
					}
				}).start();
			}
		});
		
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Spade.closeAllDocuments();
			}
		});
		
		return file;
	}
	
	public static void showOpenMenu()
	{
		final WebFileChooser chooser = new WebFileChooser(Spade.getDir());
		chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new ReadableFileFilter());
		
		// Add ALL the custom image-importers!
		ImageImporter.addAllImporters(chooser);
		
		int returned = chooser.showOpenDialog(new WebDialog(Spade.main.gui.frame, "Load Image"));
		
		if(returned == WebFileChooser.APPROVE_OPTION)
		{
			// If a Image takes too long, the application might crash.
			// By running the actual loading process in another thread, the AWT-Event Thread can continue working while the image is being loaded.
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Document doc = Document.loadFromFile(chooser.getSelectedFile());
					if(doc != null)
					{
						Spade.addDocument(doc);
						Spade.setDocument(doc);
					}
				}
			}).start();
		}
	}
	
	public static void showNewDialog()
	{
		final GridEffectDialog dialog = new GridEffectDialog(1, 1, "Create Image", null);
		
		Dimension dim = ClipboardHandler.getImageSize();
		if(dim == null)
		{
			Document current = Spade.getDocument();
			if(current != null)
			{
				dim = new Dimension(current.getWidth(), current.getHeight());
			}
			else
			{
				dim = new Dimension(512, 512);
			}
		}
		
		final JTextField width = new JTextField("" + dim.width);
		final JTextField height = new JTextField("" + dim.height);
		//width.setHorizontalAlignment(JTextField.RIGHT);
		//height.setHorizontalAlignment(JTextField.RIGHT);
		
		{
			JPanel panel = dialog.getPanel(0);
			panel.setLayout(new GridLayout(0, 2));
			
			((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberFilter());
			((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberFilter());
			
			width.setColumns(4);
			height.setColumns(4);
			
			JLabel wl = WeblafWrapper.createLabel("Width: ");
			wl.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel hl = WeblafWrapper.createLabel("Height: ");
			hl.setHorizontalAlignment(SwingConstants.CENTER);
			
			panel.add(wl);
			panel.add(width);
			
			panel.add(hl);
			panel.add(height);
		}
		
		JPanel panel = dialog.getBottomPanel();
		panel.setLayout(new GridLayout(0, 2));
		JButton create = WeblafWrapper.createButton("Create");
		JButton cancel = WeblafWrapper.createButton("Cancel");
		
		panel.add(create);
		panel.add(cancel);
		
		create.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				if(width.getText().equals("") || height.getText().equals(""))
					return;
				int w = Integer.parseInt(width.getText());
				int h = Integer.parseInt(height.getText());
				if(w > Document.MAX_DIMENSION || h > Document.MAX_DIMENSION || w == 0 || h == 0)
				{
					Popup.show("Invalid Image Dimensions", "Image must have dimensions higher than 0 and no more than " + Document.MAX_DIMENSION);
				}
				else
				{
					Spade.newImage(w, h);
				}
			}
		});
		
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
			}
		});
		
		dialog.display();
	}
	
	private static WebMenu createEditMenu()
	{
		WebMenu edit = new WebMenu("Edit");
		
		WebMenuItem undo = new WebMenuItem("Undo", GUIManager.getIcon("undo"));
		undo.setAccelerator(Hotkey.CTRL_Z);
		WebMenuItem redo = new WebMenuItem("Redo", GUIManager.getIcon("redo"));
		redo.setAccelerator(Hotkey.CTRL_Y);
		
		undo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(Spade.getDocument() != null)
					Spade.getDocument().getHistory().revertChange();
			}
		});
		
		redo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(Spade.getDocument() != null)
					Spade.getDocument().getHistory().repeatChange();
			}
		});
		
		edit.add(undo);
		edit.add(redo);
		
		return edit;
	}
	
	private static WebMenu createViewMenu()
	{
		WebMenu view = new WebMenu("View");
		
		WebMenuItem zoomIn = new WebMenuItem("Zoom In", GUIManager.getIcon("zoom_inc"));
		zoomIn.setAccelerator(new HotkeyData(true, false, false, KeyEvent.VK_EQUALS));
		WebMenuItem zoomOut = new WebMenuItem("Zoom Out", GUIManager.getIcon("zoom_dec"));
		zoomOut.setAccelerator(new HotkeyData(true, false, false, KeyEvent.VK_MINUS));
		WebMenuItem zoomReset = new WebMenuItem("Reset Zoom", GUIManager.getIcon("zoom_reset"));
		zoomReset.setAccelerator(new HotkeyData(true, false, false, KeyEvent.VK_0));
		
		WebMenuItem grid = new WebMenuItem("Toggle Grid", GUIManager.getIcon("toggle_grid"));
		grid.setAccelerator(Hotkey.CTRL_G);
		WebMenuItem darkDraw = new WebMenuItem("Toggle Dark Background", GUIManager.getIcon("toggle_dark_bg"));
		darkDraw.setAccelerator(Hotkey.CTRL_B);
		
		zoomIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				float zoom = Spade.main.gui.canvas.getScale();
				if(zoom < 1f)
				{
					Spade.main.gui.canvas.setScale(Math.min(zoom * 2f, 1f));
				}
				else
				{
					Spade.main.gui.canvas.setScale(Math.min(zoom + 2f, 64f));
				}
			}
		});
		
		zoomOut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				float zoom = Spade.main.gui.canvas.getScale();
				if(zoom <= 1f)
				{
					Spade.main.gui.canvas.setScale(Math.max(zoom / 2f, 1 / 32f));
				}
				else
				{
					Spade.main.gui.canvas.setScale(Math.max(zoom - 2f, 1f));
				}
			}
		});
		
		zoomReset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Spade.main.gui.canvas.setScale(1f);
			}
		});
		
		grid.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GRID_ENABLED = !GRID_ENABLED;
				Spade.main.gui.repaint();
			}
		});
		
		darkDraw.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DARK_BACKGROUND = !DARK_BACKGROUND;
				Spade.main.gui.repaint();
			}
		});
		
		view.add(zoomIn);
		view.add(zoomOut);
		view.add(zoomReset);
		view.add(new WebSeparator());
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
