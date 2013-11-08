package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SimpleImageOpDialog {
	final JDialog dialog;
	final JPanel panel;
	
	public SimpleImageOpDialog(String title, LayoutManager layout)
	{
		dialog = new JDialog(Paint.main.gui.frame, title);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		panel =  new JPanel();
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		dialog.getContentPane().add(panel);
		
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		
		dialog.setTitle(title);
	}
	
	public JPanel getContentPanel()
	{
		return panel;
	}
	
	public void add(JComponent comp)
	{
		panel.add(comp);
	}
	
	public void show()
	{
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(null);
	}
	
	public void close()
	{
		dialog.dispose();
	}

	public void setSize(int w, int h) {
		dialog.setSize(w, h);
	}
	
}
