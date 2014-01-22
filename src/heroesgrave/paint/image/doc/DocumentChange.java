package heroesgrave.paint.image.doc;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.IFrame;
import heroesgrave.paint.image.RevertFrame;

/**
 * Used for changes to the image document.
 * 
 * Eg: Manipulation of Layers.
 * 
 * @author HeroesGrave
 *
 */
public abstract class DocumentChange implements IFrame, RevertFrame
{
	public void setCanvas(Canvas canvas)
	{
		
	}
	
	public Canvas getCanvas()
	{
		return null;
	}
}
