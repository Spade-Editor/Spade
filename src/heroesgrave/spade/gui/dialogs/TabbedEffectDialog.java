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

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.tabbedpane.TabStretchType;
import com.alee.laf.tabbedpane.WebTabbedPane;

public class TabbedEffectDialog
{
	private WebDialog dialog;
	private WebPanel[] panels;
	private WebTabbedPane tabs;
	private WebPanel bottom;
	
	public TabbedEffectDialog(int panes, String title, ImageIcon icon)
	{
		dialog = new WebDialog(Spade.main.gui.frame, title);
		if(icon != null)
			dialog.setIconImage(icon.getImage());
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setAlwaysOnTop(true);
		dialog.setAutoRequestFocus(true);
		dialog.setShowResizeCorner(false);
		
		panels = new WebPanel[panes];
		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new BorderLayout());
		
		tabs = new WebTabbedPane();
		tabs.setTabStretchType(TabStretchType.always);
		panel.add(tabs, BorderLayout.CENTER);
		
		for(int i = 0; i < panes; i++)
		{
			tabs.add(panels[i] = new WebPanel());
		}
		
		bottom = new WebPanel();
		panel.add(bottom, BorderLayout.SOUTH);
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
	
	public JPanel getPanel(int i)
	{
		return panels[i];
	}
	
	public int getSelectedPanel()
	{
		return tabs.getSelectedIndex();
	}
	
	public JPanel getBottomPanel()
	{
		return bottom;
	}
	
	public void setPanelTitle(int i, String title)
	{
		tabs.setTitleAt(i, title);
	}
}
