package heroesgrave.paint.image.change;

import java.awt.image.BufferedImage;

public abstract class IEditChange implements IChange
{
	public int layerID;
	
	public abstract void apply(BufferedImage image);
	
	public void setLayer(int ID)
	{
		this.layerID = ID;
	}
}
