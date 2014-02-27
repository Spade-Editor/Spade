package heroesgrave.paint.image.accurate;

import heroesgrave.paint.image.BufferedChange;
import heroesgrave.paint.image.PixelChange;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.event.MouseEvent;

public class LineChange extends BufferedChange
{
	private int sx, sy, button;
	
	public LineChange(int x, int y, int button)
	{
		this.sx = x;
		this.sy = y;
		this.button = button;
		this.refresh = true;
		change(x, y);
	}
	
	public void change(int x, int y)
	{
		this.changes.clear();
		stroke(sx, sy, x, y);
	}
	
	private void stroke(int x1, int y1, int x2, int y2)
	{
		float dx = x2 - x1;
		float dy = y2 - y1;
		
		float grad;
		
		if(Math.abs(dx) > Math.abs(dy))
		{
			grad = dy / dx;
			if(dx > 0)
			{
				for(int x = x1; x <= x2; x++)
				{
					brush(x, MathUtils.floor((grad * (x - x1)) + y1));
				}
			}
			else
			{
				for(int x = x1; x >= x2; x--)
				{
					brush(x, MathUtils.floor((grad * (x - x1)) + y1));
				}
			}
		}
		else
		{
			grad = dx / dy;
			if(dy > 0)
			{
				for(int y = y1; y <= y2; y++)
				{
					brush(MathUtils.floor((grad * (y - y1)) + x1), y);
				}
			}
			else
			{
				for(int y = y1; y >= y2; y--)
				{
					brush(MathUtils.floor((grad * (y - y1)) + x1), y);
				}
			}
		}
	}
	
	private void brush(int x, int y)
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
