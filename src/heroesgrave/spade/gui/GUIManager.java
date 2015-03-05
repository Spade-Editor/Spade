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

package heroesgrave.spade.gui;

import heroesgrave.spade.gui.colorchooser.ColourChooser;
import heroesgrave.spade.gui.colorchooser.event.ColourListener;
import heroesgrave.spade.gui.dialogs.AboutDialog;
import heroesgrave.spade.gui.dialogs.LayerManager;
import heroesgrave.spade.gui.dialogs.ToolBox;
import heroesgrave.spade.gui.menus.DocumentButton;
import heroesgrave.spade.gui.menus.DocumentMenuItem;
import heroesgrave.spade.gui.menus.InfoMenuBar;
import heroesgrave.spade.gui.menus.Menu;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.io.ImageImporter;
import heroesgrave.spade.io.ReadableFileFilter;
import heroesgrave.spade.main.Input;
import heroesgrave.spade.main.Spade;
import heroesgrave.spade.main.UserPreferences;
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.misc.Callback;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;

public class GUIManager
{
	public static final BufferedImage ICON;
	public WebFrame frame;
	private JPanel panel;
	private WebPanel menus;
	public PaintCanvas canvas;
	
	public InfoMenuBar info;
	
	private WebMenuBar menuBar, documentBar, infoBar;
	
	private WebPanel documents;
	private WebMenu documentsDrop;
	
	public WebScrollPane scroll;
	public ColourChooser chooser;
	public LayerManager layers;
	
	public ToolBox toolBox;
	
	public AboutDialog about;
	
	static
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(GUIManager.class.getResource("/res/icon.png"));
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
		documents.add(new DocumentButton(doc));
		documents.revalidate();
		documentsDrop.add(new DocumentMenuItem(doc));
		if(documents.getComponentCount() > 0)
		{
			infoBar.setVisible(true);
		}
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
				if(!Spade.save(doc))
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
		
		WebPanel centre = new WebPanel();
		centre.setLayout(new BorderLayout());
		
		this.canvas = new PaintCanvas(frame);
		centre.add(canvas, BorderLayout.CENTER);
		
		Spade.main.tools.toolbox = toolBox = new ToolBox();
		centre.add(toolBox.getToolbar(), BorderLayout.WEST);
		
		panel.add(centre, BorderLayout.CENTER);
		
		chooser = new ColourChooser();
		layers = new LayerManager(frame);
		about = new AboutDialog(frame);
		
		chooser.addColorListener(new ColourListener()
		{
			public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
			{
				if(primary)
					Spade.main.setLeftColour(a << 24 | r << 16 | g << 8 | b, true);
				else
					Spade.main.setRightColour(a << 24 | r << 16 | g << 8 | b, true);
			}
		});
		
		//Add dragdrop handler
		@SuppressWarnings("serial")
		TransferHandler handler = new TransferHandler() {
            @Override
            public boolean canImport(final TransferHandler.TransferSupport support) {
                if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }

                return true;
            }

            @Override
            public boolean importData(final TransferHandler.TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                Transferable t = support.getTransferable();
                try {
                    @SuppressWarnings("unchecked")
					final File toload = (File) ((List<File>) t.getTransferData(DataFlavor.javaFileListFlavor)).get(0);
                    boolean supported = new ReadableFileFilter().accept(toload);

                    if (supported)
                    {
                        new Thread(new Runnable()
            			{
            				@Override
            				public void run()
            				{
            					Document doc = Document.loadFromFile(toload);
            					if(doc != null)
            					{
            						Spade.addDocument(doc);
            						Spade.setDocument(doc);
            					}
            				}
            			}).start();
                    }
                    
                    else return false;
                    
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
        };
        centre.setTransferHandler(handler);
		
		finish();
		initInputs();
	}
	
