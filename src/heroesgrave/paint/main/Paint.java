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

package heroesgrave.paint.main;

import heroesgrave.paint.effects.Effect;
import heroesgrave.paint.gui.Effects;
import heroesgrave.paint.gui.GUIManager;
import heroesgrave.paint.gui.Tools;
import heroesgrave.paint.image.Document;
import heroesgrave.paint.io.ImageExporter;
import heroesgrave.paint.plugin.PluginManager;
import heroesgrave.paint.tools.Tool;
import heroesgrave.utils.app.Application;
import heroesgrave.utils.io.IOUtils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.rootpane.WebDialog;

//
// "Ninety-nine little bugs in the code, ninety-nine little bugs! Take one down, patch it around, one-hundred and seventeen bugs in the code!"
//       -Aoefanatic (/r/Minecraft)

public class Paint extends Application
{
	// Major.Minor + optional letter for releases. The letter is for tiny revisions, such as fixing bugs that slipped through.
	// Major.0-Beta for beta builds.
	// Major.Minor-Dev for development builds.
	// Eg: 1.3b is the 2nd revision of verion 1.3
	
	// Stable for stable post-1.0 versions
	// Beta for new completed features.
	// Development for under-development new features.
	
	public static final String VERSION = "0.14-Dev";
	public static final String RELEASED = "25/10/2014";
	
	/**/public static final String BUILD_TYPE = "Development";
	//*/public static final String BUILD_TYPE = "Beta";
	//*/public static final String BUILD_TYPE = "Stable";
	
	public static boolean debug;
	public static Paint main = new Paint();
	public static URL questionMarkURL = Paint.class.getResource("/heroesgrave/paint/res/icons/questionmark.png");
	
	public GUIManager gui;
	public PluginManager pluginManager;
	
	public Document document;
	
	private File toOpen;
	
	public Tool currentTool;
	
	public Tools tools;
	public Effects effects;
	
	public static int leftColour = 0xff000000;
	public static int rightColour = 0xffffffff;
	
	private static HashMap<String, Tool> toolMap = new HashMap<String, Tool>();
	
	private static HashMap<String, Effect> effectMap = new HashMap<String, Effect>();
	
