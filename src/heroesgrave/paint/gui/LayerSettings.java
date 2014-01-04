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
import heroesgrave.paint.gui.Menu.CentredJLabel;
import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.main.Paint;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LayerSettings
{
	private JDialog dialog;
	private Canvas canvas;
	private JTextField label;
	private JComboBox<BlendMode> blendMode;
	
	public LayerSettings()
	{
		this.dialog = new CentredJDialog(Paint.main.gui.frame, "Layer Settings");
		dialog.setSize(200, 100);
		dialog.setVisible(false);
		dialog.setResizable(false);
		dialog.setLayout(new GridLayout(0, 2));
		
		blendMode = new JComboBox<BlendMode>();
		blendMode.addItem(BlendMode.NORMAL);
		blendMode.addItem(BlendMode.REPLACE);
		
		dialog.add(new CentredJLabel("Layer Name:"));
		dialog.add(label = new JTextField(""));
		dialog.add(new CentredJLabel("Blend Mode:"));
		dialog.add(blendMode);
		
		label.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void showFor(Canvas canvas)
	{
		this.canvas = canvas;
		label.setText(canvas.name);
		dialog.setVisible(true);
		blendMode.setSelectedItem(canvas.mode);
	}
}