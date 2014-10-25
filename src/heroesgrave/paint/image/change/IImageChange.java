package heroesgrave.paint.image.change;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.io.Serialised;

public abstract class IImageChange implements IChange, Serialised
{
	public abstract RawImage apply(RawImage image);
	
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
