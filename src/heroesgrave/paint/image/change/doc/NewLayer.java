package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.DocumentChange;
import heroesgrave.utils.misc.Metadata;

public class NewLayer implements DocumentChange
{
	private Layer layer, parent;
	private int index = -1;
	
	public NewLayer(Layer parent)
	{
		this.parent = parent;
	}
	
	public void apply(Document doc)
	{
		if(layer == null)
			layer = new Layer(doc, new Metadata());
		if(index != -1)
			parent.addLayer(layer, index);
		else
			parent.addLayer(layer);
		doc.setCurrent(layer);
		doc.reconstructFlatmap();
	}
	
	public void revert(Document doc)
	{
		index = parent.removeLayer(layer);
		doc.setCurrent(parent);
		doc.reconstructFlatmap();
	}
}
