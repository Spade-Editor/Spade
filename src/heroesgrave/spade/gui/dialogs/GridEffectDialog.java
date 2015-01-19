// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
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

package heroesgrave.spade.gui.dialogs;

import heroesgrave.spade.main.Spade;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;

public class GridEffectDialog
{
	private WebDialog dialog;
	private JPanel[] panels;
	private WebPanel bottom;
	
	public GridEffectDialog(int cols, int rows, String title, ImageIcon icon)
	{
		this.dialog = new WebDialog(Spade.main.gui.frame, title);
		if(icon != null)
			dialog.setIconImage(icon.getImage());
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		dialog.setShowResizeCorner(false);
		
		panels = new JPanel[rows * cols];
		
		JPanel pane = (JPanel) dialog.getContentPane();
		
		WebPanel panel = new WebPanel();
		panel.setLayout(new GridLayout(rows, cols));
		
		for(int i = 0; i < cols * rows; i++)
		{
			panel.add(panels[i] = new WebPanel());
		}
		
		bottom = new WebPanel();
		
		pane.add(panel, BorderLayout.CENTER);
		pane.add(bottom, BorderLayout.SOUTH);
	}
	
	public JDialog getDialog()
	{
		return dialog;
	}
	
	public void close()
	{
		dialog.dispose();
	}
	
	public void display()
	{
		dialog.pack();
		dialog.setResizable(false);
		dialog.setVisible(true);
		dialog.center(Spade.main.gui.frame);
	}
	
	public JPanel getBottomPanel()
	{
		return bottom;
	}
	
	public JPanel getPanel(int i)
	{
		return panels[i];
	}
}
