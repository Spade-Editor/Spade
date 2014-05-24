package heroesgrave.paint.image;

import heroesgrave.paint.gui.Renderer;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.image.change.IRevEditChange;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class FreezeBuffer
{
	private History source;
	private BufferedImage back, front;
	private LinkedList<IEditChange> changes = new LinkedList<IEditChange>();
	private Layer layer;
	private Document doc;
	private boolean rebuffer;
	
	public FreezeBuffer(Layer layer, BufferedImage image)
	{
		this.layer = layer;
		this.doc = layer.getDocument();
		this.back = image;
		this.front =
				new BufferedImage(image.getWidth(), image.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
		this.rebuffer = true;
	}
	
	public void addChange(IEditChange change)
	{
		changes.add(change);
		change.apply(front);
	}
	
	public void revertChange()
	{
		IEditChange change = changes.pollLast();
		if(change instanceof IRevEditChange)
			((IRevEditChange) change).revert(front);
		else
			rebuffer = true;
	}
	
	public void rebuffer()
	{
		{
			Graphics2D g = front.createGraphics();
			g.setBackground(Renderer.TRANSPARENT);
			g.clearRect(0, 0, front.getWidth(), front.getHeight());
			g.drawImage(back, 0, 0, null);
			g.dispose();
		}
		for(IEditChange c : changes)
		{
			c.apply(front);
		}
		rebuffer = false;
	}
	
	public BufferedImage getFront()
	{
		if(rebuffer)
			rebuffer();
		return front;
	}
}
