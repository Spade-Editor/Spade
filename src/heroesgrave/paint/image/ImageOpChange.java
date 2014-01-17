package heroesgrave.paint.image;

public abstract class ImageOpChange implements IFrame
{
	public abstract void apply();
	
	public abstract void revert();
	
	public abstract void repeat();
	
	public void setCanvas(Canvas canvas)
	{
		
	}
	
	public Canvas getCanvas()
	{
		return null;
	}
}
