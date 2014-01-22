package heroesgrave.paint.image.doc;

import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.IFrame;
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
		System.out.println("Change Added: " + frame);
		history.push(frame);
		if(frame instanceof ImageOpChange)
		{
			((ImageOpChange) frame).apply();
			Paint.main.gui.canvas.setDimensions();
		}
		else if(frame instanceof DocumentChange)
		{
			((DocumentChange) frame).apply();
		}
		reverted.clear();
	}
	
	public void revertChange()
	{
		if(history.isEmpty())
			return;
		IFrame frame = history.pop();
		if(frame instanceof ImageOpChange)
		{
			revertRec(Paint.main.gui.canvas.getRoot());
			Paint.main.gui.canvas.setDimensions();
		}
		else if(frame instanceof DocumentChange)
		{
			((DocumentChange) frame).revert();
		}
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
		{
			repeatRec(Paint.main.gui.canvas.getRoot());
			Paint.main.gui.canvas.setDimensions();
		}
		else if(frame instanceof DocumentChange)
		{
			((DocumentChange) frame).apply();
		}
		else
			frame.getCanvas().getHistory().repeatChange();
		Paint.main.gui.canvas.getPanel().repaint();
		history.push(frame);
	}
	
	public void clearHistory()
	{
		clearHistRec(Paint.main.gui.canvas.getRoot());
	}
	
	private void clearHistRec(Canvas canvas)
	{
		canvas.clearHistory();
		ArrayList<Canvas> list = canvas.getChildren();
		for(Canvas c : list)
			clearHistRec(c);
	}
	
	private void revertRec(Canvas canvas)
	{
		canvas.getHistory().revertChange();
		ArrayList<Canvas> list = canvas.getChildren();
		for(Canvas c : list)
			revertRec(c);
	}
	
	private void repeatRec(Canvas canvas)
	{
		canvas.getHistory().repeatChange();
		ArrayList<Canvas> list = canvas.getChildren();
		for(Canvas c : list)
			repeatRec(c);
	}
}
