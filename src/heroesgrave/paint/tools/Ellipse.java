package heroesgrave.paint.tools;

import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;
import heroesgrave.utils.math.MathUtils;

public class Ellipse extends Tool
{
	private int sx, sy;

	public Ellipse(String name)
	{
		super(name);
	}

	public void onPressed(int x, int y)
	{
		sx = x;
		sy = y;
	}

	public void onReleased(int x, int y)
	{
		circle(sx, sy, Math.abs(x - sx), Math.abs(y - sy));
		Paint.main.gui.canvas.applyPreview();
	}

	public void whilePressed(int x, int y)
	{
		circle(sx, sy, Math.abs(x - sx), Math.abs(y - sy));
	}

	public void whileReleased(int x, int y)
	{

	}

	public void circle(int cx, int cy, float rx, float ry)
	{
		Paint.main.gui.canvas.clearPreview();
		
		if(Input.CTRL)
		{
			rx = ry = Math.max(rx, ry);
		}
		
		for(int i = (int) (cx - rx); i <= cx + rx; i++)
		{
			float ex = (float) i - cx;

			float j = 1f - ((ex * ex) / (rx * rx));
			j = j * ry * ry;
			j = (float) Math.sqrt(j);
			
			brush(i, MathUtils.floor(cy+j));
			brush(i, MathUtils.ceil(cy-j));
		}
		
		for(int j = (int) (cy - ry); j <= cy + ry; j++)
		{
			float ey = (float) j - cy;

			float i = 1f - ((ey * ey) / (ry * ry));
			i = i * rx * rx;
			i = (float) Math.sqrt(i);
			
			brush(MathUtils.floor(cx+i), j);
			brush(MathUtils.ceil(cx-i), j);
		}
	}

	public void brush(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		Paint.main.gui.canvas.preview(new PixelChange(x, y, Paint.main.getColour()));
	}
}
