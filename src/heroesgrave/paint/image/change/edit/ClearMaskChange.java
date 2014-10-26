package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.io.Serialised;
import heroesgrave.paint.main.Paint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClearMaskChange implements IEditChange, Serialised
{
	public ClearMaskChange()
	{
		
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.setMaskEnabled(false);
		Paint.main.gui.canvasPanel.changeSelectedRegion();
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
