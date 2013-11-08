package heroesgrave.paint.effects;

import heroesgrave.paint.gui.SimpleImageOpDialog;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.gui.Menu.CentredJLabel;
import heroesgrave.paint.gui.Menu.NumberTextField;
import heroesgrave.paint.imageops.ImageChange;
import heroesgrave.paint.imageops.ImageOp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class MakeGrid extends ImageOp {

	@Override
	public void operation()
	{
		dialog();
	}

	private void dialog()
	{
		// create dialog
		final SimpleImageOpDialog dialog = new SimpleImageOpDialog("Grid-Maker", new GridLayout(0,2));
		
		// create components
		final NumberTextField WIDTH = new NumberTextField("16");
		final NumberTextField HEIGHT= new NumberTextField("16");
		
		JButton create = new JButton("Draw Grid");
		JButton cancel = new JButton("Cancel");
		
		// create actions
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				operation_do(WIDTH.get(), HEIGHT.get());
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
			}
		});
		
		// add components
		dialog.add(new CentredJLabel("Width"));
		dialog.add(WIDTH);
		dialog.add(new CentredJLabel("Height"));
		dialog.add(HEIGHT);
		dialog.add(create);
		dialog.add(cancel);
		
		// show
		dialog.show();
	}
	
	public void operation_do(int W, int H)
	{
		// If ANY of the size arguments are -1, return.
		if(W == -1 || H == -1)
			return;
		
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				int before = old.getRGB(i, j);
				int after = before;
				
				if(i % W == 0 || j % H == 0)
				{
					after = Paint.main.getLeftColour();
				}
				
				newImage.setRGB(i, j, after);
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}

}
