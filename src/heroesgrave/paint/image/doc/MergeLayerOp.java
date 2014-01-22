package heroesgrave.paint.image.doc;

import heroesgrave.paint.gui.LayerManager.LayerNode;

public class MergeLayerOp extends DocumentChange
{
	private LayerNode src, dest;
	
	public MergeLayerOp(LayerNode src, LayerNode dest)
	{
		this.src = src;
		this.dest = dest;
	}
	
	public void apply()
	{
		dest.mergeNoChange(src);
	}
	
	public void revert()
	{
		dest.revertMerge(src);
	}
}
