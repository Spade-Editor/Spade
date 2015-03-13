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

package heroesgrave.spade.main;

import heroesgrave.spade.editing.Effect;
import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.gui.Effects;
import heroesgrave.spade.gui.GUIManager;
import heroesgrave.spade.gui.Tools;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.io.HistoryIO;
import heroesgrave.spade.io.ImageExporter;
import heroesgrave.spade.plugin.Plugin;
import heroesgrave.spade.plugin.PluginManager;
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.misc.Callback;
import heroesgrave.utils.misc.Version;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.rootpane.WebDialog;

public class Spade
{
	public static final String REPO_URL = "https://github.com/Spade-Editor/Spade";
	
	// Major.Minor + optional letter for releases. The letter is for tiny revisions, such as fixing bugs that slipped through.
	// Major.Minor-RC.# for Release Candidates (builds that may be promoted to releases).
	// Major.Minor-Beta for beta builds.
	// Major.Minor-Alpha for alpha builds.
	// Major.Minor-Dev for development builds.
	// Eg: 1.3b is the 2nd revision of version 1.3
	
	// Stable for stable post-1.0 versions
	// Beta for new completed features.
	// Development for under-development new features.
	
	public static final String VERSION_STRING = "0.17.0-dev";
	public static final Version VERSION = Version.parse(VERSION_STRING);
	public static final String RELEASED = "13-03-2015";
	
	/* Add/Remove the stars on the following lines to change the build type string.
	//*/public static final String BUILD_TYPE = "Development";
	//*/public static final String BUILD_TYPE = "Alpha"; // I don't know if we'll ever use alphas.
	//*/public static final String BUILD_TYPE = "Beta";
	//*/public static final String BUILD_TYPE = "Release Candidate";
	//*/public static final String BUILD_TYPE = "Stable";
	
	public static boolean debug, localPlugins = true, globalPlugins = true;
	public static Spade main = new Spade();
	public static URL questionMarkURL = Spade.class.getResource("/res/icons/questionmark.png");
	
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
	
	public void launch(final ArrayList<File> toOpen) throws InvocationTargetException, InterruptedException
	{
		pluginManager = PluginManager.instance;
		
		HistoryIO.init();
		
		// Order is important. Prefer local (./spade) plugins to global (~/.spade/plugins).
		if(localPlugins)
			pluginManager.addPluginDirectory(new File(IOUtils.assemblePath(IOUtils.jarDir(), "plugins")));
		if(globalPlugins)
			pluginManager.addPluginDirectory(new File(IOUtils.assemblePath(System.getProperty("user.home"), ".spade", "plugins")));
		pluginManager.loadPluginFiles();
		
		tools = new Tools();
		effects = new Effects();
		
		SwingUtilities.invokeAndWait(new Runnable()
		{
			@Override
			public void run()
			{
				// Try to catch everything and recover enough to save.
				try
				{
					gui = new GUIManager();
					gui.init();
					
					setLeftColour(0xff000000, false);
					setRightColour(0xffffffff, false);
					
					tools.init();
					effects.init(); // Doesn't actually do anything
					pluginManager.loadPlugins();
					setTool(currentTool);
					for(File f : toOpen)
					{
						if(debug)
							System.out.println("Opening File " + f.getPath());
						Document d = Document.loadFromFile(f);
						if(d != null)
							Spade.addDocument(d);
					}
					ArrayList<Document> documents = gui.getDocuments();
					if(!documents.isEmpty())
						Spade.setDocument(documents.get(0));
					
					gui.frame.requestFocus();
				}
				catch(Exception e)
				{
					panic(e);
				}
			}
		});
	}
	
