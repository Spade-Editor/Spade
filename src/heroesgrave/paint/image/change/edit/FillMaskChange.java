package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.RawImage.MaskMode;
import heroesgrave.paint.image.change.IMaskChange;
import heroesgrave.paint.io.Serialised;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FillMaskChange implements IMaskChange, Serialised
{
	private MaskMode mode;
	
	public FillMaskChange(MaskMode mode)
	{
		this.mode = mode;
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.setMaskEnabled(true);
		image.fillMask(mode);
	}
	
	@Override
	public FillMaskChange encode()
	{
		return this;
	}
	
	@Override
	public FillMaskChange decode()
	{
		return this;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException
	{
		out.writeInt(mode.ordinal());
	}
	
	@Override
	public void read(DataInputStream in) throws IOException
	{
		int mode = in.readInt();
		for(MaskMode m : MaskMode.values())
		{
			if(m.ordinal() == mode)
			{
				this.mode = m;
				break;
			}
		}
	}
	
	@Override
	public boolean isMarker()
	{
		return false;
	}
}
