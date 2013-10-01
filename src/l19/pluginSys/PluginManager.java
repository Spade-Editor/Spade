package l19.pluginSys;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFrame;
import javax.swing.JMenu;

public class PluginManager
{
	public static PluginManager instance = null;
	private File pluginRootDirectory;
	private ArrayList<PluginBase> loadedPlugins;
	
	public PluginManager(Paint paint)
	{
		// Make sure the plugin folder exists and is accessible!
		pluginRootDirectory = new File(IOUtils.assemblePath(IOUtils.jarDir(), "plugins"));
		
		if(!pluginRootDirectory.exists())
			pluginRootDirectory.mkdirs();
		
		System.out.println("[PluginManager] PluginManager initialized. Plugin-Folder is:" + pluginRootDirectory.getAbsolutePath());
		
		// 
		loadedPlugins = new ArrayList<PluginBase>();
		
		File[] possiblePluginRoots = pluginRootDirectory.listFiles();
		
		System.out.println("[PluginManager] Scanning Plugin directory for plugins...");
		
		for(File possiblePluginRoot : possiblePluginRoots)
		{
			if(possiblePluginRoot.isDirectory())
				handlePossibleDirectoryBasedPlugin(possiblePluginRoot);
			else if(possiblePluginRoot.isFile() && possiblePluginRoot.getName().endsWith(".jar"))
				handlePossibleJarBasedPlugin(possiblePluginRoot);
			else
				System.out
						.println("[PluginManager] The file '" + possiblePluginRoot.getAbsolutePath() + "' is in the plugin folder, but it is not a plugin!");
		}
		
		handlePossibleDirectoryBasedPlugin(new File("C:\\Develope\\workspace_java\\Paint.JAVA.TestPlugin\\bin"));
		
		System.out.println("[PluginManager] Done scanning for plugins!");
		
		System.out.println("[PluginManager] Invoking all plugins now using '" + paint + "'...");
		
		for(PluginBase plugin : this.loadedPlugins)
		{
			plugin.init(paint);
		}
		
		System.out.println("[PluginManager] All plugins loaded, ready for work!");
		
	}
	
	private void handlePossibleJarBasedPlugin(File possiblePluginRoot)
	{
		System.out.println("[PluginManager] Found possible Jar-plugin! Checking now... @" + possiblePluginRoot.getAbsolutePath());
		
		try
		{
			// Open JAR
			JarFile jarFile = new JarFile(possiblePluginRoot);
			
			// Get name of Jar without the ".jar"
			String jarName = possiblePluginRoot.getName().substring(0, possiblePluginRoot.getName().length() - 4);
			
			// (Make the name uppercase if it isn't already)
			if(Character.isLowerCase(jarName.charAt(0)))
			{
				jarName = Character.toUpperCase(jarName.charAt(0)) + jarName.substring(1);
			}
			
			// Try to get the plugin-info file from the Jar-File! If not possible, stop here.
			JarEntry pluginInfoEntry = jarFile.getJarEntry("plugin.info");
			
			if(pluginInfoEntry == null)
			{
				System.out.println("[PluginManager] Jar-File is not a Plugin! Cannot FIND 'plugin.info' file!");
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
				System.out.println("[PluginManager] Jar-File is not a Plugin! Cannot READ 'plugin.info' file!");
				jarFile.close();
				return;
			}
			
			// Check if the plugin-info contains the main-class key! If not, stop here.
			if(!props.containsKey("main"))
			{
				System.out.println("[PluginManager] 'plugin.info'-file is invalid! Key 'main' (Plugin Main Class Name) is missing!");
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
				String className = je.getName().substring(0, je.getName().length() - 6);
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
					if(PluginBase.class.isAssignableFrom(c))
					{
						
						// The class is a Plugin main-class!
						// Cast it into the right type now...
						Class<? extends PluginBase> pluginClass = c.asSubclass(PluginBase.class);
						
						// Then try to instantiate it...
						try
						{
							PluginBase newPluginInstance = pluginClass.newInstance();
							this.loadedPlugins.add(newPluginInstance);
							System.out.println("[PluginManager] Found Plugin-class '" + c.getName() + "'! Instanced and added to plugin-list.");
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
			System.out.println("[PluginManager] The file '" + possiblePluginRoot.getAbsolutePath() + "' looks like a Jar-based plugin, but it is not one!");
			e.printStackTrace();
			return;
		}
	}
	
	private void handlePossibleDirectoryBasedPlugin(File possiblePluginRoot)
	{
		
		// This does NOT work like this.
		// Try this again later with a fresh mind on this.
		
		// Let it stop here please using some simple code.
		if(Boolean.parseBoolean("true"))
			return;
		
		File[] possibleClassFiles = possiblePluginRoot.listFiles();
		
		// Load JAR?
		URL[] urls = new URL[0];
		try
		{
			urls = new URL[]{possiblePluginRoot.toURI().toURL()};
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		// This should not be closed (or should it?)
		URLClassLoader cl = new URLClassLoader(urls);
		
		for(File possibleClassFile : possibleClassFiles)
		{
			if(possibleClassFile.isFile() && possibleClassFile.getName().endsWith(".class"))
			{
				System.out.println("[PluginManager] Found possible Plugin-class to check: " + possibleClassFile.getAbsolutePath());
				
				String className = possibleClassFile.getName().substring(0, possibleClassFile.getName().length() - 6);
				className = className.replace('/', '.');
				
				try
				{
					cl.loadClass(className);
				}
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public static PluginManager instance(Paint paint)
	{
		if(instance == null)
			instance = new PluginManager(paint);
		
		return instance;
	}
	
	public void toolRegistrationEvent(JMenu menu)
	{
		System.out.println("[Event] Tool-Menu creation.");
		
		for(PluginBase plugin : loadedPlugins)
		{
			plugin.toolRegistration(menu);
		}
	}
	
	public void imageopRegistrationEvent(JMenu menu)
	{
		System.out.println("[Event] ImageOP-Menu creation.");
		
		for(PluginBase plugin : loadedPlugins)
		{
			plugin.imageopRegistration(menu);
		}
	}
	
	public void filemenuRegistrationEvent(JMenu menu)
	{
		System.out.println("[Event] File-Menu creation.");
		
		for(PluginBase plugin : loadedPlugins)
		{
			plugin.filemenuRegistration(menu);
		}
		
	}
	
	public void frameCreationEvent(JFrame frame)
	{
		System.out.println("[Event] Frame creation.");
		
	}
	
}
