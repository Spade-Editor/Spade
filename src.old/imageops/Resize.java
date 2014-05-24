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

package heroesgrave.paint.imageops;

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.NumberFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
		final JDialog dialog = new CentredJDialog(Paint.main.gui.frame, "Resize Image");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new GridLayout(0, 2));
		
		final JTextField width = new JTextField("" + Paint.main.gui.canvas.getWidth());
		final JTextField height = new JTextField("" + Paint.main.gui.canvas.getHeight());
		final JComboBox<String> filter = new JComboBox<String>();
		final DefaultComboBoxModel<String> filterModel = new DefaultComboBoxModel<String>(new String[]{"Nearest Neighbor", "Bilinear", "Bicubic"});
		
		((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberFilter());
		((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberFilter());
		filter.setModel(filterModel);
		
		width.setColumns(8);
		height.setColumns(8);
		
		JLabel wl = new JLabel("Width: ");
		wl.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel hl = new JLabel("Height: ");
		hl.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel fl = new JLabel("Filter: ");
		fl.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton create = new JButton("Resize");
		JButton cancel = new JButton("Cancel");
		
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
				resize(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), filterModel.getSelectedItem().toString());
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
		panel.add(fl);
		panel.add(filter);
		panel.add(create);
		panel.add(cancel);
		
		dialog.pack();
		
		dialog.setResizable(false);
		dialog.setVisible(true);
	}
	
	public void resize(int nx, int ny, String filter)
	{
		Paint.main.history.addChange(new ResizeChange(nx, ny, filter));
	}
}