package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.change.IImageChange;

public abstract class ResizingChange extends IImageChange
{
	public abstract int getWidth();
	
	public abstract int getHeight();
}
