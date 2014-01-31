package heroesgrave.paint.image.doc;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.main.Paint;

public class DeleteSelectionOp extends DocumentChange
{
	private Canvas canvas, parent;
	
	public DeleteSelectionOp(Canvas c, Canvas p)
	{
		this.canvas = c;
		this.parent = p;
	}
	
	public void apply()
	{
		parent.removeLayer(canvas);
		Paint.main.gui.canvas.selection.setFloating(false);
		Paint.main.gui.canvas.select(parent);
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public void revert()
	{
		parent.addLayer(canvas);
		Paint.main.gui.canvas.selection.setFloating(true);
		Paint.main.gui.canvas.select(canvas);
		Paint.main.gui.canvas.getPanel().repaint();
	}
}
