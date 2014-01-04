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

package heroesgrave.paint.gui;

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class LayerSettings
{
	private JDialog dialog;
	private Canvas canvas;
	private JLabel label;
	
	public LayerSettings()
	{
		this.dialog = new CentredJDialog(Paint.main.gui.frame, "Layer Settings");
		dialog.setSize(300, 200);
		dialog.setVisible(false);
		dialog.setResizable(false);
		dialog.setLayout(new BorderLayout());
		dialog.add(label = new JLabel(), BorderLayout.NORTH);
	}
	
	public void showFor(Canvas canvas)
	{
		this.canvas = canvas;
		label.setText(canvas.name);
		label.setHorizontalAlignment(JLabel.CENTER);
		dialog.setVisible(true);
	}
}