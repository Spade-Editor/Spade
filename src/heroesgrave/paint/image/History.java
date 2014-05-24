package heroesgrave.paint.image;

import heroesgrave.paint.image.change.IChange;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.io.Serialised;

import java.util.LinkedList;
import java.util.Stack;

public class History
{
	public static final int MNN = 2;
	public static final int MDN = 2 * MNN;
	public static final int MXN = 2 * MDN;
	
	private LinkedList<IChange> changes = new LinkedList<IChange>();
	private LinkedList<Serialised> oldChanges = new LinkedList<Serialised>();
	
	private Stack<IChange> reverted = new Stack<IChange>();
	
	private Document doc;
	
	public History(Document doc)
	{
		this.doc = doc;
	}
	
	public void addChange(IChange change)
	{
		reverted.clear();
		changes.addLast(change);
		
		if(changes.size() > MXN)
		{
			while(changes.size() >= MDN)
			{
				oldChanges.addLast(changes.removeFirst().encode());
			}
		}
	}
	
	public void revertChange()
	{
		if(changes.isEmpty())
			return;
		IChange change = changes.pollLast();
		if(change instanceof IEditChange)
		{
			doc.getFlatMap().get(((IEditChange) change).layerID).revertChange();
		}
		reverted.push(change);
		if(changes.size() < MNN)
		{
			while(changes.size() < MDN && oldChanges.size() > 0)
			{
				changes.addFirst(oldChanges.removeLast().decode());
			}
		}
	}
	
	public void repeatChange()
	{
		if(reverted.isEmpty())
			return;
		IChange change = reverted.pop();
		if(change instanceof IEditChange)
		{
			doc.getFlatMap().get(((IEditChange) change).layerID)
					.repeatChange((IEditChange) change);
		}
		changes.addLast(change);
	}
}
