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

import heroesgrave.utils.math.MathUtils;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import com.alee.laf.menu.MenuBarStyle;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;

public class InfoMenuBar
{
	private WebMenu scale, saved, size, coords;
	private MemoryWatcher memoryWatcher;
	
	public WebMenuBar createInfoMenuBar()
	{
		WebMenuBar menuBar = new WebMenuBar();
		menuBar.setMenuBarStyle(MenuBarStyle.standalone);
		
		WebMenuBar left = new WebMenuBar();
		left.setUndecorated(true);
		left.setLayout(new GridLayout(1, 0));
		
		left.setBackground(PaintCanvas.TRANSPARENT);
		
		scale = new WebMenu("");
		saved = new WebMenu("");
		size = new WebMenu("");
		coords = new WebMenu("");
		
		left.add(new WebMenu());
		left.add(coords);
		left.add(size);
		left.add(scale);
		
		left.add(saved);
		
		menuBar.setLayout(new BorderLayout());
		menuBar.add(left, BorderLayout.CENTER);
		
		// Check if the MemoryWatcher should be activated.
		if(System.getProperty("DmemoryWatcherFlag") != null)
		{
			memoryWatcher = new MemoryWatcher();
			menuBar.add(memoryWatcher, BorderLayout.EAST);
		}
		
		return menuBar;
	}
	
	public void clear()
	{
		scale.setText("");
		saved.setText("");
		size.setText("");
		coords.setText("");
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
		coords.setText("(" + x + ", " + y + ")");
	}
}
