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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebTextPane;

public class AboutDialog extends WebDialog
{
	private static final long serialVersionUID = -1020702247965837857L;
	
	public AboutDialog(WebFrame frame)
	{
		super(frame, "About Paint.JAVA");
		this.center(400, 300);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		WebTextPane area = new WebTextPane();
		StyledDocument doc = area.getStyledDocument();
		
		addStylesToDocument(doc);
		
		append(doc, "Paint.JAVA", "large");
		append(doc, "\n    Version: ", "bold");
		append(doc, Paint.VERSION, "regular");
		append(doc, "\n    Build Type: ", "bold");
		append(doc, Paint.BUILD_TYPE, "regular");
		append(doc, "\n    Released on: ", "bold");
		append(doc, Paint.RELEASED, "regular");
		append(doc, "\n\n", "regular");
		append(doc, "Paint.JAVA is an open-source cross-platform clone of Paint.NET. ", "regular");
		append(doc, "It was started, and is currently maintained by HeroesGrave.\n", "regular");
		append(doc, "\n", "regular");
		append(doc, "Links:\n", "large");
		append(doc, "    Github Repo:\n", "bold");
		append(doc, "        https://github.com/PaintDotJava/Paint.JAVA/\n", "italic");
		append(doc, "    HeroesGrave Development:\n", "bold");
		append(doc, "        http://heroesgrave.github.io/\n", "italic");
		append(doc, "\n", "regular");
		append(doc, "Credits:\n", "large");
		append(doc, "    HeroesGrave\n", "regular");
		append(doc, "    Longor1996\n", "regular");
		append(doc, "    BurntPizza\n", "regular");
		append(doc, "    Gef4k\n", "regular");
		append(doc, "    MarkBernard\n", "regular");
		append(doc, "    pwnedary\n", "regular");
		
		area.setEditable(false);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
		this.setResizable(false);
	}
	
	protected static void append(StyledDocument doc, String str, String sty)
	{
		try
		{
			doc.insertString(doc.getLength(), str, doc.getStyle(sty));
		}
		catch(BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	
	protected static void addStylesToDocument(StyledDocument doc)
	{
		//Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		
		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);
		
		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);
		
		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);
		
		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);
		StyleConstants.setBold(s, true);
	}
}
