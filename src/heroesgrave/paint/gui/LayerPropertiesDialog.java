package heroesgrave.paint.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import heroesgrave.paint.gui.LayerManager.LayerNode;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.IFunc;

@SuppressWarnings("serial")
public class LayerPropertiesDialog {
	final LayerManager layerManager;
	final JDialog dialog;
	final LayerNode layer;
	
	public LayerPropertiesDialog(LayerManager lm, LayerNode n) {
		layerManager = lm;
		layer = n;
		
		dialog = new JDialog(Paint.main.gui.frame, "Properties");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setLayout(new GridLayout(0, 2));
		dialog.setMinimumSize(new Dimension(128,64));
		dialog.getRootPane().setBorder(new TitledBorder("Layer-Node Properties"));
		
		Container root = dialog.getContentPane();
		root.add(new JLabel("Layer Name "));
		root.add(new SimpleActionTextField(n.canvas.name, new IFunc<String>()
		{
			@Override public void action(String param)
			{
				layer.canvas.name = param;
				layerManager.redrawTree();
				layerManager.dialog.revalidate();
				layerManager.dialog.repaint();
			}
		}));
		
		root.add(new JLabel());
		root.add(new JButton(new AbstractAction("Done")
		{
			@Override public void actionPerformed(ActionEvent e)
			{
				LayerPropertiesDialog.this.close();
			}
		}));
		
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
	
	
	
}
