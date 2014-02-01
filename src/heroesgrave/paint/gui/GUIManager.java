/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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
import heroesgrave.paint.image.CanvasManager;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.UserPreferences;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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
	private JComponent infoBar;
	private JMenuBar menuBar;
	public JScrollPane scroll;
	
	public CanvasManager canvas;
	public ColourChooser chooser;
	public LayerManager layers;
	public InfoMenu info;
	public ToolBox toolBox;
	
	AboutDialog about;
	
	private JPanel toolOptions;
	
	public GUIManager()
	{
		/* Remove/Add Slash at the end of this line to switch between Nimbus L&F and the Default */
		String LAF_TO_USE = "Nimbus";
		
		// Check if the DlafClassName-property is avaible, and if so, use it's value as LAF name.
		if(System.getProperty("DlafClassName") != null)
		{
			LAF_TO_USE = System.getProperty("DlafClassName");
		}
		
		if(LAF_TO_USE.equalsIgnoreCase("system_default"))
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("[GUIManager] Trying to apply LAF '" + LAF_TO_USE + "'!");
			
			try
			{
				boolean success = false;
				
				for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
				{
					if(info.getName().equals(LAF_TO_USE))
					{
						UIManager.setLookAndFeel(info.getClassName());
						System.out.println("[GUIManager] Successfully applied LAF '" + LAF_TO_USE + "'!");
						success = true;
						break;
					}
				}
				
				if(!success)
					throw new Exception("Failed to apply LAF! LAF not found: " + LAF_TO_USE);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				System.out.println("Applying LAF failed. Printing all LAF names for correction:");
				for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
				{
					System.out.println("LAF: " + info.getName() + " / " + info.getClassName());
				}
				
			}
		}
		
		/**/
	}
	
	public void init()
	{
		initFrame();
		initMenu();
		createCanvas();
		
		chooser = new ColourChooser(frame);
		layers = new LayerManager(canvas.getRoot());
		about = new AboutDialog(frame);
		toolBox = new ToolBox(frame);
		finish();
		
		initInputs();
	}
	
	public void setToolOption(JPanel options)
	{
		if(toolOptions != null)
		{
			info.getSpace().remove(toolOptions);
		}
		toolOptions = options;
		info.getSpace().add(toolOptions);
		menus.revalidate();
		menus.repaint();
	}
	
	public void setTitle(String title)
	{
		frame.setTitle(title);
	}
	
	public void createCanvas()
	{
		canvas = new CanvasManager();
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel()
		{
			BufferedImage img;
			Rectangle2D rect;
			TexturePaint paint;
			{
				img = new BufferedImage(2, 2, BufferedImage.TYPE_BYTE_GRAY);
				img.setRGB(0, 0, 0x333333);
				img.setRGB(1, 1, 0x333333);
				img.setRGB(1, 0, 0x555555);
				img.setRGB(0, 1, 0x555555);
				
				rect = new Rectangle2D.Float(0, 0, 24, 24);
				paint = new TexturePaint(img, rect);
			}
			
			@Override
			public void paint(Graphics $g)
			{
				if(Menu.DARK_BACKGROUND)
				{
					Graphics2D g = (Graphics2D) $g;
					g.setPaint(paint);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setPaint(null);
				}
				else
				{
					$g.setColor(new Color(0xDDEEFF));
					$g.fillRect(0, 0, getWidth(), getHeight());
				}
				
				super.paint($g);
			}
		};
		panel.setBackground(new java.awt.Color(0, true));
		panel.add(canvas.getPanel(), BorderLayout.CENTER);
		
		scroll = new JScrollPane(panel);
		scroll.removeMouseWheelListener(scroll.getMouseWheelListeners()[0]);
		scroll.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e)
			{
				if(e.isControlDown())
				{
					if(e.getUnitsToScroll() > 0)
					{
						Paint.main.gui.canvas.decZoom();
					}
					else if(e.getUnitsToScroll() < 0)
					{
						Paint.main.gui.canvas.incZoom();
					}
				}
				else
				{
					if(e.isShiftDown())
					{
						// Horizontal scrolling
						Adjustable adj = scroll.getHorizontalScrollBar();
						int scroll = e.getUnitsToScroll() * adj.getBlockIncrement();
						adj.setValue(adj.getValue() + scroll);
					}
					else
					{
						// Vertical scrolling
						Adjustable adj = scroll.getVerticalScrollBar();
						int scroll = e.getUnitsToScroll() * adj.getBlockIncrement();
						adj.setValue(adj.getValue() + scroll);
					}
				}
			}
		});
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		this.panel.add(scroll, BorderLayout.CENTER);
	}
	
	public void initMenu()
	{
		info = new InfoMenu();
		
		menus = new JPanel();
		menus.setLayout(new BorderLayout());
		
		menuBar = Menu.createMenuBar();
		infoBar = info.createInfoMenuBar();
		
		menus.add(menuBar, BorderLayout.NORTH);
		menus.add(infoBar, BorderLayout.CENTER);
		
		panel.add(menus, BorderLayout.NORTH);
	}
	
	public void setFile(File file)
	{
		frame.setTitle((file == null ? "Untitled" : file.getAbsolutePath()) + " - Paint.JAVA");
	}
	
	public void initFrame()
	{
		// Create the Frame
		frame = new JFrame("Untitled - Paint.JAVA");
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				displayCloseDialogue();
			}
		});
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Load the frames Icon. It looks a lot nicer with an actual logo. Remove if inappropriate.
		try
		{
			frame.setIconImage(ImageIO.read(this.getClass().getResource("/heroesgrave/paint/res/favicon.png")));
		}
		catch(IOException e1)
		{
			// Ignore the error if there is one! The logo doesn't matter so much as to crash the application.
		}
		
		heroesgrave.paint.plugin.PluginManager.instance.frameCreationEvent(frame);
		
		panel = (JPanel) frame.getContentPane();
		
		panel.setLayout(new BorderLayout());
	}
	
	public void displayCloseDialogue()
	{
		if(Paint.main.saved)
		{
			UserPreferences.savePrefs(frame, chooser, layers, toolBox);
			Paint.main.terminate = true;
			return;
		}
		
		// dialogue creation
		final JDialog close = new CentredJDialog(frame, "Save before you quit?");
		close.setAlwaysOnTop(true);
		close.setAutoRequestFocus(true);
		close.setLayout(new BorderLayout());
		
		JButton save = new JButton("Save & Quit");
		JButton dispose = new JButton("Quit without saving");
		JButton cancel = new JButton("Don't Quit");
		
		// Init all the actions
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.save();
				UserPreferences.savePrefs(frame, chooser, layers, toolBox);
				Paint.main.terminate = true;
				close.dispose();
			}
		});
		dispose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				UserPreferences.savePrefs(frame, chooser, layers, toolBox);
				Paint.main.terminate = true;
				close.dispose();
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				close.dispose();
			}
		});
		
		// Add the actions to the dialogue
		close.add(save, BorderLayout.NORTH);
		close.add(dispose, BorderLayout.CENTER);
		close.add(cancel, BorderLayout.SOUTH);
		
		// pack it, show it!
		close.pack();
		close.setResizable(false);
		close.setVisible(true);
	}
	
	/**
	 * Finishe's the GUI building process.
	 **/
	public void finish()
	{
		UserPreferences.loadPrefs(frame, chooser, layers, toolBox);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(true);
		frame.requestFocus();
	}
	
	public void initInputs()
	{
		Input in = new Input();
		
		frame.addKeyListener(in);
		chooser.getDialog().addKeyListener(in);
		layers.getDialog().addKeyListener(in);
		toolBox.getDialog().addKeyListener(in);
	}
	
	public static final ImageIcon getIcon(String name)
	{
		String fullPath = "/heroesgrave/paint/res/icons/" + name + ".png";
		
		try
		{
			URL url = Paint.class.getResource(fullPath);
			
			if(url == null)
				throw new IOException("ImageIcon Not found: " + fullPath);
			
			return new ImageIcon(ImageIO.read(url));
		}
		catch(IOException e)
		{
			try
			{
				return new ImageIcon(ImageIO.read(Paint.questionMarkURL));
			}
			catch(IOException e1)
			{
				throw new RuntimeException("FATAL ERROR WHILE LOADING ICONIMAGE: " + name);
			}
		}
	}
}