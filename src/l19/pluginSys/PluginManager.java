package l19.pluginSys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import l19.util.WorkingDirectory;

import heroesgrave.paint.main.Paint;

public class PluginManager {
	public static PluginManager instance = null;
	private Paint appInstance;
	private File pluginRootDirectory;
	private ArrayList<PluginBase> loadedPlugins;

	public PluginManager(Paint paint){
		appInstance = paint;
		
		// Make sure the plugin folder exists and is accessible!
		pluginRootDirectory = new File(WorkingDirectory.getWorkingDirectoryFor("Paint.JAVA"),"plugins");
		
		if(!pluginRootDirectory.exists())
			pluginRootDirectory.mkdirs();
		
		System.out.println("[PluginManager] PluginManager initialized. Plugin-Folder is:" + pluginRootDirectory.getAbsolutePath());
		
		// 
		loadedPlugins = new ArrayList<PluginBase>();
		
		File[] possiblePluginRoots = pluginRootDirectory.listFiles();
		
		System.out.println("[PluginManager] Scanning Plugin directory for plugins...");
		
		for(File possiblePluginRoot : possiblePluginRoots){
			
			if(possiblePluginRoot.isDirectory())
				handlePossibleDirectoryBasedPlugin(possiblePluginRoot);
			else if(possiblePluginRoot.isFile() && possiblePluginRoot.getName().endsWith(".jar"))
				handlePossibleJarBasedPlugin(possiblePluginRoot);
			else 
				System.out.println("[PluginManager] The file '"+possiblePluginRoot.getAbsolutePath()+"' is in the plugin folder, but it is not a plugin!");
			
		}
		
		handlePossibleDirectoryBasedPlugin(new File("C:\\Develope\\workspace_java\\Paint.JAVA.TestPlugin\\bin"));
		
		
		System.out.println("[PluginManager] Done scanning for plugins!");
		
		System.out.println("[PluginManager] Invoking all plugins now using '"+paint+"'...");
		
		for(PluginBase plugin : this.loadedPlugins){
			plugin.init(paint);
		}
		
		System.out.println("[PluginManager] All plugins loaded, ready for work!");
		
	}
	
	private void handlePossibleJarBasedPlugin(File possiblePluginRoot) {
		System.out.println("[PluginManager] Found possible Jar-plugin! Checking now... @"+possiblePluginRoot.getAbsolutePath());
		
		try {
			// Open JAR
			JarFile jarFile = new JarFile(possiblePluginRoot);
			
			// List Entries
			Enumeration<JarEntry> e = jarFile.entries();
			
			// Load JAR?
            URL[] urls = { new URL("jar:file:" + possiblePluginRoot.getAbsolutePath()+"!/") };
            
            // This should not be closed (or should it?)
            @SuppressWarnings("resource")
			URLClassLoader cl = new URLClassLoader(urls);
            
            while (e.hasMoreElements()) {
                JarEntry je = (JarEntry) e.nextElement();
                
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                
                // Get the actual class-name!
                // -6 because of .class
                String className = je.getName().substring(0,je.getName().length()-6);
                className = className.replace('/', '.');
                
                // Try to load the class !
                try {
                	//Load
					Class<?> c = cl.loadClass(className);
					
					// Check if the class is assignable from PluginBase (Is it a plugin?)
					if(PluginBase.class.isAssignableFrom(c)){
						
						// The class is a Plugin main-class!
						// Cast it into the right type now...
						Class<? extends PluginBase> pluginClass = c.asSubclass(PluginBase.class);
						
						// Then try to instantiate it...
						try {
							PluginBase newPluginInstance = pluginClass.newInstance();
							this.loadedPlugins.add(newPluginInstance);
							System.out.println("[PluginManager] Found Plugin-class '" + c.getName() + "'! Instanced and added to plugin-list.");
						} catch (ReflectiveOperationException e1) {
							e1.printStackTrace();
						}
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
            }
            
            // Close JAR
            jarFile.close();
		} catch (IOException e) {
			System.out.println("[PluginManager] The file '"+possiblePluginRoot.getAbsolutePath()+"' looks like a Jar-based plugin, but it is not one!");
			e.printStackTrace();
			return;
		}
	}
	
	private void handlePossibleDirectoryBasedPlugin(File possiblePluginRoot) {
		
		// This does NOT work like this.
		// Try this again later with a fresh mind on this.
		
		
		// Let it stop here please using some simple code.
		if(Boolean.parseBoolean("true"))
			return;
		
		
		
		
		
		
		
		
		File[] possibleClassFiles = possiblePluginRoot.listFiles();
		
		// Load JAR?
        URL[] urls = new URL[0];
		try {
			urls = new URL[] { possiblePluginRoot.toURI().toURL() };
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        
		
		
        // This should not be closed (or should it?)
        @SuppressWarnings("resource")
		URLClassLoader cl = new URLClassLoader(urls);
        
		for(File possibleClassFile : possibleClassFiles){
			if(possibleClassFile.isFile() && possibleClassFile.getName().endsWith(".class")){
				System.out.println("[PluginManager] Found possible Plugin-class to check: " + possibleClassFile.getAbsolutePath());
				
                String className = possibleClassFile.getName().substring(0,possibleClassFile.getName().length()-6);
                className = className.replace('/', '.');
                
				try {
					cl.loadClass(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public static PluginManager instance(Paint paint) {
		if(instance == null)
			instance = new PluginManager(paint);
		
		return instance;
	}
	
}
