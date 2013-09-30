package heroesgrave.paint.tools;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;

public class Pixel extends Brush
{
	public Pixel(String name)
	{
		super(name);
	}

	public void brush(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		buffer(new PixelChange(x, y, Paint.main.getColour()));
	}
}
