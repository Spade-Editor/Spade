package heroesgrave.paint.image.change.doc;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.IDocChange;
import heroesgrave.paint.image.change.edit.ResizingChange;

public class DocResize implements IDocChange
{
	private int oldWidth, oldHeight;
	private ResizingChange change;
	
	public DocResize(ResizingChange change)
	{
		this.change = change;
	}
	
	@Override
	public void apply(Document doc)
	{
		oldWidth = doc.getWidth();
		oldHeight = doc.getHeight();
		for(Layer l : doc.getFlatMap())
		{
			l.addChangeSilent(change);
		}
		doc.resize(change.getWidth(), change.getHeight());
	}
	
	@Override
	public void revert(Document doc)
	{
		for(Layer l : doc.getFlatMap())
		{
			l.revertChange();
		}
		doc.resize(oldWidth, oldHeight);
	}
	
	@Override
	public void repeat(Document doc)
	{
		for(Layer l : doc.getFlatMap())
		{
			l.repeatChange();
		}
		doc.resize(change.getWidth(), change.getHeight());
	}
}
