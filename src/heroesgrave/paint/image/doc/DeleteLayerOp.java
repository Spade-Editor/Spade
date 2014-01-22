package heroesgrave.paint.image.doc;

import heroesgrave.paint.gui.LayerManager.LayerNode;

/**
 * Inverse of {@link NewLayerOp}
 * 
 * @author HeroesGrave
 *
 */
public class DeleteLayerOp extends DocumentChange
{
	private LayerNode canvas, parent;
	
	public DeleteLayerOp(LayerNode canvas)
	{
		this.canvas = canvas;
		this.parent = (LayerNode) canvas.getParent();
	}
	
	public void apply()
	{
		canvas.deleteNoChange();
	}
	
	public void revert()
	{
		parent.restore(canvas);
	}
}
