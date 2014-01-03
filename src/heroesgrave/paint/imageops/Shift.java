/*
 *	Copyright 2013 HeroesGrave
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

package heroesgrave.paint.imageops;

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.NumberFilter.SignedNumberFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;

public class Shift extends ImageOp
{
	public void operation()
	{
		final JDialog dialog = new CentredJDialog(Paint.main.gui.frame, "Shift Canvas");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		
		dialog.getContentPane().add(panel);
		
		final JTextField width = new JTextField("0");
		final JTextField height = new JTextField("0");
		
		((AbstractDocument) width.getDocument()).setDocumentFilter(new SignedNumberFilter());
		((AbstractDocument) height.getDocument()).setDocumentFilter(new SignedNumberFilter());
		
		width.setColumns(8);
		height.setColumns(8);
		
		JLabel wl = new JLabel("XShift: ");
		wl.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel hl = new JLabel("YShift: ");
		hl.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton create = new JButton("Shift");
		JButton cancel = new JButton("Cancel");
		
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				shift(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
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
	}
	
	public void shift(int x, int y)
	{
		BufferedImage image = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		if(x < 0)
			x += image.getWidth();
		if(y < 0)
			y += image.getHeight();
		
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				int i1 = (i + x) % image.getWidth();
				int j1 = (j + y) % image.getHeight();
				newImage.setRGB(i1, j1, image.getRGB(i, j));
			}
		}
		
		Paint.addChange(new KeyFrame(newImage));
	}
}