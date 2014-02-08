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

import heroesgrave.utils.math.MathUtils;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class InfoMenuBar
{
	private JPanel tool;
	private JLabel scale, saved, size, coords;
	private MemoryWatcher memoryWatcher;
	
	public JComponent createInfoMenuBar()
	{
		JComponent menuBar = new JMenuBar();
		
		SpringLayout layout = new SpringLayout();
		menuBar.setLayout(layout);
		
		scale = new JLabel("Scale: 100%");
		saved = new JLabel("Saved: Yes");
		size = new JLabel();
		coords = new JLabel();
		
		// Check if the MemoryWatcher should be activated.
		if(System.getProperty("DmemoryWatcherFlag") != null)
		{
			memoryWatcher = new MemoryWatcher();
		}
		
		scale.setHorizontalAlignment(SwingConstants.CENTER);
		saved.setHorizontalAlignment(SwingConstants.CENTER);
		size.setHorizontalAlignment(SwingConstants.RIGHT);
		coords.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tool = new JPanel();
		tool.setOpaque(false);
		
		JPanel spacer = new JPanel();
		spacer.setOpaque(false);
		
		menuBar.add(scale);
		menuBar.add(saved);
		menuBar.add(size);
		menuBar.add(coords);
		menuBar.add(tool);
		menuBar.add(spacer);
		
		layout.putConstraint(SpringLayout.WEST, scale, 20, SpringLayout.WEST, menuBar);
		layout.putConstraint(SpringLayout.WEST, saved, 40, SpringLayout.EAST, scale);
		layout.putConstraint(SpringLayout.WEST, tool, 40, SpringLayout.EAST, saved);
		layout.putConstraint(SpringLayout.WEST, coords, 40, SpringLayout.EAST, spacer);
		layout.putConstraint(SpringLayout.WEST, size, 40, SpringLayout.EAST, coords);
		//layout.putConstraint(SpringLayout.WEST, spacer, 0, SpringLayout.EAST, coords);
		layout.putConstraint(SpringLayout.EAST, menuBar, 20, SpringLayout.EAST, size);
		
		// Check if the memory-watcher is not null (eg: Activated or not), then add constraints.
		if(memoryWatcher != null)
			layout.putConstraint(SpringLayout.EAST, memoryWatcher, 0, SpringLayout.EAST, menuBar);
		
		layout.putConstraint(SpringLayout.NORTH, scale, 5, SpringLayout.NORTH, menuBar);
		layout.putConstraint(SpringLayout.NORTH, saved, 5, SpringLayout.NORTH, menuBar);
		layout.putConstraint(SpringLayout.NORTH, size, 5, SpringLayout.NORTH, menuBar);
		layout.putConstraint(SpringLayout.NORTH, coords, 5, SpringLayout.NORTH, menuBar);
		
		layout.putConstraint(SpringLayout.SOUTH, menuBar, 7, SpringLayout.SOUTH, scale);
		
		if(memoryWatcher != null)
		{
			menuBar.add(memoryWatcher);
		}
		
		return menuBar;
	}
	
	public JPanel getSpace()
	{
		return tool;
	}
	
	public void setScale(float scale)
	{
		this.scale.setText("Scale: " + MathUtils.round(scale * 100) + "%");
	}
	
	public void setSaved(boolean saved)
	{
		this.saved.setText("Saved: " + (saved ? "Yes" : "No"));
	}
	
	public void setSize(int w, int h)
	{
		size.setText(w + " x " + h);
	}
	
	public void setMouseCoords(int x, int y)
	{
		coords.setText("[" + x + ", " + y + "]");
	}
	
	public void setMouseCoords(int x, int y, int newX, int newY, boolean selecting)
	{
		coords.setText("[" + x + ", " + y + "]" + " --> " + "[" + newX + ", " + newY + "]"
				+ (selecting ? " (" + Math.abs(newX - x) + " x " + Math.abs(newY - y) + ")" : ""));
	}
}