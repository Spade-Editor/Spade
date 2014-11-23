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

import heroesgrave.paint.gui.colorchooser.ColourChooser;
import heroesgrave.paint.gui.colorchooser.event.ColourListener;
import heroesgrave.paint.image.Document;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.UserPreferences;
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.misc.Callback;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.menu.MenuBarStyle;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;

public class GUIManager
{
	public static final BufferedImage ICON;
	public WebFrame frame;
	private JPanel panel;
	private WebPanel menus;
	public PaintCanvas canvasPanel;
	
	private WebMenuBar menuBar;
	
	private WebPanel documents;
	public WebScrollPane scroll;
	public ColourChooser chooser;
	public LayerManager layers;
	
	public InfoMenuBar info;
	
	public ToolBox toolBox;
	
	AboutDialog about;
	
	static
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(GUIManager.class.getResource("/res/favicon.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		ICON = image;
	}
	
	public GUIManager()
	{
		setLAF();
	}
	
	public void addDocument(Document doc)
	{
		DocumentButton b = new DocumentButton(doc);
		documents.add(b);
		documents.revalidate();
	}
	
	public void tryRemove(final Document doc, final Callback callback)
	{
		if(doc.saved())
		{
			removeDocument(doc);
			callback.callback();
			return;
		}
		final WebDialog close = new WebDialog(frame, "Save Changes?");
		close.setAlwaysOnTop(true);
		close.setAutoRequestFocus(true);
		close.setLayout(new BorderLayout());
		
		JButton save = new JButton("Save Changes");
		JButton dispose = new JButton("Close without Saving");
		JButton cancel = new JButton("Don't Close");
		
		// Init all the actions
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!Paint.save(doc))
					return;
				removeDocument(doc);
				callback.callback();
				close.dispose();
			}
		});
		dispose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeDocument(doc);
				callback.callback();
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
		
		save.setFocusable(false);
		dispose.setFocusable(false);
		cancel.setFocusable(false);
		
		close.add(save, BorderLayout.NORTH);
		close.add(dispose, BorderLayout.CENTER);
		close.add(cancel, BorderLayout.SOUTH);
		
		close.pack();
		close.setResizable(false);
		close.setVisible(true);
		close.setLocationRelativeTo(frame);
	}
	
	/**
	 * Finishes the GUI building process.
	 **/
	public void finish()
	{
		frame.pack();
		UserPreferences.loadPrefs(frame, chooser, layers);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.requestFocus();
	}
	
	public ArrayList<Document> getDocuments()
	{
		ArrayList<Document> docs = new ArrayList<Document>();
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			docs.add(b.doc);
		}
		return docs;
	}
	
	public void init()
	{
		initFrame();
		initMenu();
		this.canvasPanel = new PaintCanvas(frame);
		panel.add(canvasPanel, BorderLayout.CENTER);
		chooser = new ColourChooser();
		layers = new LayerManager(frame);
		about = new AboutDialog(frame);
		
		Paint.main.tools.toolbox = toolBox = new ToolBox();
		panel.add(toolBox.getToolbar(), BorderLayout.WEST);
		finish();
		
		chooser.addColorListener(new ColourListener()
		{
			public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
			{
				if(primary)
					Paint.main.setLeftColour(a << 24 | r << 16 | g << 8 | b, true);
				else
					Paint.main.setRightColour(a << 24 | r << 16 | g << 8 | b, true);
			}
		});
		
		initInputs();
	}
	
	public void initFrame()
	{
		frame = new WebFrame("Paint.JAVA");
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Paint.closeAllDocuments();
				frame.requestFocus();
			}
		});
		frame.addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				Input.CTRL = false;
				Input.ALT = false;
				Input.SHIFT = false;
			}
			
			public void focusLost(FocusEvent e)
			{
				Input.CTRL = false;
				Input.ALT = false;
				Input.SHIFT = false;
			}
		});
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		frame.setIconImage(ICON);
		
		panel = (JPanel) frame.getContentPane();
		
		panel.setLayout(new BorderLayout());
	}
	
	public void initInputs()
	{
		frame.addKeyListener(new Input());
	}
	
	public void initMenu()
	{
		menus = new WebPanel();
		menus.setLayout(new BorderLayout());
		
		menuBar = Menu.createMenuBar();
		
		WebMenuBar documentBar = new WebMenuBar();
		documentBar.setMenuBarStyle(MenuBarStyle.standalone);
		documentBar.setLayout(new BorderLayout());
		
		documents = new WebPanel();
		documents.setBackground(PaintCanvas.TRANSPARENT);
		documents.setLayout(new BoxLayout(documents, BoxLayout.X_AXIS));
		
		WebButton button = new WebButton(GUIManager.getIcon("exit"));
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.closeDocument(Paint.getDocument());
			}
		});
		button.setFocusable(false);
		
		documentBar.add(documents, BorderLayout.WEST);
		documentBar.add(button, BorderLayout.EAST);
		
		menus.add(menuBar, BorderLayout.NORTH);
		menus.add(documentBar, BorderLayout.CENTER);
		
		panel.add(menus, BorderLayout.NORTH);
	}
	
	public void removeDocument(Document doc)
	{
		DocumentButton toRemove = null;
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			if(b.doc == doc)
			{
				toRemove = b;
				break;
			}
		}
		if(toRemove == null)
			return;
		documents.remove(toRemove);
		documents.revalidate();
	}
	
	public void checkButtonNames()
	{
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			b.checkName();
		}
	}
	
	public void repaint()
	{
		canvasPanel.repaint();
	}
	
	public void setDocument(Document document)
	{
		canvasPanel.setDocument(document);
		layers.setDocument(document);
		if(document != null)
		{
			document.reconstructFlatmap();
			if(document.getFile() != null)
			{
				setTitle(IOUtils.relativeFrom(System.getProperty("user.dir"), document.getFile().getAbsolutePath()));
			}
			else
			{
				setTitle("Untitled - Paint.JAVA");
			}
		}
		else
		{
			setTitle("Paint.JAVA");
		}
		// Set selected button.
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			if(b.doc == document)
			{
				b.setSelected(true);
				b.checkName();
			}
			else
			{
				b.setSelected(false);
				b.checkName();
			}
		}
		
		frame.repaint();
	}
	
	public void setTitle(String title)
	{
		frame.setTitle(title);
	}
	
	public static final ImageIcon getIcon(String name)
	{
		String fullPath = "/res/icons/" + name + ".png";
		
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
	
	public static void setLAF()
	{
		// Check if the DlafClassName-property is avaible, and if so, use it's value as LAF name.
		String LAF_TO_USE = System.getProperty("DlafClassName");
		if(LAF_TO_USE != null && LAF_TO_USE != "")
		{
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
				try
				{
					boolean success = false;
					
					for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
					{
						if(info.getName().equals(LAF_TO_USE))
						{
							UIManager.setLookAndFeel(info.getClassName());
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
					
					System.err.println("Applying LAF failed. Printing all LAF names for correction:");
					for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
					{
						System.err.println("LAF: " + info.getName() + " / " + info.getClassName());
					}
					
				}
			}
		}
		else
		{
			WebLookAndFeel.install();
			WebLookAndFeel.setDecorateAllWindows(true);
		}
	}
}
