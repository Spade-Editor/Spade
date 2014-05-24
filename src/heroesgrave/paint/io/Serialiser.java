package heroesgrave.paint.io;

import java.io.DataInputStream;
import java.io.IOException;

public class Serialiser
{
	public void read(DataInputStream in)
	{
		try
		{
			int ID = 0;
			while((ID = in.read()) != -1)
			{
				// TODO
			}
		}
		catch(IOException e)
		{
			
		}
	}
}
