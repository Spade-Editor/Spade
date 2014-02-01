/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.paint.effects;

import heroesgrave.paint.gui.Menu.CentredJLabel;
import heroesgrave.paint.gui.Menu.NumberTextField;
import heroesgrave.paint.gui.SimpleImageOpDialog;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MakeGrid extends ImageOp
{
	public void operation()
	{
		dialog();
	}
	
	private void dialog()
	{
		// create dialog
		final SimpleImageOpDialog dialog = new SimpleImageOpDialog("Grid-Maker", new GridLayout(0, 2));
		
		// create components
		final NumberTextField WIDTH = new NumberTextField("16");
		final NumberTextField HEIGHT = new NumberTextField("16");
		
		JButton create = new JButton("Draw Grid");
		JButton cancel = new JButton("Cancel");
		
		DocumentListener PREVIEW_ACTION = new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				operation_do(WIDTH.get(), HEIGHT.get());
			}
			
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				operation_do(WIDTH.get(), HEIGHT.get());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				operation_do(WIDTH.get(), HEIGHT.get());
			}
		};
		
		WIDTH.getDocument().addDocumentListener(PREVIEW_ACTION);
		HEIGHT.getDocument().addDocumentListener(PREVIEW_ACTION);
		
		// create actions
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				operation_do(WIDTH.get(), HEIGHT.get());
				Paint.main.gui.canvas.applyPreview();
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.canvas.preview(null);
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
		
		// Checks to prevent division by zero.
		if(W == 0)
		{
			W = 1;
		}
		if(H == 0)
		{
			H = 1;
		}
		
		BufferedImage old = Paint.main.gui.canvas.getCanvas().getImage();
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
		
		Paint.main.gui.canvas.preview(new KeyFrame(newImage));
	}
	
}