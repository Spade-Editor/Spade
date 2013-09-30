package heroesgrave.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextLoader
{
	public BufferedReader in;

	public void load(String directory, String name)
	{

	}

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

	public void setStream(BufferedReader in)
	{
		if(this.in != null)
			close();
		this.in = in;
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
			if(line == null)
				return line;
			if(line.startsWith("#"))
				return line.substring(1);
			return next();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
