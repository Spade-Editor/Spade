package heroesgrave.paint.tools;

import java.awt.Point;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.edit.FloodPathChange;
import heroesgrave.paint.main.Paint;

public class FloodFill extends Tool
{

	private FloodPathChange path;
	
	public FloodFill(String name)
	{
		super(name);
	}

	@Override
	public void onPressed(Layer layer, short x, short y, int button)
	{
		path = new FloodPathChange(new Point(x, y), Paint.main.getColor(button));
		Paint.getDocument().preview(path);
	}

	@Override
	public void onReleased(Layer layer, short x, short y, int button)
	{
		Paint.getDocument().applyPreview();
		path = null;
	}

	@Override
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(path.moveTo(x, y))
			Paint.getDocument().repaint();
	}
}
