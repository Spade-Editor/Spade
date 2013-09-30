package heroesgrave.paint.tools;

public abstract class Tool
{
	public final String name;
	
	public Tool(String name)
	{
		this.name = name;
	}
	
	public abstract void onPressed(int x, int y);
	public abstract void onReleased(int x, int y);
	public abstract void whilePressed(int x, int y);
	public abstract void whileReleased(int x, int y);
}
