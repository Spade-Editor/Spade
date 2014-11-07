package heroesgrave.paint.plugin;

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
}
