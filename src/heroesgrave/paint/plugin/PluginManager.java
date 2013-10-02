/*
 *	Copyright 2013 Longor1996 & HeroesGrave
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

package heroesgrave.paint.plugin;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.Popup;
import heroesgrave.utils.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFrame;
import javax.swing.JMenu;

/**
 * 
 * @author Longor1996 & HeroesGrave
 *
 */
public class PluginManager
{
	public static PluginManager instance = null;
	private File pluginRootDirectory;
	private ArrayList<Plugin> loadedPlugins;
	
	public PluginManager(Paint paint)
	{
		// Make sure the plugin folder exists and is accessible!
		pluginRootDirectory = new File(IOUtils.assemblePath(IOUtils.jarDir(), "plugins"));
		
		if(!pluginRootDirectory.exists())
			pluginRootDirectory.mkdirs();
		
		// 
		loadedPlugins = new ArrayList<Plugin>();
		
		File[] possiblePluginRoots = pluginRootDirectory.listFiles();
		
		System.out.println("[PluginManager] Searching for Plugins...");
		
		for(File possiblePluginRoot : possiblePluginRoots)
		{
			if(possiblePluginRoot.isFile() && possiblePluginRoot.getName().endsWith(".jar"))
				handlePossibleJarBasedPlugin(possiblePluginRoot);
		}
		
		System.out.println("[PluginManager] Done searching. Found " + loadedPlugins.size() + " plugins.");
		
		System.out.println("[PluginManager] Loading Plugins...");
		
		for(Plugin plugin : this.loadedPlugins)
		{
			plugin.init(paint);
		}
		
		System.out.println("[PluginManager] All plugins loaded.");
	}
	
