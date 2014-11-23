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

import heroesgrave.paint.editing.Effect;
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.gui.Effects;
import heroesgrave.paint.gui.GUIManager;
import heroesgrave.paint.gui.Tools;
import heroesgrave.paint.image.Document;
import heroesgrave.paint.io.HistoryIO;
import heroesgrave.paint.io.ImageExporter;
import heroesgrave.paint.plugin.Plugin;
import heroesgrave.paint.plugin.PluginManager;
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.misc.Callback;
import heroesgrave.utils.misc.Version;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.rootpane.WebDialog;

public class Paint
{
	// Major.Minor + optional letter for releases. The letter is for tiny revisions, such as fixing bugs that slipped through.
	// Major.Minor-RC.# for Release Candidates (builds that may be promoted to releases).
	// Major.Minor-Beta for beta builds.
	// Major.Minor-Alpha for alpha builds.
	// Major.Minor-Dev for development builds.
	// Eg: 1.3b is the 2nd revision of verion 1.3
	
	// Stable for stable post-1.0 versions
	// Beta for new completed features.
	// Development for under-development new features.
	
	public static final String VERSION_STRING = "0.15-Dev";
	public static final Version VERSION = Version.parse(VERSION_STRING);
	public static final String RELEASED = "23-11-2014";
	
	/* Add/Remove the stars on the following lines to change the build type string.
	//*/public static final String BUILD_TYPE = "Development";
	//*/public static final String BUILD_TYPE = "Alpha"; // I don't know if we'll ever use alphas.
	//*/public static final String BUILD_TYPE = "Beta";
	//*/public static final String BUILD_TYPE = "Release Candidate";
	//*/public static final String BUILD_TYPE = "Stable";
	
	public static boolean debug, localPlugins = true, globalPlugins = true;
	public static Paint main = new Paint();
	public static URL questionMarkURL = Paint.class.getResource("/res/icons/questionmark.png");
	
	public GUIManager gui;
	public PluginManager pluginManager;
	
	private Document document;
	
	public Tool currentTool;
	
	public Tools tools;
	public Effects effects;
	
	public static int leftColour = 0xff000000;
	public static int rightColour = 0xffffffff;
	
	private static HashMap<Character, Tool> toolMap = new HashMap<Character, Tool>();
	
	private static HashMap<Character, Effect> effectMap = new HashMap<Character, Effect>();
	
