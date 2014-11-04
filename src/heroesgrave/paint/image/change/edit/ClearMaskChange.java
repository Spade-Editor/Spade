package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IMaskChange;
import heroesgrave.paint.io.Serialised;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClearMaskChange implements IMaskChange, Serialised
{
	public ClearMaskChange()
	{
		
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.setMaskEnabled(false);
	}
	
	@Override
	public ClearMaskChange encode()
	{
		return this;
	}
	
	@Override
	public ClearMaskChange decode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
	}
	
	@Override
	public boolean isMarker()
	{
		return false;
	}
}
