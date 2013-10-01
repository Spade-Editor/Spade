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

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

public class GUIManager
{
	public JFrame frame;
	private JPanel panel, menus;
	private JMenuBar menuBar;
	private JScrollPane canvasZone;

	public CanvasManager canvas;
	public ColourChooser chooser;
	public InfoMenu info;
	
	private JMenuBar toolOptions;

	private Input input = new Input();

	public GUIManager()
	{
		/* Remove/Add Slash at the end of this line to switch between Nimbus L&F and the Default */
		try
		{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if(info.getName().equals("Nimbus"))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		/**/

		initFrame();
		initMenu();
		initInputs();
		createCanvas();
		finish();

		chooser = new ColourChooser();
	}
	
	public void setToolOption(JMenuBar options)
	{
		if(toolOptions != null)
		{
			menus.remove(toolOptions);
		}
		toolOptions = options;
		menus.add(toolOptions);
		toolOptions.revalidate();
	}

	public void setTitle(String title)
	{
		frame.setTitle(title);
	}

	public void createCanvas()
	{
		canvas = new CanvasManager();

		JPanel panel = new JPanel();
		panel.add(canvas.getCanvas());

		canvasZone = new JScrollPane(panel);
		canvasZone.addMouseWheelListener(input);
		canvasZone.getVerticalScrollBar().setUnitIncrement(16);
		canvasZone.getHorizontalScrollBar().setUnitIncrement(16);
		this.panel.add(canvasZone, BorderLayout.CENTER);
	}

	public void initMenu()
	{
		info = new InfoMenu();

		menus = new JPanel();
		menus.setLayout(new GridLayout(0, 1));

		menuBar = Menu.createMenuBar();
		JMenuBar infoBar = info.createInfoMenuBar();

		menus.add(menuBar);
		menus.add(infoBar);

		panel.add(menus, BorderLayout.NORTH);
	}

	public void setFile(File file)
	{
		frame.setTitle((file == null ? "Untitled" : file.getAbsolutePath()) + " - Paint.JAVA");
	}

	public void initFrame()
	{
		frame = new JFrame("Untitled - Paint.JAVA");
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(Paint.main.saved)
				{
					Paint.main.terminate = true;
					return;
				}

				final JDialog close = new CentredJDialog();
				close.setTitle("Save before you quit?");
				close.setAlwaysOnTop(true);
				close.setAutoRequestFocus(true);
				close.setLayout(new BorderLayout());

				JButton save = new JButton("Save & Quit");
				JButton dispose = new JButton("Quit without saving");
				JButton cancel = new JButton("Don't Quit");

				close.add(save, BorderLayout.NORTH);
				close.add(dispose, BorderLayout.CENTER);
				close.add(cancel, BorderLayout.SOUTH);

				close.pack();
				close.setResizable(false);
				close.setVisible(true);

				save.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Paint.save();
						Paint.main.terminate = true;
						close.dispose();
					}
				});
				dispose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Paint.main.terminate = true;
						close.dispose();
					}
				});
				cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						close.dispose();
					}
				});
			}
		});
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		panel = (JPanel) frame.getContentPane();

		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(new BorderLayout());
	}

	public void finish()
	{
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
	}

	public void initInputs()
	{
		Input in = new Input();

		frame.addKeyListener(in);
		frame.addMouseWheelListener(in);
	}
}