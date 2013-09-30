package heroesgrave.utils.app;

import java.util.HashMap;

public class DataHandler
{
	private static HashMap<String, Data> data = new HashMap<String, Data>();
	
	private DataHandler()
	{
		
	}
	
	public static void addData(String name, Data data)
	{
		DataHandler.data.put(name, data);
	}
	
	public static Data getData(String name)
	{
		return data.get(name);
	}
	
	public static void removeData(String name)
	{
		data.remove(name);
	}
	
	public static interface Data
	{
		
	}
}
