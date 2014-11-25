// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextArea;

public class Popup
{
	public static void show(String title, String msg)
	{
		WebDialog dialog = new WebDialog(Paint.main.gui.frame, title);
		
		WebTextArea text = new WebTextArea();
		text.setEditable(false);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		
		text.append(msg);
		
		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new BorderLayout());
		
		panel.setPreferredSize(new Dimension(400, 200));
		
		panel.add(text, BorderLayout.CENTER);
		
		dialog.pack();
		dialog.setResizable(true);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(Paint.main.gui.frame);
	}
}
