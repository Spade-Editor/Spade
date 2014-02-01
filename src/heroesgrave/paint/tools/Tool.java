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

package heroesgrave.paint.tools;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public abstract class Tool
{
	public final String name;
	
	protected final JPanel menu;
	
	public Tool(String name)
	{
		this.name = name;
		menu = new JPanel();
		menu.setOpaque(false);
		
		SpringLayout l = new SpringLayout();
		menu.setLayout(l);
		
		JLabel label = new JLabel("Tool: " + name);
		menu.add(label);
		
		l.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, menu);
		l.putConstraint(SpringLayout.EAST, menu, 5, SpringLayout.EAST, label);
		l.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, menu);
		l.putConstraint(SpringLayout.SOUTH, menu, 0, SpringLayout.SOUTH, label);
	}
	
	public final JPanel getOptions()
	{
		return menu;
	}
	
	public abstract void onPressed(int x, int y, int button);
	
	public abstract void onReleased(int x, int y, int button);
	
	public abstract void whilePressed(int x, int y, int button);
	
	public void whileReleased(int x, int y, int button)
	{
		
	}
}