	public static void newImage(final int width, final int height)
	{
		Document doc = new Document(width, height);
		Spade.addDocument(doc);
		Spade.setDocument(doc);
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
	
	public static boolean setTool(Tool tool)
	{
		if(main.currentTool == tool)
			return false;
		Input.CTRL = false;
		Input.ALT = false;
		Input.SHIFT = false;
		main.currentTool = tool;
		main.tools.toolbox.setSelected(tool);
		return true;
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
		Spade.leftColour = c;
	}
	
	public int getLeftColour()
	{
		return Spade.leftColour;
	}
	
	public void setRightColour(int c, boolean checked)
	{
		if(!checked)
		{
			gui.chooser.setColour(c, this, false);
		}
		Spade.rightColour = c;
	}
	
	public int getRightColour()
	{
		return Spade.rightColour;
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
		return mouseButton == MouseEvent.BUTTON1 ? Spade.leftColour : (mouseButton == MouseEvent.BUTTON3 ? Spade.rightColour : 0xFF000000);
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
		IOUtils.setMainClass(Spade.class);
		
		ArrayList<File> open = new ArrayList<File>();
		
		System.setProperty("DlafClassName", "");
		
		StringBuilder argsDebug = new StringBuilder();
		
		// Go through ALL the arguments and...
		for(String arg : args)
		{
			argsDebug.append("Picked up argument: " + arg + "\n");
			
			if(arg.equals("-v"))
			{
				// Print version string and exit.
				System.out.println(VERSION);
				System.exit(0);
			}
			else if(arg.equals("--version"))
			{
				// Print detailed version info and exit.
				System.out.println("Spade v" + VERSION);
				System.out.println("Version Released: " + RELEASED);
				System.out.println("Built Type: " + BUILD_TYPE);
				System.exit(0);
			}
			else if(arg.equals("--print-jar-path"))
			{
				// Print the absolute path of the jar and exit.
				System.out.println(IOUtils.jarPath());
				System.exit(0);
			}
			else if(arg.equals("--show-memory-watcher"))
			{
				// ...If the arguments contain the DmemoryWatcherFlag flag, set the property to true to enable the MemoryWatcher.
				System.setProperty("DmemoryWatcherFlag", "true");
			}
			else if(arg.startsWith("--look-and-feel="))
			{
				System.setProperty("DlafClassName", arg.substring(16));
			}
			else if(arg.equals("--debug"))
			{
				debug = true;
			}
			else if(arg.equals("--no-global-plugins"))
			{
				globalPlugins = false;
			}
			else if(arg.equals("--no-local-plugins"))
			{
				localPlugins = false;
			}
			else if(arg.equals("--help"))
			{
				System.out.println("Spade v" + VERSION);
				System.out.println("Version Released: " + RELEASED);
				System.out.println("Built Type: " + BUILD_TYPE);
				System.out.println();
				System.out.println("Copyright 2013-2014 HeroesGrave and Other Spade Developers");
				System.out.println();
				System.out.println("For more info, to report issues, make suggestions, or contribute");
				System.out.println("please visit " + REPO_URL);
				System.out.println();
				System.out.println("Command-Line Options:");
				System.out.println();
				System.out.println("    Information:");
				System.out.println("    --help:                  Print this text and exit");
				System.out.println("    -v                       Print the raw version id and exit");
				System.out.println("    --version                Print more detailed version info and exit");
				System.out.println();
				System.out.println("    Plugin Settings:");
				System.out.println("    --no-local-plugins       Disable Local Plugins (./plugins/)");
				System.out.println("    --no-global-plugins      Disable Global Plugins (~/.spade/.plugins/)");
				System.out.println();
				System.out.println("    UI Settings:");
				System.out.println("    --look-and-feel=[LAF]    Specify the 'Look And Feel' to use");
				System.out.println("    --show-memory-watcher    Enable the memory usage watcher");
				System.out.println();
				System.out.println("    Debug Switches:");
				System.out.println("    --debug                  Enable debugging messages");
				System.out.println("    --debug-timings          Print rendering times");
				System.out.println();
				System.out.println("Any other arguments will be read as files to open on startup.");
				System.exit(0);
			}
			else
			{
				File f = new File(arg);
				
				if(f.exists() && f.isFile() && !f.isHidden())
				{
					open.add(f);
				}
			}
			
			/// XXX: Expand here by adding more debugging options and system flags!
		}
		
		if(debug)
			System.out.println(argsDebug);
		
		// Try to catch all exceptions and recover enough to save
		try
		{
			// Finally Launch Spade!
			main.launch(open);
		}
		catch(Exception e)
		{
			panic(e);
		}
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
		Spade.closeDocument(Spade.getDocument(), new Callback()
		{
			@Override
			public void callback()
			{
				Spade.closeAllDocuments();
			}
		});
	}
	
	public static void closeDocument(Document doc)
	{
		closeDocument(doc, new Callback()
		{
			@Override
			public void callback()
			{
			}
		});
	}
	
	public static void closeDocument(final Document doc, final Callback callback)
	{
		if(main.gui.getDocuments().isEmpty())
		{
			Spade.close();
		}
		final int index = main.gui.getDocuments().indexOf(doc);
		main.gui.tryRemove(doc, new Callback()
		{
			@Override
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
	
	public static void panic(Exception e)
	{
		try {
			ArrayList<Document> documents = main.gui.getDocuments();
			for(Document doc : documents)
			{
				File f = doc.getFile();
				if(f == null)
					continue;
				String name = "~" + f.getName() + ".bck";
				int i = 0;
				while(f.exists())
				{
					f = new File(f.getParentFile(), name + "." + i++);
				}
				doc.setFile(f);
				doc.save(true);
			}
			main.pluginManager.dispose();
			main.gui.frame.dispose();
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
			System.err.println("Exception encountered while handling an exception!\nYou will not go to space today.\nOriginal Error:");
			e.printStackTrace();
			System.exit(-1);
		}
		Popup.showException("Spade has crashed", e, "");
		System.exit(-1);
	}
}
