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

package heroesgrave.utils.misc;

import heroesgrave.utils.io.IOUtils;

public class Version
{
	private int major, minor, patch;
	private String type;
	
	public Version(int major, int minor, int patch)
	{
		this(major, minor, patch, null);
	}
	
	public Version(int major, int minor, int patch, String type)
	{
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.type = type;
	}
	
	public Version(String string)
	{
		this.major = -1;
		this.minor = -1;
		this.patch = 0;
		this.type = string;
	}
	
	public boolean isGreater(Version other)
	{
		if(this.major < other.major)
			return false;
		else if(this.major > other.major)
			return true;
		else
		{
			if(this.minor < other.minor)
				return false;
			else if(this.minor > other.minor)
				return true;
			else
			{
				if(this.patch < other.patch)
					return false;
				else if(this.patch > other.patch)
					return true;
				else
				{
					if(this.type() <= other.type())
						return false;
					else
						return true;
				}
			}
		}
	}
	
	private int type()
	{
		if(this.type == null)
			return 16;
		if(this.type.startsWith("RC."))
		{
			return 3 + Integer.parseInt(this.type.substring(3));
		}
		switch(this.type)
		{
			case "Dev":
				return 1;
			case "Alpha":
				return 2;
			case "Beta":
				return 3;
			default:
				return 15;
		}
	}
	
	public static Version parse(String version)
	{
		if(version.startsWith("\"") && version.endsWith("\""))
		{
			return new Version(version.substring(1, version.length() - 1));
		}
		try
		{
			String[] dot = version.split("\\.");
			String[] dash = dot[2].split("-");
			
			String major = dot[0];
			String minor = dot[1];
			String patch = dash[0];
			String type = null;
			
			if(dash.length == 2)
			{
				type = dash[1];
			}
			return new Version(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(patch), type);
		}
		catch(Exception e)
		{
			System.err.println("Badly formatted version: " + version);
			System.err.println("Due to the recent switch to semantic versioning, outdated plugins using the wrong versioning scheme will eventually cause a crash here.\n" +
					"Please clear out all plugin directories (\n\t" +
					IOUtils.assemblePath(IOUtils.jarDir(), "plugins") +
					"\n\t" +
					IOUtils.assemblePath(System.getProperty("user.home"), ".spade", "plugins") +
					"\n) and update all your plugins.");
			return new Version(0, 0, 0, "INVALID_VERSION_FORMAT");
		}
	}
	
	public String toString()
	{
		String v = major + "." + minor + "." + patch;
		if(type != null)
		{
			v += "-" + type;
		}
		return v;
	}
}
