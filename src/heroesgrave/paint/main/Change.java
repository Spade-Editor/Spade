package heroesgrave.paint.main;

import java.awt.image.BufferedImage;

public interface Change
{
	public BufferedImage apply(BufferedImage image);
	public BufferedImage revert(BufferedImage image);
	public int getSize();
	public boolean samePos(int x, int y);
}
