package heroesgrave.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextReader
{
	private BufferedReader in;

	public void newStream(String path)
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		in = null;
	}

	public String next()
	{
		try
		{
			String line = in.readLine();
			if(line != null)
				return line.trim();
			return null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
