package heroesgrave.paint.image.doc;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.IFrame;

public abstract class ImageOpChange implements IFrame
{
	public abstract void apply();
	
	public void setCanvas(Canvas canvas)
	{
		
	}
	
	public Canvas getCanvas()
	{
		return null;
	}
}
