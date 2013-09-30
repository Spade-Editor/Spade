package heroesgrave.paint.tools;

import heroesgrave.paint.main.Paint;

public class Picker extends Tool
{
	public Picker(String name)
	{
		super(name);
	}

	public void onPressed(int x, int y)
	{
		
	}

	public void onReleased(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		Paint.main.setColour(Paint.main.gui.canvas.getImage().getRGB(x, y));
	}

	public void whilePressed(int x, int y)
	{
		
	}

	public void whileReleased(int x, int y)
	{
		
	}
}
