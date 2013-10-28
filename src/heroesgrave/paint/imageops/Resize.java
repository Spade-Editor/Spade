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
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.misc.NumberDocumentFilter;

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

public class Resize extends ImageOp
{
	public void operation()
	{
		final JDialog dialog = new CentredJDialog();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		
		dialog.getContentPane().add(panel);
		
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		
		dialog.setTitle("Resize");
		
		final JTextField width = new JTextField("" + Paint.main.gui.canvas.getImage().getWidth());
		final JTextField height = new JTextField("" + Paint.main.gui.canvas.getImage().getHeight());
		((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberDocumentFilter());
		((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberDocumentFilter());
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
				resize(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
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
	
	public void resize(float w, float h)
	{
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);
		int oldW = old.getWidth();
		int oldH = old.getHeight();
		
		float sx = w / oldW;
		float sy = h / oldH;
		
		for(int i = 0; i < w; i++)
		{
			for(int j = 0; j < h; j++)
			{
				newImage.setRGB(i, j, old.getRGB(MathUtils.floor(i / sx), MathUtils.floor(j / sy)));
			}
		}
		
		Paint.addChange(new ImageChange(newImage));
	}
}