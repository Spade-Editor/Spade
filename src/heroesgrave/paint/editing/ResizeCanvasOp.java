package heroesgrave.paint.editing;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.doc.DocResize;
import heroesgrave.paint.image.change.edit.ResizeCanvasChange;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.NumberFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;

import com.alee.laf.rootpane.WebDialog;

public class ResizeCanvasOp extends Effect
{
	public ResizeCanvasOp(String name)
	{
		super(name);
	}
	
	@Override
	public void perform(final Layer layer)
	{
		final WebDialog dialog = new WebDialog(Paint.main.gui.frame, "Resize Canvas");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		
		dialog.getContentPane().add(panel);
		
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		
		final JTextField width = new JTextField("" + layer.getWidth());
		final JTextField height = new JTextField("" + layer.getHeight());
		
		((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberFilter());
		((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberFilter());
		
		width.setColumns(8);
		height.setColumns(8);
		
		JLabel wl = new JLabel("Width: ");
		wl.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel hl = new JLabel("Height: ");
		hl.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton create = new JButton("Resize");
		JButton cancel = new JButton("Cancel");
		
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				resize(layer, Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
			}
		});
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
			}
		});
		
		panel.add(wl);
		panel.add(width);
		panel.add(hl);
		panel.add(height);
		panel.add(create);
		panel.add(cancel);
		
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);
		dialog.center(Paint.main.gui.frame);
	}
	
	public void resize(Layer layer, int w, int h)
	{
		layer.getDocument().addChange(new DocResize(new ResizeCanvasChange(w, h)));
	}
}
