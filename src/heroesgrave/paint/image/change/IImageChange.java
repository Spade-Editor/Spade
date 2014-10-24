package heroesgrave.paint.image.change;

import heroesgrave.paint.io.Serialised;

import java.awt.image.BufferedImage;

public abstract class IImageChange implements IChange, Serialised
{
	public abstract BufferedImage apply(BufferedImage image);
	
	public final boolean isMarker()
	{
		return true;
	}
	
	public final Serialised encode()
	{
		return this;
	}
	
	public final IChange decode()
	{
		return this;
	}
}
