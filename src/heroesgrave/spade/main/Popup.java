// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JPanel;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;

public class Popup
{
	public static void show(String title, String msg)
	{
		WebDialog dialog = new WebDialog(Spade.main.gui.frame, title);
		
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
		dialog.setLocationRelativeTo(Spade.main.gui.frame);
	}
	
	public static void showException(String title, Exception e, String msg2)
	{
		WebDialog dialog = new WebDialog(Spade.main.gui.frame, title);
		dialog.setModal(true);
		
		WebTextArea text = new WebTextArea();
		text.setEditable(false);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		text.setFontName(Font.MONOSPACED);
		
		StringWriter msg = new StringWriter();
		e.printStackTrace(new PrintWriter(msg));
		text.append(msg.toString());
		
		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new BorderLayout());
		
		panel.setPreferredSize(new Dimension(800, 400));
		
		WebScrollPane scroll = new WebScrollPane(text);
		
		panel.add(new WebLabel("An Exception Occured: ").setMargin(5), BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);
		
		WebPanel bottom = new WebPanel();
		bottom.setLayout(new BorderLayout());
		
		WebTextArea report =
				new WebTextArea("Please copy the above message and report it to " + Spade.REPO_URL
						+ " along with details of what you were doing when it happened.");
		report.setLineWrap(true);
		report.setWrapStyleWord(true);
		report.setEditable(false);
		report.setMargin(5);
		
		WebTextArea endMsg = new WebTextArea(msg2);
		endMsg.setLineWrap(true);
		endMsg.setWrapStyleWord(true);
		endMsg.setEditable(false);
		endMsg.setMargin(5);
		
		bottom.add(report, BorderLayout.NORTH);
		bottom.add(endMsg, BorderLayout.CENTER);
		
		panel.add(bottom, BorderLayout.SOUTH);
		
		dialog.pack();
		dialog.setResizable(true);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(Spade.main.gui.frame);
	}
}
