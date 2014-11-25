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

package heroesgrave.spade.gui.colorchooser;

import heroesgrave.spade.gui.colorchooser.event.ColourEventBroadcaster;
import heroesgrave.spade.gui.colorchooser.event.ColourListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ColourSliderField extends JTextField implements ActionListener, ColourListener, FocusListener
{
	private ColourEventBroadcaster parent;
	private Channel channel;
	private int value;
	
	public ColourSliderField(Channel channel, ColourEventBroadcaster parent)
	{
		setSize(45, 26);
		setHorizontalAlignment(JTextField.RIGHT);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		
		this.channel = channel;
		
		this.parent = parent;
		parent.addColorListener(this);
		
		this.addActionListener(this);
		this.addFocusListener(this);
	}
	
	public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
	{
		if(!primary)
			return;
		switch(channel)
		{
			case Red:
				value = r;
				break;
			case Green:
				value = g;
				break;
			case Blue:
				value = b;
				break;
			case Hue:
				value = h;
				break;
			case Saturation:
				value = s;
				break;
			case Value:
				value = v;
				break;
			case Alpha:
				value = a;
				break;
		}
		this.setText("" + value);
	}
	
	public void update()
	{
		try
		{
			if(channel == Channel.Hue)
				value = Integer.parseInt(this.getText()) & 0x3FF;
			else
				value = Integer.parseInt(this.getText()) & 0xFF;
			this.setText("" + value);
			parent.makeChange(this, channel, value, true);
			parent.broadcastChanges(this);
		}
		catch(NumberFormatException e)
		{
			this.setText("ERR");
		}
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		update();
	}
	
	@Override
	public void focusGained(FocusEvent e)
	{
	}
	
	@Override
	public void focusLost(FocusEvent e)
	{
		update();
	}
}
