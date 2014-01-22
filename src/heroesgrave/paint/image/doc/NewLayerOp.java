package heroesgrave.paint.image.doc;

import heroesgrave.paint.gui.LayerManager.LayerNode;
import heroesgrave.paint.main.Paint;

public class NewLayerOp extends DocumentChange
{
	private LayerNode canvas, parent;
	
	public NewLayerOp(LayerNode parent)
	{
		canvas = parent.createNoAdd();
		this.parent = parent;
	}
	
	public void apply()
	{
		parent.restore(canvas);
		Paint.main.gui.layers.redrawTree();
	}
	
	public void revert()
	{
		canvas.deleteNoChange();
		Paint.main.gui.layers.redrawTree();
	}
}
