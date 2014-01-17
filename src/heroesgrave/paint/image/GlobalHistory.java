package heroesgrave.paint.image;

import heroesgrave.paint.main.Paint;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Simple wrapper to combine layer-specific histories into one global history.
 * 
 * Probably not that efficient.
 * 
 * @author HeroesGrave
 *
 */
public class GlobalHistory
{
	private Stack<IFrame> history = new Stack<IFrame>();
	private Stack<IFrame> reverted = new Stack<IFrame>();
	
	public void addChange(IFrame frame)
	{
		history.push(frame);
		if(frame instanceof ImageOpChange)
		{
			((ImageOpChange) frame).apply();
		}
	}
	
	public void revertChange()
	{
		if(history.isEmpty())
			return;
		IFrame frame = history.pop();
		if(frame instanceof ImageOpChange)
			((ImageOpChange) frame).revert();
		else
			frame.getCanvas().getHistory().revertChange();
		Paint.main.gui.canvas.getPanel().repaint();
		reverted.push(frame);
	}
	
	public void repeatChange()
	{
		if(reverted.isEmpty())
			return;
		IFrame frame = reverted.pop();
		if(frame instanceof ImageOpChange)
			((ImageOpChange) frame).repeat();
		else
			frame.getCanvas().getHistory().repeatChange();
		Paint.main.gui.canvas.getPanel().repaint();
		history.push(frame);
	}
	
	public void clearHistory()
	{
		clhRecursive(Paint.main.gui.canvas.getRoot());
	}
	
	private void clhRecursive(Canvas canvas)
	{
		canvas.clearHistory();
		ArrayList<Canvas> list = canvas.getChildren();
		for(Canvas c : list)
			clhRecursive(c);
	}
}
