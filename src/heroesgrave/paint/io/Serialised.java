package heroesgrave.paint.io;

import heroesgrave.paint.image.change.IChange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Serialised
{
	public IChange decode();
	
	public void write(DataOutputStream out) throws IOException;
	
	public void read(DataInputStream in) throws IOException;
}
