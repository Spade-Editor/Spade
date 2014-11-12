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
