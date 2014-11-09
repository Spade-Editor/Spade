package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.image.change.IDocChange;
import heroesgrave.paint.image.change.edit.SetImageChange;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MergeLayer implements IDocChange
{
	private Layer layer, parent;
	private int index = -1;
	
	public MergeLayer(Layer layer)
	{
		this.layer = layer;
	}
	
	@Override
	public void apply(Document doc)
	{
		this.parent = layer.getParentLayer();
		this.index = parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(parent);
		
		BufferedImage image = parent.getBufferedImage();
		BlendMode mode = layer.getBlendMode();
		{
			Graphics2D g = image.createGraphics();
			g.setComposite(mode);
			g.drawImage(layer.getBufferedImage(), 0, 0, null);
			g.dispose();
		}
		int[] buffer = RawImage.fromBufferedImage(image).borrowBuffer();
		parent.addChangeSilent(new SetImageChange(buffer));
	}
	
	@Override
	public void revert(Document doc)
	{
		parent.revertChange();
		parent.addLayer(layer, index);
		doc.reconstructFlatmap();
		doc.setCurrent(layer);
	}
	
	@Override
	public void repeat(Document doc)
	{
		parent.removeLayer(layer);
		doc.reconstructFlatmap();
		doc.setCurrent(layer);
	}
}