	@Override
	public void init()
	{
		ImageExporter.registerExporters();
		
		pluginManager = PluginManager.instance(this);
		
		tools = new Tools();
		effects = new Effects();
		
		if(toOpen != null)
		{
			document = new Document(toOpen);
			toOpen = null;
		}
		else
		{
			document = new Document(512, 512);
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				gui = new GUIManager();
				gui.init();
				
				setRightColour(0xffffffff, false);
				setLeftColour(0xff000000, false);
				
				tools.registerTools();
				effects.registerEffects();
				setTool(currentTool);
				pluginManager.registerOther();
				gui.setDocument(document);
				
				Paint.main.gui.frame.requestFocus();
				pluginManager.onLaunch();
			}
		});
	}
	
	@Override
	public void update()
	{
		//gui.info.setSaved(saved);
		//gui.setFile(openFile);
	}
	
	@Override
	public void render()
	{
		
	}
	
	@Override
	public void dispose()
	{
		
	}
	
	public void newImage(final int width, final int height)
	{
		if(!document.saved)
		{
			final WebDialog newImage = new WebDialog(gui.frame, "Save current image?");
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
			newImage.setLocationRelativeTo(gui.frame);
			
			save.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Paint.save();
					newImage.dispose();
					createImage(width, height);
				}
			});
			dispose.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					newImage.dispose();
					createImage(width, height);
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				@Override
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
		this.document = new Document(width, height);
		gui.setDocument(document);
	}
	
	public static void addTool(String key, Tool tool)
	{
		toolMap.put(key.toLowerCase(), tool);
	}
	
	public static Tool getTool(String key)
	{
		return toolMap.get(key.toLowerCase());
	}
	
	public static void addEffect(String key, Effect op)
	{
		effectMap.put(key.toLowerCase(), op);
	}
	
	public static Effect getEffect(String key)
	{
		return effectMap.get(key.toLowerCase());
	}
	
	public static Document getDocument()
	{
		return main.document;
	}
	
	public static void setTool(Tool tool)
	{
		Input.CTRL = false;
		Input.ALT = false;
		Input.SHIFT = false;
		main.currentTool.onDeselect();
		main.currentTool = tool;
		main.currentTool.onSelect();
		main.gui.setToolOption(tool.getOptions());
		main.tools.toolbox.setSelected(tool);
	}
	
	public static void save()
	{
		if(getDocument().getFile() != null)
			getDocument().save();
		else
			main.saveAs();
	}
	
	public void saveAs()
	{
		WebFileChooser chooser = new WebFileChooser(document.getDir());
		chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		for(ImageExporter exporter : ImageExporter.exporters)
		{
			chooser.addChoosableFileFilter(exporter);
		}
		FileFilter allFilter = new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				if(f.isDirectory())
					return true;
				String fileName = f.getAbsolutePath();
				int i = fileName.lastIndexOf('.');
				if(i < 0)
					return false;
				
				return ImageExporter.get(fileName.substring(i + 1)) != null;
			}
			
			@Override
			public String getDescription()
			{
				return "All supported import formats";
			}
		};
		chooser.setFileFilter(allFilter);
		
		int returned = chooser.showSaveDialog(new WebDialog(main.gui.frame, "Save Image"));
		
		if(returned == WebFileChooser.APPROVE_OPTION)
		{
			// FIXME: The filechooser doesn't seem to change the filter to the one the user selected.
			// It always seems to fall through to the else branch.
			if(chooser.getFileFilter() instanceof ImageExporter)
			{
				ImageExporter format = (ImageExporter) chooser.getFileFilter();
				File file = chooser.getSelectedFile();
				
				String fileName = file.getAbsolutePath();
				
				if(fileName.endsWith("." + format.getFileExtension()))
				{
					// Do nothing.
					document.setFile(file);
				}
				else
				{
					// Put the format at the end of the File-Name!
					fileName += "." + format.getFileExtension();
					document.setFile(new File(fileName));
				}
			}
			else
			{
				File file = chooser.getSelectedFile();
				if(!allFilter.accept(file))
				{
					file = new File(file.getAbsolutePath() + ".png");
				}
				document.setFile(file);
			}
			
			document.save();
		}
	}
	
	public void setLeftColour(int c, boolean checked)
	{
		if(!checked)
		{
			gui.chooser.setLeftColour(c);
		}
		Paint.leftColour = c;
	}
	
	public int getLeftColour()
	{
		return Paint.leftColour;
	}
	
	public void setRightColour(int c, boolean checked)
	{
		if(!checked)
		{
			gui.chooser.setRightColour(c);
		}
		Paint.rightColour = c;
	}
	
	public int getRightColour()
	{
		return Paint.rightColour;
	}
	
	/**
	 * @param mouseButton The mouse-button to get the color for.
	 * @return The color assigned to the given MouseButton.
	 */
	public int getColor(int mouseButton)
	{
		// BUTTON1 (LEFT): left
		// BUTTON2 (MIDDLE): Color.BLACK
		// BUTTON3 (RIGHT): right
		return mouseButton == MouseEvent.BUTTON1 ? Paint.leftColour : (mouseButton == MouseEvent.BUTTON3 ? Paint.rightColour : 0xFF000000);
	}
	
	public static void main(String[] args)
	{
		IOUtils.setMainClass(Paint.class);
		
		// Check for a file argument
		if(args.length >= 1)
		{
			File f = new File(args[0]);
			
			if(f.exists() && f.isFile() && !f.isHidden())
			{
				main.toOpen = f;
			}
		}
		
		System.setProperty("DlafClassName", "");
		
		// Go through ALL the arguments and...
		for(String STR : args)
		{
			// Print version string and exit.
			if(STR.equalsIgnoreCase("-v"))
			{
				System.out.println(VERSION);
				System.exit(0);
			}
			
			// Print detailed version info and exit.
			if(STR.equalsIgnoreCase("-vv"))
			{
				System.out.println("Paint.JAVA v" + VERSION);
				System.out.println("Version Released: " + RELEASED);
				System.out.println("Built Type: " + BUILD_TYPE);
				System.exit(0);
			}
			
			// Print the absolute path of the jar and exit.
			if(STR.equalsIgnoreCase("-p"))
			{
				System.out.println(IOUtils.jarPath());
				System.exit(0);
			}
			
			// ...If the arguments contain the DmemoryWatcherFlag flag, set the property to true to enable the MemoryWatcher.
			if(STR.equalsIgnoreCase("DmemoryWatcherFlag"))
			{
				System.setProperty("DmemoryWatcherFlag", "true");
			}
			
			if(STR.startsWith("DlafClassName="))
			{
				System.setProperty("DlafClassName", STR.substring(14));
			}
			
			if(STR.equals("--debug"))
			{
				debug = true;
			}
			
			/// XXX: Expand here by adding more debugging options and system flags!
		}
		
		// Finally Launch Paint.JAVA!
		Application.launch(main);
	}
	
	public static String getVersionString()
	{
		return VERSION;
	}
	
	public static void setDocument(File file)
	{
		main.document = new Document(file);
		main.gui.layers.setDocument(main.document);
		main.gui.setDocument(main.document);
	}
}
