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

package heroesgrave.utils.misc;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Metadata
{
	private HashMap<String, String> metadata = new HashMap<String, String>();
	
	public String set(String key, String value)
	{
		return metadata.put(key, value);
	}
	
	public String remove(String key)
	{
		return metadata.remove(key);
	}
	
	public String get(String key)
	{
		return metadata.get(key);
	}
	
	public String getOr(String key, String def)
	{
		String ret = metadata.get(key);
		return ret != null ? ret : def;
	}
	
	public String getOrSet(String key, String def)
	{
		String ret = metadata.get(key);
		if(ret == null)
		{
			this.set(key, def);
			return def;
		}
		return ret;
	}
	
	public boolean has(String key)
	{
		return metadata.containsKey(key);
	}
	
	public int size()
	{
		return metadata.size();
	}
	
	public Set<Entry<String, String>> getEntries()
	{
		return metadata.entrySet();
	}
}
