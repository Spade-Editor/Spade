package heroesgrave.paint.image.change;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A change that has no data and therefore can be contained in a single instance
 * 
 * @author HeroesGrave
 *
 */
public abstract class SingleChange extends SerialisedChange
{
	public abstract SingleChange getInstance();
	
	@Override
	public final void write(DataOutputStream out) throws IOException
	{
	}
	
	@Override
	public final void read(DataInputStream in) throws IOException
	{
	}
}
