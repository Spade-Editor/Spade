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
import heroesgrave.utils.io.ImageLoader;
import heroesgrave.utils.misc.NumberDocumentFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
	public static boolean GRID_ENABLED = false;

	public static JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		menuBar.add(createViewMenu());
		menuBar.add(ToolMenu.createImageMenu());
		menuBar.add(ToolMenu.createToolMenu());
		menuBar.add(createWindowMenu());

		return menuBar;
	}

	private static JMenu createDialogsMenu()
	{
		JMenu dialogs = new JMenu("Manage Dialogs");

		JMenuItem colourChooser = new JMenuItem("Colour Chooser (F5)");

		colourChooser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.chooser.toggle();
			}
		});

		dialogs.add(colourChooser);

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

		JMenuItem newFile = new JMenuItem("New (Ctrl+N)");
		JMenuItem load = new JMenuItem("Open (Ctrl+O)");
		JMenuItem save = new JMenuItem("Save (Ctrl+S)");
		final JMenuItem saveAs = new JMenuItem("Save As");

		file.add(newFile);
		file.add(load);
		file.add(save);
		file.add(saveAs);

		newFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showNewMenu();
			}
		});

		load.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showOpenMenu();
			}
		});

		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.save();
			}
		});

		saveAs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.saveAs();
			}
		});

		return file;
	}

	public static void showOpenMenu()
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
		int returned = chooser.showOpenDialog(new CentredJDialog());
		
		if(returned == JFileChooser.APPROVE_OPTION)
		{
			Paint.main.openDir = Paint.main.openFile.getParentFile();
			Paint.main.openFile = chooser.getSelectedFile();
			Paint.main.gui.canvas.setImage(ImageLoader.loadImage(chooser.getSelectedFile().getAbsolutePath()));
		}
	}

	public static void showNewMenu()
	{
		final JDialog dialog = new CentredJDialog();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));

		dialog.getContentPane().add(panel);

		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);

		dialog.setTitle("New Image");

		final JTextField width = new JTextField("800");
		final JTextField height = new JTextField("600");
		((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberDocumentFilter());
		((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberDocumentFilter());
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
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				Paint.main.newImage(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
			}
		});

		cancel.addActionListener(new ActionListener()
		{
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

		JMenuItem undo = new JMenuItem("Undo (Ctrl+Z)");
		JMenuItem redo = new JMenuItem("Redo (Ctrl+Y)");

		undo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.gui.canvas.revertChange();
			}
		});

		redo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.gui.canvas.repeatChange();
			}
		});

		edit.add(undo);
		edit.add(redo);

		return edit;
	}

	private static JMenu createViewMenu()
	{
		JMenu view = new JMenu("View");

		JMenuItem zoomIn = new JMenuItem("Zoom In (Ctrl++)");
		JMenuItem zoomOut = new JMenuItem("Zoom Out (Ctrl+-)");
		JMenuItem grid = new JMenuItem("Toggle Grid (Ctrl+G)");

		zoomIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.canvas.incZoom();
			}
		});

		zoomOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.canvas.decZoom();
			}
		});

		grid.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GRID_ENABLED = !GRID_ENABLED;
				Paint.main.gui.canvas.getCanvas().repaint();
			}
		});

		view.add(zoomIn);
		view.add(zoomOut);
		view.add(grid);

		return view;
	}

	public static class CentredJDialog extends JDialog
	{
		private static final long serialVersionUID = 2628868597000831164L;

		public void setVisible(boolean b)
		{
			super.setVisible(b);
			if(b)
			{
				this.setLocationRelativeTo(null);
			}
		}
	}
}