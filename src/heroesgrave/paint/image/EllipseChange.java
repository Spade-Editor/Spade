package heroesgrave.paint.image;

import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.event.MouseEvent;

public class EllipseChange extends BufferedChange
{
	private int sx, sy, button;
	
	public EllipseChange(int x, int y, int button)
	{
		this.sx = x;
		this.sy = y;
		this.button = button;
	}
	
	public void change(int x, int y)
	{
		this.changes.clear();
		outline(sx, sy, Math.abs(x - sx), Math.abs(y - sy));
	}
	
	public void outline(int cx, int cy, float rx, float ry)
	{
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
			
			brush(i, MathUtils.floor(cy + j));
			brush(i, MathUtils.ceil(cy - j));
		}
		
		for(int j = (int) (cy - ry); j <= cy + ry; j++)
		{
			float ey = (float) j - cy;
			
			float i = 1f - ((ey * ey) / (ry * ry));
			i = i * rx * rx;
			i = (float) Math.sqrt(i);
			
			brush(MathUtils.floor(cx + i), j);
			brush(MathUtils.ceil(cx - i), j);
		}
	}
	
	public void brush(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getWidth() || y >= Paint.main.gui.canvas.getHeight())
			return;
		if(button == MouseEvent.BUTTON1)
		{
			buffer(new PixelChange(x, y, Paint.main.getLeftColour()));
		}
		else if(button == MouseEvent.BUTTON3)
		{
			buffer(new PixelChange(x, y, Paint.main.getRightColour()));
		}
	}
}
