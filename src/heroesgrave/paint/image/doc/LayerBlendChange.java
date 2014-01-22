package heroesgrave.paint.image.doc;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.main.Paint;

public class LayerBlendChange extends DocumentChange
{
	private BlendMode oldMode, newMode;
	private Canvas canvas;
	
	public LayerBlendChange(Canvas canvas, BlendMode mode)
	{
		this.canvas = canvas;
		this.oldMode = canvas.mode;
		this.newMode = mode;
	}
	
	public void apply()
	{
		canvas.setBlendMode(newMode);
		Paint.main.gui.layers.redrawTree();
	}
	
	public void revert()
	{
		canvas.setBlendMode(oldMode);
		Paint.main.gui.layers.redrawTree();
	}
}
