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

package heroesgrave.spade.gui.colorchooser;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ModePanel extends JPanel
{
	private ColourChooser parent;
	
	public ModePanel(ColourChooser p)
	{
		super();
		this.parent = p;
		
		setSize(52, 52);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		
		this.setLayout(new GridLayout(2, 1));
		
		final JButton less = new JButton("Less");
		final JButton more = new JButton("More");
		
		less.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(!parent.showLess())
				{
					less.setEnabled(false);
				}
				else
				{
					more.setEnabled(true);
				}
			}
		});
		
		more.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(!parent.showMore())
				{
					more.setEnabled(false);
				}
				else
				{
					less.setEnabled(true);
				}
			}
		});
		
		this.add(less);
		this.add(more);
	}
}
