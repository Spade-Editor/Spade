package heroesgrave.paint.image.doc;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.main.Paint;

public class LayerNameChange extends DocumentChange
{
	private String newName, oldName;
	private Canvas canvas;
	
	public LayerNameChange(Canvas canvas, String name)
	{
		this.canvas = canvas;
		this.oldName = canvas.name;
		this.newName = name;
	}
	
	public void apply()
	{
		canvas.name = newName;
		Paint.main.gui.layers.redrawTree();
	}
	
	public void revert()
	{
		canvas.name = oldName;
		Paint.main.gui.layers.redrawTree();
	}
}
