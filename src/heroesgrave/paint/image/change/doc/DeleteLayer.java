package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.DocumentChange;

public class DeleteLayer implements DocumentChange
{
	private Layer layer, parent;
	private int index = -1;
	
	public DeleteLayer(Layer layer)
	{
		this.layer = layer;
	}
	
	public void apply(Document doc)
	{
		parent = layer.getParentLayer();
		index = parent.removeLayer(layer);
		doc.setCurrent(parent);
		doc.reconstructFlatmap();
	}
	
	public void revert(Document doc)
	{
		parent.addLayer(layer, index);
		doc.setCurrent(layer);
		doc.reconstructFlatmap();
	}
}
