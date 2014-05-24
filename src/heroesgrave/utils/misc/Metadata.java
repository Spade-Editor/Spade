package heroesgrave.utils.misc;

import java.util.HashMap;

public class Metadata
{
	private HashMap<String, String> metadata = new HashMap<String, String>();
	
	public String put(String key, String value)
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
}
