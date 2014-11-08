package heroesgrave.paint.image.change;

import heroesgrave.paint.io.Serialised;

/**
 * A change that contains no additional data that needs to be serialised.
 * 
 * @author HeroesGrave
 *
 */
public abstract class SerialisedChange implements IChange, Serialised
{
	@Override
	public final IChange decode()
	{
		return this;
	}
	
	@Override
	public final Serialised encode()
	{
		return this;
	}
}
