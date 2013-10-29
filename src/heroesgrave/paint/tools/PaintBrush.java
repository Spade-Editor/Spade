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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;

import heroesgrave.paint.gui.ColourChooser.CentredLabel;
import heroesgrave.paint.main.Paint;
import heroesgrave.paint.main.PixelChange;
import heroesgrave.paint.tools.Brush;

public class PaintBrush extends Brush
{
	private JSlider slider;
	
	public PaintBrush(String name)
	{
		super(name);
		slider = new JSlider(1, 8, 1);
		
		menu.setLayout(new GridLayout(1, 8));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		
		panel.add(new CentredLabel("Size: "), BorderLayout.WEST);
		panel.add(slider, BorderLayout.CENTER);
		
		slider.setFocusable(false);
		
		menu.add(panel);
		menu.add(new JSeparator(JSeparator.VERTICAL));
		menu.add(new JSeparator(JSeparator.VERTICAL));
		menu.add(new JSeparator(JSeparator.VERTICAL));
	}
	
	public void brush(int x, int y, int button)
	{
		if(x < 0 || y < 0 || x >= Paint.main.gui.canvas.getImage().getWidth() || y >= Paint.main.gui.canvas.getImage().getHeight())
			return;
		if(slider.getValue() == 1)
		{
			if(button == MouseEvent.BUTTON1)
			{
				buffer(new PixelChange(x, y, Paint.main.getLeftColour()));
			}
			else if(button == MouseEvent.BUTTON3)
			{
				buffer(new PixelChange(x, y, Paint.main.getRightColour()));
			}
		}
	}
}