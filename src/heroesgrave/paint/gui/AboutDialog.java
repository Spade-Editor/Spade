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

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class AboutDialog extends CentredJDialog
{
	private static final long serialVersionUID = -1020702247965837857L;
	
	public AboutDialog(JFrame frame)
	{
		super(frame, "About Paint.JAVA");
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		JTextPane area = new JTextPane();
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("Paint.JAVA\n");
		buf.append("\nVersion: " + Paint.VERSION);
		buf.append("\nBuild Type: " + Paint.BUILD_TYPE);
		buf.append("\nReleased on: " + Paint.RELEASED);
		buf.append("\n\n");
		buf.append("Paint.JAVA is an open-source cross-platform clone of Paint.NET. ");
		buf.append("It was started, and is currently maintained by HeroesGrave.\n");
		buf.append("\n");
		buf.append("Links:\n");
		buf.append("\n");
		buf.append("Github Repo: https://github.com/HeroesGrave/Paint.JAVA\n");
		buf.append("HeroesGrave Development Blog: http://heroesgrave.github.io/\n");
		buf.append("\n");
		buf.append("Credits:\n");
		buf.append("\n");
		buf.append("HeroesGrave\n");
		buf.append("Longor1996\n");
		buf.append("Gef4k\n");
		buf.append("MarkBernard\n");
		buf.append("pwnedary\n");
		buf.append("BurntPizza\n");
		
		area.setText(buf.toString());
		area.setEditable(false);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
		this.setResizable(false);
	}
}