	public void initFrame()
	{
		frame = new WebFrame("Spade");
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Spade.closeAllDocuments();
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
		
		documentBar = new WebMenuBar();
		//documentBar.setMenuBarStyle(MenuBarStyle.standalone);
		documentBar.setLayout(new BorderLayout());
		
		documents = new WebPanel();
		documents.setBackground(PaintCanvas.TRANSPARENT);
		documents.setLayout(new BoxLayout(documents, BoxLayout.X_AXIS));
		
		documentsDrop = new WebMenu(GUIManager.getIcon("save"));
		
		WebButton button = new WebButton(GUIManager.getIcon("exit"));
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Spade.closeDocument(Spade.getDocument());
			}
		});
		button.setFocusable(false);
		
		WebMenuItem load = new WebMenuItem("Open", GUIManager.getIcon("open"));
		load.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Menu.showOpenMenu();
			}
		});
		WebMenuItem new_ = new WebMenuItem("New", GUIManager.getIcon("new"));
		new_.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Menu.showNewDialog();
			}
		});
		
		documentsDrop.add(new_);
		documentsDrop.add(load);
		documentsDrop.add(new WebSeparator());
		
		documentBar.add(documentsDrop, BorderLayout.WEST);
		documentBar.add(documents, BorderLayout.CENTER);
		documentBar.add(button, BorderLayout.EAST);
		
		menus.add(menuBar, BorderLayout.NORTH);
		menus.add(documentBar, BorderLayout.CENTER);
		
		panel.add(menus, BorderLayout.NORTH);
		
		info = new InfoMenuBar();
		
		infoBar = info.createInfoMenuBar();
		infoBar.setVisible(false);
		
		panel.add(infoBar, BorderLayout.SOUTH);
	}
	
	public void removeDocument(Document doc)
	{
		Component toRemove = null;
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			if(b.doc == doc)
			{
				toRemove = b;
				break;
			}
		}
		if(toRemove != null)
		{
			documents.remove(toRemove);
			documents.revalidate();
			toRemove = null;
		}
		
		for(Component c : documentsDrop.getMenuComponents())
		{
			if(c.getClass() == DocumentMenuItem.class)
			{
				if(((DocumentMenuItem) c).doc == doc)
				{
					toRemove = c;
					break;
				}
			}
		}
		if(toRemove != null)
		{
			documentsDrop.remove(toRemove);
			documentsDrop.revalidate();
			toRemove = null;
		}
		
		if(documents.getComponentCount() == 0)
		{
			infoBar.setVisible(false);
		}
	}
	
	public void checkButtonNames()
	{
		info.setSaved(Spade.getDocument().saved());
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			b.checkName();
		}
		documentBar.repaint();
	}
	
	public void checkDynamicInfo()
	{
		info.setScale(canvas.getScale());
		info.setMouseCoords(canvas.mouseX, canvas.mouseY);
	}
	
	public void repaint()
	{
		canvas.repaint();
	}
	
	public void setDocument(Document document)
	{
		canvas.setDocument(document);
		layers.setDocument(document);
		if(document != null)
		{
			info.setSize(document.getWidth(), document.getHeight());
			info.setSaved(document.saved());
			document.reconstructFlatmap();
			if(document.getFile() != null)
			{
				setTitle(IOUtils.relativeFrom(new File(System.getProperty("user.dir")), document.getFile()));
			}
			else
			{
				setTitle("Untitled - Spade");
			}
		}
		else
		{
			setTitle("Spade");
			info.clear();
		}
		DocumentButton select = null;
		// Set selected button.
		for(Component c : documents.getComponents())
		{
			DocumentButton b = (DocumentButton) c;
			if(b.doc == document)
			{
				b.setSelected(true);
				b.checkName();
				select = b;
			}
			else
			{
				b.setSelected(false);
				b.checkName();
			}
		}
		if(select != null)
			documents.add(select, 0);
		
		documentBar.repaint();
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
			URL url = Spade.class.getResource(fullPath);
			
			if(url == null)
				throw new IOException("ImageIcon Not found: " + fullPath);
			
			return new ImageIcon(ImageIO.read(url));
		}
		catch(IOException e)
		{
			try
			{
				return new ImageIcon(ImageIO.read(Spade.questionMarkURL));
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
