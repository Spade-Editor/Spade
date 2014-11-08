package heroesgrave.utils.misc;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.alee.laf.rootpane.WebDialog;

public class DialogWrapper
{
	public JDialog dialog;
	
	public DialogWrapper(JFrame parent, String title)
	{
		this.dialog = new WebDialog(parent, title);
	}
	
	public void centre(JFrame frame)
	{
		((WebDialog) dialog).center(frame);
	}
}
