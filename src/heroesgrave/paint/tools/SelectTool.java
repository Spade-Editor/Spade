package heroesgrave.paint.tools;

import heroesgrave.paint.image.MultiChange;
import heroesgrave.paint.image.SelectionCanvas.CombineMode;
import heroesgrave.paint.image.ShapeChange;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;

import java.awt.AlphaComposite;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class SelectTool extends Tool
{
	public static enum SelectionType
	{
		RECTANGLE, ELLIPSE
	}
	
	public int sx, sy;
	public Shape shape;
	
	public SelectTool(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		sx = x;
		sy = y;
		this.shape = getShape(SelectionType.RECTANGLE, x, y, 1, 1);
		Paint.main.gui.canvas.selector = new MultiChange(new ShapeChange(shape, 0xff0066ff).setFill(true).setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0x3f / 255f)), new ShapeChange(shape, 0xff001133).setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 0x7f / 255f)));
	}
	
	public void onReleased(int x, int y, int button)
	{
		int minX = Math.min(x, sx);
		int minY = Math.min(y, sy);
		int width = Math.abs(x - sx);
		int height = Math.abs(y - sy);
		Paint.main.gui.canvas.selector = null;
		shape = null;
		if(width < 1 || height < 1)
		{
			return;
		}
		Paint.main.gui.canvas.selection.create(getShape(SelectionType.RECTANGLE, minX, minY, width, height), getMode());
	}
	
	public void whilePressed(int x, int y, int button)
	{
		int minX = Math.min(x, sx);
		int minY = Math.min(y, sy);
		int width = Math.abs(x - sx);
		int height = Math.abs(y - sy);
		Rectangle2D.Float rect = (Rectangle2D.Float) shape;
		rect.x = minX;
		rect.y = minY;
		rect.width = width;
		rect.height = height;
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public static CombineMode getMode()
	{
		if(Input.CTRL)
		{
			if(Input.ALT)
			{
				return CombineMode.XOR;
			}
			else
			{
				return CombineMode.ADD;
			}
		}
		else if(Input.ALT)
		{
			return CombineMode.SUBTRACT;
		}
		else if(Input.SHIFT)
		{
			return CombineMode.INTERSECT;
		}
		else
		{
			return CombineMode.REPLACE;
		}
	}
	
	public Shape getShape(SelectionType type, int x, int y, int w, int h)
	{
		switch(type)
		{
			case RECTANGLE:
				return new Rectangle2D.Float(x, y, w, h);
			case ELLIPSE:
				return new Ellipse2D.Float(x, y, w, h);
			default:
				return null;
		}
	}
}
