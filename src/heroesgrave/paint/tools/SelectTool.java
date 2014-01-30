package heroesgrave.paint.tools;

import heroesgrave.paint.image.MultiChange;
import heroesgrave.paint.image.ShapeChange;
import heroesgrave.paint.main.Paint;

import java.awt.geom.Rectangle2D;

public class SelectTool extends Tool
{
	public int sx, sy;
	
	public SelectTool(String name)
	{
		super(name);
	}
	
	public void onPressed(int x, int y, int button)
	{
		sx = x;
		sy = y;
	}
	
	public void onReleased(int x, int y, int button)
	{
		int minX = Math.min(x, sx);
		int minY = Math.min(y, sy);
		int width = Math.abs(x - sx);
		int height = Math.abs(y - sy);
		Paint.main.gui.canvas.preview(null);
		if(width < 1 || height < 1)
		{
			return;
		}
		Paint.main.gui.canvas.selection.create(minX, minY, width, height);
	}
	
	public void whilePressed(int x, int y, int button)
	{
		int minX = Math.min(x, sx);
		int minY = Math.min(y, sy);
		int width = Math.abs(x - sx);
		int height = Math.abs(y - sy);
		Paint.main.gui.canvas.preview(new MultiChange( //
				new ShapeChange(new Rectangle2D.Float(minX, minY, width, height), 0x3f0066ff).setFill(true), //
				new ShapeChange(new Rectangle2D.Float(minX, minY, width, height), 0x7f001133)));
	}
}
