package heroesgrave.spade.image;

import heroesgrave.spade.image.RawImage.MaskMode;

public interface IImage 
{
	public void move(int dx, int dy);
	public void drawLine(int x1, int y1, final int x2, final int y2, final int c);
	public void drawRect(int x1, int y1, int x2, int y2, final int c);
	public void fillRect(int x1, int y1, int x2, int y2, int c);
	public void drawPixel(int x, int y, int c);
	public void drawPixelChecked(int x, int y, int c);
	public void fill(int c);
	public void setMask(boolean[] mask);
	public void clear(int c);
	public void setPixel(int x, int y, int c);
	public int getPixel(int x, int y);
	public void fillMask(MaskMode mode);
	public void maskRect(int x1, int y1, int x2, int y2, MaskMode mode);
	public void setMaskEnabled(boolean enabled);
	public void toggleMask();
}