	public void launch(File toOpen)
	{
		pluginManager = PluginManager.instance;
		
		HistoryIO.init();
		
		// Order is important. Prefer local (./plugins) plugins to global (~/.paint-java/plugins).
		if(localPlugins)
			pluginManager.addPluginDirectory(new File(IOUtils.assemblePath(IOUtils.jarDir(), "plugins")));
		if(globalPlugins)
			pluginManager.addPluginDirectory(new File(IOUtils.assemblePath(System.getProperty("user.home"), ".paint-java", "plugins")));
		pluginManager.loadPluginFiles();
		
		tools = new Tools();
		effects = new Effects();
		
		final Document document;
		if(toOpen != null)
		{
			document = new Document(toOpen);
		}
		else
		{
			document = null;
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				gui = new GUIManager();
				gui.init();
				
				setLeftColour(0xff000000, false);
				setRightColour(0xffffffff, false);
				
				tools.init();
				effects.init(); // Doesn't actually do anything
				pluginManager.loadPlugins();
				setTool(currentTool);
				if(document != null)
				{
					Paint.addDocument(document);
					Paint.setDocument(document);
				}
				
				Paint.main.gui.frame.requestFocus();
			}
		});
	}
	
	public static void newImage(final int width, final int height)
	{
		Document doc = new Document(width, height);
		Paint.addDocument(doc);
		Paint.setDocument(doc);
	}
	
	public static void addTool(Character key, Tool tool)
	{
		toolMap.put(Character.toLowerCase(key), tool);
	}
	
	public static Tool getTool(Character key)
	{
		return toolMap.get(Character.toLowerCase(key));
	}
	
	public static void addEffect(Character key, Effect op)
	{
		effectMap.put(Character.toLowerCase(key), op);
	}
	
	public static Effect getEffect(Character key)
	{
		return effectMap.get(Character.toLowerCase(key));
	}
	
	public static void setTool(Tool tool)
	{
		Input.CTRL = false;
		Input.ALT = false;
		Input.SHIFT = false;
		main.currentTool = tool;
		main.tools.toolbox.setSelected(tool);
	}
	
	public static boolean save(Document doc)
	{
		if(doc == null)
		{
			return false;
		}
		if(doc.getFile() != null)
		{
			doc.save();
			return true;
		}
		else
			return saveAs(doc);
	}
	
	public static boolean saveAs(Document doc)
	{
		if(doc == null)
		{
			return false;
		}
		WebFileChooser chooser = new WebFileChooser(doc.getDir());
		chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		ImageExporter.addAllExporters(chooser);
		
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
					doc.setFile(file);
				}
				else
				{
					// Put the format at the end of the File-Name!
					fileName += "." + format.getFileExtension();
					doc.setFile(new File(fileName));
				}
			}
			else
			{
				File file = chooser.getSelectedFile();
				if(!allFilter.accept(file))
				{
					file = new File(file.getAbsolutePath() + ".png");
				}
				doc.setFile(file);
			}
			
			if(doc == main.document)
				main.gui.setTitle(doc.getFile().getName());
			doc.save();
			return true;
		}
		return false;
	}
	
	public void setLeftColour(int c, boolean checked)
	{
		if(!checked)
		{
			gui.chooser.setColour(c, this, true);
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
			gui.chooser.setColour(c, this, false);
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
	
	public static void launchWithPlugins(String[] args, Plugin... plugins)
	{
		for(Plugin p : plugins)
		{
			if(p.getInfo() == null)
			{
				try
				{
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getClass().getResourceAsStream("/plugin.info")));
					p.setInfo(PluginManager.loadPluginInfo(in));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			p.getInfo().set("location", "Class: " + p.getClass().getCanonicalName());
			PluginManager.instance.registerPlugin(p);
		}
		launch(args);
	}
	
	public static void launch(String[] args)
	{
		IOUtils.setMainClass(Paint.class);
		
		File open = null;
		
		// Check for a file argument
		if(args.length >= 1)
		{
			File f = new File(args[0]);
			
			if(f.exists() && f.isFile() && !f.isHidden())
			{
				open = f;
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
			if(STR.equalsIgnoreCase("--version"))
			{
				System.out.println("Paint.JAVA v" + VERSION);
				System.out.println("Version Released: " + RELEASED);
				System.out.println("Built Type: " + BUILD_TYPE);
				System.exit(0);
			}
			
			// Print the absolute path of the jar and exit.
			if(STR.equalsIgnoreCase("--print-jar-path"))
			{
				System.out.println(IOUtils.jarPath());
				System.exit(0);
			}
			
			// ...If the arguments contain the DmemoryWatcherFlag flag, set the property to true to enable the MemoryWatcher.
			if(STR.equalsIgnoreCase("--show-memory-watcher"))
			{
				System.setProperty("DmemoryWatcherFlag", "true");
			}
			
			if(STR.startsWith("--look-and-feel="))
			{
				System.setProperty("DlafClassName", STR.substring(16));
			}
			
			if(STR.equals("--debug"))
			{
				debug = true;
			}
			
			if(STR.equals("--no-global-plugins"))
			{
				globalPlugins = false;
			}
			
			if(STR.equals("--no-local-plugins"))
			{
				localPlugins = false;
			}
			
			/// XXX: Expand here by adding more debugging options and system flags!
		}
		
		// Finally Launch Paint.JAVA!
		main.launch(open);
	}
	
	public static Version getVersion()
	{
		return VERSION;
	}
	
	public static void addDocument(Document doc)
	{
		main.gui.addDocument(doc);
	}
	
	public static void setDocument(Document doc)
	{
		main.document = doc;
		main.gui.setDocument(main.document);
	}
	
	public static Document getDocument()
	{
		return main.document;
	}
	
	public static void closeAllDocuments()
	{
		Paint.closeDocument(Paint.getDocument(), new Callback()
		{
			public void callback()
			{
				Paint.closeAllDocuments();
			}
		});
	}
	
	public static void closeDocument(Document doc)
	{
		closeDocument(doc, new Callback()
		{
			public void callback()
			{
			}
		});
	}
	
	public static void closeDocument(final Document doc, final Callback callback)
	{
		if(main.gui.getDocuments().isEmpty())
		{
			Paint.close();
		}
		final int index = main.gui.getDocuments().indexOf(doc);
		main.gui.tryRemove(doc, new Callback()
		{
			public void callback()
			{
				ArrayList<Document> documents = main.gui.getDocuments();
				if(main.gui.getDocuments().isEmpty())
				{
					setDocument(null);
				}
				if(doc == main.document)
				{
					if(index == 0)
						setDocument(documents.get(0));
					else
						setDocument(documents.get(index - 1));
				}
				callback.callback();
			}
		});
	}
	
	public static void close()
	{
		UserPreferences.savePrefs(main.gui.frame, main.gui.chooser, main.gui.layers);
		main.pluginManager.dispose();
		main.gui.frame.dispose();
		System.exit(0);
	}
	
	public static String getDir()
	{
		if(main.document != null)
			return main.document.getDir();
		return System.getProperty("user.dir");
	}
}
