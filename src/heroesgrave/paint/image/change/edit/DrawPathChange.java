package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.SingleChange;
import heroesgrave.paint.image.change.edit.PathChange.IPathChange;

public class DrawPathChange extends IPathChange
{
	public static DrawPathChange instance = new DrawPathChange();
	
	public DrawPathChange()
	{
		
	}
	
	@Override
	public void point(RawImage image, int x, int y, int c)
	{
		image.drawPixelChecked(x, y, c);
	}
	
	@Override
	public void line(RawImage image, int x1, int y1, int x2, int y2, int c)
	{
		image.drawLine(x1, y1, x2, y2, c);
	}
	
	@Override
	public SingleChange getInstance()
	{
		return instance;
	}
}
