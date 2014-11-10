package heroesgrave.paint.plugin;

import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.Metadata;

public abstract class Plugin
{
	private Metadata info;
	boolean loaded;
	
	public Metadata getInfo()
	{
		return info;
	}
	
	public void setInfo(Metadata info)
	{
		this.info = info;
	}
	
	public Plugin()
	{
		
	}
	
	public abstract void load();
	
	public abstract void register(Registrar registrar);
	
	public boolean isLoaded()
	{
		return loaded;
	}
	
	protected static void launchPaintWithPlugins(String[] args, boolean dev, Plugin... plugins)
	{
		if(dev)
		{
			try
			{
				System.out.println("Launching Paint.JAVA v" + Paint.getVersion() + " in plugin development mode");
			}
			catch(NoClassDefFoundError e)
			{
				System.err.println("Attempted to launch Paint.JAVA in plugin development mode but Paint.JAVA could not be found.");
				System.err.println("Ensure your dev environment is set up properly.");
				System.exit(-1);
			}
		}
		Paint.launchWithPlugins(args, plugins);
	}
}
