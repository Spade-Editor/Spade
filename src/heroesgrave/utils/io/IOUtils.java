package heroesgrave.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class IOUtils
{
	private static Class<?> MAIN_CLASS;
	
	private IOUtils()
	{
		
	}
	
	public static void setMainClass(Class<?> c)
	{
		MAIN_CLASS = c;
	}
	
	public static String jarDir()
	{
		try
		{
			return new File(MAIN_CLASS.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		}
		catch(URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String assemblePath(String... path)
	{
		String fullPath = "";
		for(String p : path)
		{
			fullPath += File.separator + p;
		}
		return fullPath;
	}

	public static String fileToString(String path)
	{
		StringBuffer sBuffer = new StringBuffer();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			char[] buffer = new char[1024];

			int c;

			while ((c = reader.read(buffer, 0, buffer.length)) != -1)
			{
				sBuffer.append(buffer, 0, c);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return sBuffer.toString();
	}

	public static String fileToStringOneLine(String path)
	{
		String string = new String();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			
			String line;
			while((line = reader.readLine()) != null)
			{
				string += line;
			}
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return string;
	}
}
