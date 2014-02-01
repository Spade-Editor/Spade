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

import heroesgrave.paint.image.ShapeChange;
import heroesgrave.paint.main.Paint;

import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class Line extends Tool
{
	private Line2D.Float line;
	private ShapeChange change;
	private JCheckBox antialias;
	
	public Line(String name)
	{
		super(name);
		antialias = new JCheckBox("Antialiasing");
		antialias.setFocusable(false);
		
		JLabel label = (JLabel) menu.getComponent(0);
		
		SpringLayout layout = new SpringLayout();
		menu.setLayout(layout);
		
		menu.add(label);
		menu.add(antialias);
		
		// top/bottom
		layout.putConstraint(SpringLayout.NORTH, antialias, -2, SpringLayout.NORTH, menu);
		layout.putConstraint(SpringLayout.SOUTH, menu, 0, SpringLayout.SOUTH, label);
		
		// left/right
		layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, menu);
		layout.putConstraint(SpringLayout.WEST, antialias, 20, SpringLayout.EAST, label);
		layout.putConstraint(SpringLayout.EAST, menu, 20, SpringLayout.EAST, antialias);
		
	}
	
	public void onPressed(int x, int y, int button)
	{
		line = new Line2D.Float(x, y, x, y);
		if(button == MouseEvent.BUTTON1)
		{
			change = new ShapeChange(line, Paint.main.getLeftColour()).setAntialiasing(antialias.isSelected());
		}
		else if(button == MouseEvent.BUTTON3)
		{
			change = new ShapeChange(line, Paint.main.getRightColour()).setAntialiasing(antialias.isSelected());
		}
		Paint.main.gui.canvas.preview(change);
	}
	
	public void onReleased(int x, int y, int button)
	{
		line.x2 = x;
		line.y2 = y;
		Paint.main.gui.canvas.applyPreview();
	}
	
	public void whilePressed(int x, int y, int button)
	{
		line.x2 = x;
		line.y2 = y;
		Paint.main.gui.canvas.getPanel().repaint();
	}
}