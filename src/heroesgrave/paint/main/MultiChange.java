package heroesgrave.paint.main;

import java.awt.image.BufferedImage;

public class MultiChange implements Change
{
	public Change[] changes;
	
	public MultiChange(Change... c)
	{
		this.changes = c;
	}
	
	public BufferedImage apply(BufferedImage image)
	{
		for(int i = 0; i < changes.length; i++)
			changes[i].apply(image);
		return image;
	}

	public BufferedImage revert(BufferedImage image)
	{
		for(int i = changes.length-1; i >= 0; i--)
			changes[i].revert(image);
		return image;
	}
	
	public int getSize()
	{
		int size = 0;
		for(Change c : changes)
			size += c.getSize();
		return size;
	}

	public boolean samePos(int x, int y)
	{
		for(Change c : changes)
		{
			if(c.samePos(x, y))
				return true;
		}
		return false;
	}
}