	private void handlePossibleJarBasedPlugin(File possiblePluginRoot)
	{
		//System.out.println("[PluginManager] Found possible Jar-plugin! Checking now... @" + possiblePluginRoot.getAbsolutePath());
		
		try
		{
			// Open JAR
			JarFile jarFile = new JarFile(possiblePluginRoot);
			
			// Get name of Jar without the ".jar"
			String jarName = possiblePluginRoot.getName().replace(".jar", "");
			
			// (Make the name uppercase if it isn't already)
			if(Character.isLowerCase(jarName.charAt(0)))
			{
				jarName = Character.toUpperCase(jarName.charAt(0)) + jarName.substring(1);
			}
			
			// Try to get the plugin-info file from the Jar-File! If not possible, stop here.
			JarEntry pluginInfoEntry = jarFile.getJarEntry("plugin.info");
			
			if(pluginInfoEntry == null)
			{
				//System.out.println("[PluginManager] " + possiblePluginRoot.getName() + " is not a Plugin! Cannot FIND 'plugin.info' file!");
				jarFile.close();
				return;
			}
			
			Properties props = new Properties();
			
			// Load the plugin information file! If not possible, stop here.
			try
			{
				props.load(jarFile.getInputStream(pluginInfoEntry));
			}
			catch(Exception e3)
			{
				e3.printStackTrace();
				Popup.show("Plugin Load Error", "[PluginManager] Cannot READ 'plugin.info' file!\nThe Plugin may be corrupt, or is not a plugin at all");
				jarFile.close();
				return;
			}
			
			// Check if the plugin-info contains the main-class key! If not, stop here.
			if(!props.containsKey("main"))
			{
				Popup.show("Plugin Load Error", "[PluginManager] 'plugin.info'-file is invalid!\n"
						+ "Key 'main' (Plugin Main Class Name) is missing!\nThe Plugin may be corrupt");
				jarFile.close();
				return;
			}
			
			// Get Main-Class name!
			String mainClassName = props.getProperty("main");
			
			// List Entries
			Enumeration<JarEntry> e = jarFile.entries();
			
			// Load JAR?
			URL[] urls = {new URL("jar:file:" + possiblePluginRoot.getAbsolutePath() + "!/")};
			
			// This should not be closed (or should it?)
			URLClassLoader cl = new URLClassLoader(urls);
			
			while(e.hasMoreElements())
			{
				JarEntry je = (JarEntry) e.nextElement();
				
				if(je.isDirectory() || !je.getName().endsWith(".class"))
				{
					continue;
				}
				
				// Get the actual class-name!
				// -6 because of .class
				String className = je.getName().replace(".class", "");
				className = className.replace('/', '.');
				
				// If this is not the main-class, skip it! This speeds up the time it takes to load the plugin,
				// since we don't load all classes at once, but rather by just loading the main-class and then
				// letting the JVM-SystemClassLoader do its work. (Its faster this way!)
				if(!className.equals(mainClassName))
				{
					continue;
				}
				
				// Try to load the class!
				try
				{
					//Load
					Class<?> c = cl.loadClass(className);
					
					// Check if the class is assignable from PluginBase (Is it a plugin?)
					if(Plugin.class.isAssignableFrom(c))
					{
						// The class is a Plugin main-class!
						// Cast it into the right type now...
						Class<? extends Plugin> pluginClass = c.asSubclass(Plugin.class);
						
						// Then try to instantiate it...
						try
						{
							Plugin newPluginInstance = pluginClass.newInstance();
							this.loadedPlugins.add(newPluginInstance);
							
							System.out.println("[PluginManager] Plugin " + newPluginInstance.name + " loaded.");
							
							// Create info object.
							newPluginInstance.info = new Properties();
							
							// Format 'Size'
							newPluginInstance.info.put("size", humanReadableByteCount(possiblePluginRoot.length(), true));
							
							// Format 'Description'
							newPluginInstance.info.put("description", ((String) props.get("description")).replace("\\n", "\n"));
							
							// Author
							newPluginInstance.info.put("author", ((String) props.get("author")));
							// Version (defined by author)
							newPluginInstance.info.put("version", ((String) props.get("version")));
							// Date updated (defined by author)
							newPluginInstance.info.put("updated", ((String) props.get("updated")));
						}
						catch(ReflectiveOperationException e1)
						{
							e1.printStackTrace();
						}
					}
				}
				catch(ClassNotFoundException e1)
				{
					e1.printStackTrace();
				}
			}
			
			// Close JAR
			jarFile.close();
		}
		catch(IOException e)
		{
			//System.out.println("[PluginManager] The file '" + possiblePluginRoot.getAbsolutePath() + "' looks like a Jar-based plugin, but it is not one!");
			e.printStackTrace();
			return;
		}
	}
	
	private static String humanReadableByteCount(long bytes, boolean si)
	{
		int unit = si ? 1000 : 1024;
		if(bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static PluginManager instance(Paint paint)
	{
		if(instance == null)
			instance = new PluginManager(paint);
		
		return instance;
	}
	
	public void registerTools(JMenu menu)
	{
		//System.out.println("[Event] Tool-Menu creation.");
		RegisterTools register = new RegisterTools(menu);
		
		for(Plugin plugin : loadedPlugins)
		{
			plugin.registerTools(register);
		}
	}
	
	public void registerImageOps(JMenu menu)
	{
		//System.out.println("[Event] ImageOP-Menu creation.");
		RegisterImageOps register = new RegisterImageOps(menu);
		
		for(Plugin plugin : loadedPlugins)
		{
			plugin.registerImageOps(register);
		}
	}
	
	public void frameCreationEvent(JFrame frame)
	{
		//System.out.println("[Event] Frame creation.");
	}
	
	public void onLaunch()
	{
		for(Plugin plugin : loadedPlugins)
		{
			plugin.onLaunch();
		}
	}
	
	public void showPluginManager()
	{
		new PluginManagerViewer().show(this);
	}
	
	public ArrayList<Plugin> getPluginList()
	{
		return this.loadedPlugins;
	}
	
}