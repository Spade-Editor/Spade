package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.DocumentChange;

public class MoveLayer implements DocumentChange
{
	private Layer layer, oldParent, newParent;
	private int oldIndex = -1, newIndex = -1;
	
	public MoveLayer(Layer layer, Layer target, int targetIndex)
	{
		this.layer = layer;
		this.oldParent = layer.getParentLayer();
		this.newParent = target;
		newIndex = targetIndex;
	}
	
	public void apply(Document doc)
	{
		oldIndex = oldParent.getIndex(layer);
		if(newIndex != -1)
			newParent.addLayer(layer, newIndex);
		else
			newParent.addLayer(layer);
		doc.setCurrent(layer);
		doc.reconstructFlatmap();
	}
	
	public void revert(Document doc)
	{
		oldParent.addLayer(layer, oldIndex);
		doc.setCurrent(layer);
		doc.reconstructFlatmap();
	}
}
