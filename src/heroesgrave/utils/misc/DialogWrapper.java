package heroesgrave.utils.misc;

import heroesgrave.paint.main.Paint;

import javax.swing.JDialog;

import com.alee.laf.rootpane.WebDialog;

public class DialogWrapper
{
	public JDialog dialog;
	
	public DialogWrapper(String title)
	{
		this.dialog = new WebDialog(Paint.main.gui.frame, title);
	}
	
	public void centre()
	{
		((WebDialog) dialog).center(Paint.main.gui.frame);
	}
}
