package heroesgrave.paint.image.change;

import java.awt.image.BufferedImage;

/**
 * Changes that are easily revertable should use this interface instead of IChange.
 * 
 * @author HeroesGrave
 *
 */
public abstract class IRevEditChange extends IEditChange
{
	public abstract void revert(BufferedImage image);
}
