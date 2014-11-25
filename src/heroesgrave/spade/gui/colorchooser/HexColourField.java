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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.alee.managers.popup.WebPopup;

@SuppressWarnings("serial")
public class HexColourField extends WebPopup implements ActionListener, ColourListener, KeyListener
{
	private ColourEventBroadcaster parent;
	private int primary, secondary;
	private JLabel label;
	private JTextField input;
	private boolean showFor;
	
	public HexColourField(ColourEventBroadcaster parent)
	{
		super();
		this.parent = parent;
		parent.addColorListener(this);
		
		this.input = new JTextField(8);
		this.label = new JLabel("<html><b>Primary</b></html>");
		label.setHorizontalAlignment(JLabel.CENTER);
		input.setHorizontalAlignment(JLabel.CENTER);
		
		input.setFont(Font.decode(Font.MONOSPACED));
		input.addActionListener(this);
		input.addKeyListener(this);
		
		this.setLayout(new BorderLayout());
		this.add(label, BorderLayout.NORTH);
		this.add(input, BorderLayout.SOUTH);
	}
	
	public void showFor(boolean primary, Component c)
	{
		this.showFor = primary;
		if(showFor)
		{
			label.setText("<html><b>Primary</b></html>");
			input.setText(padZeros(Integer.toHexString(this.primary), 8));
		}
		else
		{
			label.setText("<html><b>Secondary</b></html>");
			input.setText(padZeros(Integer.toHexString(this.secondary), 8));
		}
		this.packPopup();
		this.showPopup(c);
		input.requestFocus(true);
	}
	
	public void changeColor(int r, int g, int b, int h, int s, int v, int a, boolean primary)
	{
		if(primary)
		{
			this.primary = (a << 24) | (r << 16) | (g << 8) | b;
		}
		else
		{
			this.secondary = (a << 24) | (r << 16) | (g << 8) | b;
		}
		if(this.isVisible())
		{
			if(showFor)
			{
				input.setText(padZeros(Integer.toHexString(this.primary), 8));
			}
			else
			{
				input.setText(padZeros(Integer.toHexString(this.secondary), 8));
			}
		}
	}
	
	public String padZeros(String s, int len)
	{
		while(s.length() < len)
		{
			s = "0" + s;
		}
		return s;
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		try
		{
			String text = input.getText();
			if(text.length() == 8)
			{
				int alpha = Integer.parseInt(text.substring(0, 2), 16);
				int red = Integer.parseInt(text.substring(2, 4), 16);
				int green = Integer.parseInt(text.substring(4, 6), 16);
				int blue = Integer.parseInt(text.substring(6, 8), 16);
				
				parent.makeChange(this, Channel.Alpha, alpha, showFor);
				parent.makeChange(this, Channel.Red, red, showFor);
				parent.makeChange(this, Channel.Green, green, showFor);
				parent.makeChange(this, Channel.Blue, blue, showFor);
				parent.broadcastChanges(this);
				this.hidePopup();
			}
			else if(text.length() == 6)
			{
				int red = Integer.parseInt(text.substring(0, 2), 16);
				int green = Integer.parseInt(text.substring(2, 4), 16);
				int blue = Integer.parseInt(text.substring(4, 6), 16);
				
				parent.makeChange(this, Channel.Red, red, showFor);
				parent.makeChange(this, Channel.Green, green, showFor);
				parent.makeChange(this, Channel.Blue, blue, showFor);
				parent.broadcastChanges(this);
				this.hidePopup();
			}
			else if(text.length() == 3)
			{
				int red = Integer.parseInt(text.substring(0, 1), 16);
				int green = Integer.parseInt(text.substring(1, 2), 16);
				int blue = Integer.parseInt(text.substring(2, 3), 16);
				
				parent.makeChange(this, Channel.Red, red | (red << 4), showFor);
				parent.makeChange(this, Channel.Green, green | (green << 4), showFor);
				parent.makeChange(this, Channel.Blue, blue | (blue << 4), showFor);
				parent.broadcastChanges(this);
				this.hidePopup();
			}
		}
		catch(NumberFormatException e)
		{
			input.setText("ERR");
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.hidePopup();
		}
	}
	
	public void keyReleased(KeyEvent arg0)
	{
	}
	
	public void keyTyped(KeyEvent arg0)
	{
	}
}
