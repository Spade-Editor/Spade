// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
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

package heroesgrave.spade.editing;

import heroesgrave.spade.gui.Tools;
import heroesgrave.spade.gui.misc.WeblafWrapper;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.main.Spade;

import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;

public class Picker extends Tool
{
	private JCheckBox switchPencil;
	
	public Picker(String name)
	{
		super(name);
		
		// XXX: WebCheckBoxMenuItem is broken.
		
		this.switchPencil = WeblafWrapper.createCheckBox();
		switchPencil.setText(" Switch to Pencil");
		switchPencil.setSelected(true);
		
		menu.add(WeblafWrapper.asMenuItem(switchPencil));
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		if(x < 0 || y < 0 || x >= layer.getWidth() || y >= layer.getHeight())
			return;
		if(button == MouseEvent.BUTTON1)
		{
			Spade.main.setLeftColour(layer.getImage().getPixel(x, y), false);
		}
		else if(button == MouseEvent.BUTTON3)
		{
			Spade.main.setRightColour(layer.getImage().getPixel(x, y), false);
		}
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		if(x < 0 || y < 0 || x >= layer.getWidth() || y >= layer.getHeight())
			return;
		if(button == MouseEvent.BUTTON1)
		{
			Spade.main.setLeftColour(layer.getImage().getPixel(x, y), false);
		}
		else if(button == MouseEvent.BUTTON3)
		{
			Spade.main.setRightColour(layer.getImage().getPixel(x, y), false);
		}
		
		if(switchPencil.isSelected() ^ isCtrlDown())
		{
			Spade.setTool(Tools.DEF);
		}
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(x < 0 || y < 0 || x >= layer.getWidth() || y >= layer.getHeight())
			return;
		if(button == MouseEvent.BUTTON1)
		{
			Spade.main.setLeftColour(layer.getImage().getPixel(x, y), false);
		}
		else if(button == MouseEvent.BUTTON3)
		{
			Spade.main.setRightColour(layer.getImage().getPixel(x, y), false);
		}
	}
}
