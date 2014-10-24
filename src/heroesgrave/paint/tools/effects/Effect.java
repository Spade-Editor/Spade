package heroesgrave.paint.tools.effects;

import heroesgrave.paint.image.Layer;

public abstract class Effect
{
	public final String name;
	
	public Effect(String name)
	{
		this.name = name;
	}
	
	public abstract void perform(Layer layer);
}
