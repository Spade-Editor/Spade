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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SimpleImageOpDialog
{
	final JDialog dialog;
	final JPanel panel;
	
	public SimpleImageOpDialog(String title, LayoutManager layout)
	{
		dialog = new JDialog(Paint.main.gui.frame, title);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		dialog.getContentPane().add(panel);
		
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
	
	public void setSize(int w, int h)
	{
		dialog.setSize(w, h);
	}
	
}