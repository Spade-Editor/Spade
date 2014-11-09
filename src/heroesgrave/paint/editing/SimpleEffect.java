package heroesgrave.paint.editing;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.SingleChange;

public class SimpleEffect extends Effect
{
	private SingleChange change;
	
	public SimpleEffect(String name, SingleChange change)
	{
		super(name);
		this.change = change;
	}
	
	@Override
	public void perform(Layer layer)
	{
		layer.addChange(change);
	}
}
