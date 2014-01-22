package heroesgrave.paint.imageops;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.image.doc.ImageOpChange;
import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ResizeCanvasChange extends ImageOpChange
{
	private int w, h;
	
	public ResizeCanvasChange(int w, int h)
	{
		this.w = w;
		this.h = h;
	}
	
	@Override
	public void apply()
	{
		recurse(Paint.main.gui.canvas.getRoot());
	}
	
	public void recurse(Canvas c)
	{
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = (Graphics2D) newImage.getGraphics();
		g2d.drawImage(c.getImage(), 0, 0, null);
		
		c.addChange(new KeyFrame(newImage));
		
		ArrayList<Canvas> list = c.getChildren();
		for(Canvas cn : list)
			recurse(cn);
	}
}
