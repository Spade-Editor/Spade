package heroesgrave.paint.effects;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.edit.InvertChange;

public class Invert extends Effect
{
	// InvertChange takes no data and so can be static.
	private static final InvertChange instance = new InvertChange();
	
	public Invert(String name)
	{
		super(name);
	}
	
	public void perform(Layer layer)
	{
		layer.addChange(instance);
	}
}
