/*
 *	Copyright 2013 HeroesGrave
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

/*
 *	Copyright 2013 HeroesGrave
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

import heroesgrave.paint.gui.ColourChooser.CentredLabel;
import heroesgrave.paint.image.ShapeChange;
import heroesgrave.paint.main.Paint;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class Eraser extends Tool
{
	private GeneralPath path;
	private ShapeChange change;
	private JSlider slider;
	
	public Eraser(String name)
	{
		super(name);
		slider = new JSlider(0, 16, 0);
		menu.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		
		panel.add(new CentredLabel("Size: "), BorderLayout.WEST);
		panel.add(slider, BorderLayout.CENTER);
		
		slider.setFocusable(false);
		
		menu.add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public void onPressed(int x, int y, int button)
	{
		path = new GeneralPath();
		path.moveTo(x, y);
		change = new ShapeChange(path, 0x000000, new BasicStroke(slider.getValue() + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		Paint.main.gui.canvas.preview(change);
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
	public void onReleased(int x, int y, int button)
	{
		if(path != null)
		{
			path.lineTo(x, y);
		}
		Paint.main.gui.canvas.applyPreview();
		path = null;
		change = null;
	}
	
	public void whilePressed(int x, int y, int button)
	{
		if(path != null)
		{
			path.lineTo(x, y);
		}
		Paint.main.gui.canvas.getPanel().repaint();
	}
}