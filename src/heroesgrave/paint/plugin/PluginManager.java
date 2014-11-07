package heroesgrave.paint.plugin;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.Metadata;
import heroesgrave.utils.misc.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager
{
	public static final PluginManager instance = new PluginManager();
	
	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	private ArrayList<Plugin> validPlugins = new ArrayList<Plugin>();
	
	private ArrayList<File> pluginFiles = new ArrayList<File>();
	private ArrayList<Metadata> pluginFileInfo = new ArrayList<Metadata>();
	
	private PluginViewer pluginViewer;
	private URLClassLoader classLoader;
	
	public void loadPlugins()
	{
		for(Plugin plugin : validPlugins)
		{
			plugin.load();
		}
		Registrar registrar = new Registrar();
		for(Plugin plugin : validPlugins)
		{
			plugin.register(registrar);
			plugin.loaded = true;
		}
		registrar.completeRegistration(Paint.main.tools, Paint.main.effects);
	}
	
	public void dispose()
	{
		try
		{
			if(classLoader != null)
				classLoader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void registerPlugin(Plugin plugin)
	{
		if(validatePlugin(plugin))
		{
			System.out.println("[PluginManager] Loaded plugin: " + plugin.getInfo().get("name"));
			plugins.add(plugin);
			validPlugins.add(plugin);
		}
	}
	
	private boolean validatePlugin(Plugin plugin)
	{
		Metadata info = plugin.getInfo();
		if(info == null)
			return false;
		if(!info.has("name"))
		{
			System.err.println("[PluginManager] Unnamed plugin");
			return false;
		}
		String name = info.get("name");
		if(!info.has("version"))
		{
			System.err.println("[PluginManager] Plugin \"" + name + "\" has no version field.");
			return false;
		}
		if(info.has("min-paint-version"))
		{
			if(Version.parse(info.get("min-paint-version")).isGreater(Paint.getVersion()))
			{
				System.err.println("[PluginManager] Plugin \"" + name + "\" requires a newer version of Paint.JAVA");
				System.err.println("[PluginManager] Have: " + Paint.getVersion());
				System.err.println("[PluginManager] Need at least: " + Version.parse(info.get("min-paint-version")));
				plugins.add(plugin);
				return false;
			}
		}
		if(info.has("max-paint-version"))
		{
			if(Paint.getVersion().isGreater(Version.parse(info.get("max-paint-version"))))
			{
				System.err.println("[PluginManager] Plugin \"" + name + "\" requires an older version of Paint.JAVA");
				System.err.println("[PluginManager] Have: " + Paint.getVersion());
				System.err.println("[PluginManager] Need less than: " + Version.parse(info.get("max-paint-version")));
				plugins.add(plugin);
				return false;
			}
		}
		Version version = Version.parse(info.get("version"));
		Plugin replace = null;
		for(Plugin p : plugins)
		{
			if(p.getInfo().get("name").equals(info.get("name")))
			{
				Version otherv = Version.parse(p.getInfo().get("version"));
				if(version.isGreater(otherv))
				{
					System.err.println("[PluginManager] Multiple versions of plugin \"" + name + "\" detected.");
					System.err.printf("[PluginManager] Choosing the latest version (%s >= %s)\n", version, otherv);
					replace = p;
				}
				else
				{
					System.err.println("[PluginManager] Multiple versions of plugin \"" + name + "\" detected.");
					System.err.printf("[PluginManager] Choosing the latest version (%s >= %s)\n", otherv, version);
					plugins.add(plugin);
					return false;
				}
			}
		}
		
		if(replace != null)
		{
			plugins.remove(replace);
			plugins.add(replace);
		}
		return true;
	}
	
	public void loadPluginFiles()
	{
		URL[] urls = new URL[pluginFiles.size()];
		for(int i = 0; i < urls.length; i++)
		{
			try
			{
				urls[i] = pluginFiles.get(i).toURI().toURL();
			}
			catch(MalformedURLException e)
			{
				System.err.println("[PluginManager] Error loading plugin file: " + pluginFiles.get(i));
			}
		}
		classLoader = new URLClassLoader(urls, getClass().getClassLoader());
		
		for(Metadata info : pluginFileInfo)
		{
			try
			{
				@SuppressWarnings("unchecked")
				Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) classLoader.loadClass(info.get("main"));
				Plugin plugin = pluginClass.newInstance();
				
				plugin.setInfo(info);
				registerPlugin(plugin);
				
				continue;
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(InstantiationException e)
			{
				e.printStackTrace();
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}
			System.err.println("[PluginManager] Error creating plugin instance for " + info.get("main"));
		}
		
		pluginFiles.clear();
		pluginFileInfo.clear();
	}
	
	private boolean loadPluginInfo(File file)
	{
		try(JarFile jar = new JarFile(file))
		{
			JarEntry entry = jar.getJarEntry("plugin.info");
			if(entry == null)
			{
				System.err.println("[PluginManager] Plugin file " + file + " is missing a 'plugin.info' file. Perhaps it is corrupted?");
				return false;
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));
			
			Metadata info = new Metadata();
			
			String line;
			while((line = in.readLine()) != null)
			{
				line = line.trim();
				if(line.equals(""))
					continue;
				String[] splits = line.split(":", 2);
				if(splits.length != 2)
				{
					System.err.println("[PluginManager] Error reading 'plugin.info'. Metadata must be stored in the format \"<key> : <value>\"");
					System.err.println("[PluginManager] Plugin File: " + file);
					System.err.println("[PluginManager] Line: " + line);
					return false;
				}
				info.set(splits[0].trim(), splits[1].trim());
			}
			
			if(info.has("main"))
			{
				pluginFileInfo.add(info);
			}
			else
			{
				System.err.println("[PluginManager] plugin.info is missing a 'main' key and so cannot be loaded. Source: " + file);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public void addPluginFile(File file)
	{
		if(file.exists() && !file.isDirectory())
		{
			String filename = file.getAbsolutePath();
			if(filename.endsWith(".jar") || filename.endsWith(".plugin"))
			{
				pluginFiles.add(file);
				loadPluginInfo(file);
			}
		}
	}
	
	public void addPluginDirectory(File file)
	{
		if(file.exists() && file.isDirectory())
		{
			for(File f : file.listFiles())
			{
				addPluginFile(f);
			}
		}
	}
	
	public ArrayList<Plugin> getPlugins()
	{
		return plugins;
	}
	
	public int getPluginCount()
	{
		return plugins.size();
	}
	
	public void showPluginViewer()
	{
		if(pluginViewer == null)
			pluginViewer = new PluginViewer(this);
		pluginViewer.show();
	}
}
