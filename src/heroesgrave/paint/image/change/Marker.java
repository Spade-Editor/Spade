package heroesgrave.paint.image.change;

import heroesgrave.paint.io.Serialised;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Marker implements Serialised
{
	public IChange decode()
	{
		throw new IllegalStateException("Cannot decode a Marker");
	}
	
	public void write(DataOutputStream out) throws IOException
	{
		
	}
	
	public void read(DataInputStream in) throws IOException
	{
		
	}
	
	public final boolean isMarker()
	{
		return true;
	}
}
