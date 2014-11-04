package heroesgrave.paint.effects;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.IChange;
import heroesgrave.paint.image.change.edit.GrayscaleChange;

public class Grayscale extends Effect
{

	private static final IChange INSTANCE = new GrayscaleChange();
	
	public Grayscale(String name)
	{
		super(name);
	}

	@Override
	public void perform(Layer layer)
	{
		layer.addChange(INSTANCE);
	}
}
