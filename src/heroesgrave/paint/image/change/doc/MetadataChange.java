package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.DocumentChange;

public class MetadataChange implements DocumentChange
{
	private Layer layer;
	private String key, oldValue, newValue;
	
	public MetadataChange(Layer layer, String key, String value)
	{
		this.layer = layer;
		this.key = key;
		this.newValue = value;
		this.oldValue = layer.getMetadata().get(key);
	}
	
	public void apply(Document doc)
	{
		layer.getMetadata().put(key, newValue);
		layer.updateMetadata();
	}
	
	public void revert(Document doc)
	{
		layer.getMetadata().put(key, oldValue);
		layer.updateMetadata();
	}
	
	public String toString()
	{
		return "MetadataChange(\"" + key + "\": \"" + newValue + "\")";
	}
}
