package heroesgrave.paint.main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Error
{
	public static void show(String title, String msg)
	{
		JDialog dialog = new JDialog();
		
		dialog.setAutoRequestFocus(true);
		dialog.setAlwaysOnTop(true);
		
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		
		text.append(msg);
		
		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new BorderLayout());
		
		panel.setPreferredSize(new Dimension(400, 200));
		
		dialog.setTitle(title);
		panel.add(text, BorderLayout.CENTER);
		
		dialog.pack();
		dialog.setResizable(true);
		dialog.setVisible(true);
	}
}
