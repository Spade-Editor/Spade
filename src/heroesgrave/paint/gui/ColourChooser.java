/*
 *	Copyright 2013 HeroesGrave & markbernard
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

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.misc.ColourNumberFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument;

public class ColourChooser
{
	private JDialog dialog;
	private JSlider r, g, b, a;
	private JTextField tr, tg, tb, ta;
	private JRadioButton leftRadio, rightRadio, RGB, HSB;
	private ColourPanel colour;
	
	/**
	 * The 'left'-colour.
	 **/
	private int leftColour;
	
	/**
	 * The 'right'-colour.
	 **/
	private int rightColour;
	
	public ColourChooser()
	{
		dialog = new CentredJDialog(Paint.main.gui.frame, "Colour Chooser");
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		JPanel panel = (JPanel) dialog.getContentPane();
		panel.setLayout(new BorderLayout());
		
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		
		bottom.setLayout(new GridLayout(0, 1));
		top.setLayout(new BorderLayout());
		right.setLayout(new GridLayout(0, 2));
		
		r = new JSlider(0, 255, 0);
		g = new JSlider(0, 255, 0);
		b = new JSlider(0, 255, 0);
		a = new JSlider(0, 255, 255);
		
		tr = new JTextField("0");
		tg = new JTextField("0");
		tb = new JTextField("0");
		ta = new JTextField("255");
		
		r.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				tr.setText("" + r.getValue());
				colourChanged();
			}
		});
		g.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				tg.setText("" + g.getValue());
				colourChanged();
			}
		});
		b.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				tb.setText("" + b.getValue());
				colourChanged();
			}
		});
		a.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				ta.setText("" + a.getValue());
				colourChanged();
			}
		});
		
		tr.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e)
			{
				r.setValue(MathUtils.clamp(Integer.parseInt(tr.getText()), 255, 0));
				tr.setText("" + MathUtils.clamp(Integer.parseInt(tr.getText()), 255, 0));
				if(leftRadio.isSelected())
				{
					leftColour = ((leftColour & 0xff00ffff) | ((r.getValue() & 0xff) << 16));
					Paint.main.setLeftColour(leftColour);
				}
				else if(rightRadio.isSelected())
				{
					rightColour = ((rightColour & 0xff00ffff) | ((r.getValue() & 0xff) << 16));
					Paint.main.setRightColour(rightColour);
				}
			}
			
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		tg.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e)
			{
				g.setValue(MathUtils.clamp(Integer.parseInt(tg.getText()), 255, 0));
				tg.setText("" + MathUtils.clamp(Integer.parseInt(tg.getText()), 255, 0));
				if(leftRadio.isSelected())
				{
					leftColour = ((leftColour & 0xffff00ff) | ((g.getValue() & 0xff) << 8));
					Paint.main.setLeftColour(leftColour);
				}
				else if(rightRadio.isSelected())
				{
					rightColour = ((rightColour & 0xffff00ff) | ((g.getValue() & 0xff) << 8));
					Paint.main.setRightColour(rightColour);
				}
			}
			
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		tb.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e)
			{
				b.setValue(MathUtils.clamp(Integer.parseInt(tb.getText()), 255, 0));
				tb.setText("" + MathUtils.clamp(Integer.parseInt(tb.getText()), 255, 0));
				if(leftRadio.isSelected())
				{
					leftColour = ((leftColour & 0xffffff00) | (b.getValue() & 0xff));
					Paint.main.setLeftColour(leftColour);
				}
				else if(rightRadio.isSelected())
				{
					rightColour = ((rightColour & 0xffffff00) | (b.getValue() & 0xff));
					Paint.main.setRightColour(rightColour);
				}
			}
			
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		ta.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e)
			{
				a.setValue(MathUtils.clamp(Integer.parseInt(ta.getText()), 255, 0));
				ta.setText("" + MathUtils.clamp(Integer.parseInt(ta.getText()), 255, 0));
				if(leftRadio.isSelected())
				{
					leftColour = ((leftColour & 0x00ffffff) | ((a.getValue() & 0xff) << 24));
					Paint.main.setLeftColour(leftColour);
				}
				else if(rightRadio.isSelected())
				{
					rightColour = ((rightColour & 0x00ffffff) | ((a.getValue() & 0xff) << 24));
					Paint.main.setRightColour(rightColour);
				}
			}
			
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		
		tr.setColumns(3);
		tg.setColumns(3);
		tb.setColumns(3);
		ta.setColumns(3);
		
		((AbstractDocument) tr.getDocument()).setDocumentFilter(new ColourNumberFilter());
		((AbstractDocument) tg.getDocument()).setDocumentFilter(new ColourNumberFilter());
		((AbstractDocument) tb.getDocument()).setDocumentFilter(new ColourNumberFilter());
		((AbstractDocument) ta.getDocument()).setDocumentFilter(new ColourNumberFilter());
		
		right.add(new CentredLabel("Red: "));
		right.add(tr);
		right.add(new CentredLabel("Green: "));
		right.add(tg);
		right.add(new CentredLabel("Blue: "));
		right.add(tb);
		right.add(new CentredLabel("Alpha: "));
		right.add(ta);
		
		bottom.add(r);
		bottom.add(g);
		bottom.add(b);
		bottom.add(a);
		
		colour = new ColourPanel();
		colour.setPreferredSize(new Dimension(100, 100));
		left.add(colour);
		
		JPanel radioButtonPanel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		leftRadio = new JRadioButton("Primary");
		group.add(leftRadio);
		leftRadio.setSelected(true);
		radioButtonPanel.add(leftRadio);
		rightRadio = new JRadioButton("Secondary");
		group.add(rightRadio);
		radioButtonPanel.add(rightRadio);
		
		JPanel colorModelPanel = new JPanel();
		ButtonGroup group2 = new ButtonGroup();
		RGB = new JRadioButton("RGB");
		group2.add(RGB);
		RGB.setSelected(true);
		colorModelPanel.add(RGB);
		HSB = new JRadioButton("HSB");
		group2.add(HSB);
		colorModelPanel.add(HSB);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		
		buttonPanel.add(radioButtonPanel, BorderLayout.NORTH);
		// Not working yet. Supposed to enable HSB colour input.
		// buttonPanel.add(colorModelPanel, BorderLayout.SOUTH);
		// ...
		// XXX: Longor1996 comment:
		//
		// You should go and:
		// 1. Make different methods for loading up the single color tabs.
		// 2. Yes: TABS. Use TABS (JTabbedPane?) to do the different color choosers! It just looks better in my opinion.
		// 3. You should go and use the property-change methods of the inputs to update the color value.
		// 4. Format the stuff in here!
		//
		// XXX: HeroesGrave comment:
		// 
		// This was meant to be temporary, but got a little bit bloated.
		//
		// XXX: Longor1996 comment:
		// I think I am going to rework this entire class. Its just one huge mess in here.
		// I am going to make some tabs, each having its own way of selecting the color.
		// Tabs:
		// - RGB Circle Chooser
		// - RGB Slider Chooser
		// - HSV Circle Chooser
		// - HSV Slider Chooser
		// - HSB Circle Chooser
		// - HSB Slider Chooser
		// - Color Pellet/Pallet like in Paint.NET
		// - User defined Color Pellet/Pallet (Good idea?)
		// - (?) 3D-Cube Color Chooser
		//
		
		
		top.add(buttonPanel, BorderLayout.NORTH);
		
		top.add(left, BorderLayout.CENTER);
		top.add(right, BorderLayout.EAST);
		panel.add(top, BorderLayout.NORTH);
		panel.add(bottom, BorderLayout.SOUTH);
		
		dialog.pack();
		dialog.setResizable(false);
		
		leftRadio.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int leftColour = colour.getLeftColour();
				a.setValue((leftColour >> 24) & 0xFF);
				r.setValue((leftColour >> 16) & 0xFF);
				g.setValue((leftColour >> 8) & 0xFF);
				b.setValue(leftColour & 0xFF);
				ta.setText("" + ((leftColour >> 24) & 0xFF));
				tr.setText("" + ((leftColour >> 16) & 0xFF));
				tg.setText("" + ((leftColour >> 8) & 0xFF));
				tb.setText("" + (leftColour & 0xFF));
			}
		});
		rightRadio.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int rightColour = colour.getRightColour();
				a.setValue((rightColour >> 24) & 0xFF);
				r.setValue((rightColour >> 16) & 0xFF);
				g.setValue((rightColour >> 8) & 0xFF);
				b.setValue(rightColour & 0xFF);
				ta.setText("" + ((rightColour >> 24) & 0xFF));
				tr.setText("" + ((rightColour >> 16) & 0xFF));
				tg.setText("" + ((rightColour >> 8) & 0xFF));
				tb.setText("" + (rightColour & 0xFF));
			}
		});
	}
	
	public int getLeftColour()
	{
		return leftColour;
	}
	
	public int getRightColour()
	{
		return rightColour;
	}
	
	public void setLeftColour(int colour)
	{
		if(RGB.isSelected())
		{
			if(leftRadio.isSelected())
			{
				a.setValue((colour >> 24) & 0xFF);
				r.setValue((colour >> 16) & 0xFF);
				g.setValue((colour >> 8) & 0xFF);
				b.setValue(colour & 0xFF);
				ta.setText("" + ((colour >> 24) & 0xFF));
				tr.setText("" + ((colour >> 16) & 0xFF));
				tg.setText("" + ((colour >> 8) & 0xFF));
				tb.setText("" + (colour & 0xFF));
			}
		}
		leftColour = colour;
		this.colour.setLeftColour(colour);
	}
	
	public void setRightColour(int colour)
	{
		if(RGB.isSelected())
		{
			if(rightRadio.isSelected())
			{
				a.setValue((colour >> 24) & 0xFF);
				r.setValue((colour >> 16) & 0xFF);
				g.setValue((colour >> 8) & 0xFF);
				b.setValue(colour & 0xFF);
				ta.setText("" + ((colour >> 24) & 0xFF));
				tr.setText("" + ((colour >> 16) & 0xFF));
				tg.setText("" + ((colour >> 8) & 0xFF));
				tb.setText("" + (colour & 0xFF));
			}
		}
		rightColour = colour;
		this.colour.setRightColour(colour);
	}
	
	public void colourChanged()
	{
		if(RGB.isSelected())
		{
			if(leftRadio.isSelected())
			{
				leftColour = (a.getValue() << 24) + (r.getValue() << 16) + (g.getValue() << 8) + b.getValue();
				Paint.main.setLeftColour(leftColour);
			}
			else if(rightRadio.isSelected())
			{
				rightColour = (a.getValue() << 24) + (r.getValue() << 16) + (g.getValue() << 8) + b.getValue();
				Paint.main.setRightColour(rightColour);
			}
		}
	}
	
	public void show()
	{
		dialog.setVisible(true);
	}
	
	public void hide()
	{
		dialog.setVisible(false);
	}
	
	public void toggle()
	{
		dialog.setVisible(!dialog.isVisible());
	}
	
	public boolean isVisible()
	{
		return dialog.isVisible();
	}
	
	public static class CentredLabel extends JLabel
	{
		private static final long serialVersionUID = -6858861607042506044L;
		
		public CentredLabel(String text)
		{
			super(text);
			this.setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
	
	public static class ColourPanel extends JPanel
	{
		private static final long serialVersionUID = 7541204326016173356L;
		
		private Color leftColour;
		private Color rightColour;
		
		public ColourPanel()
		{
			
		}
		
		public void setLeftColour(int colour)
		{
			this.leftColour = new Color(colour);
			this.repaint();
		}
		
		public void setRightColour(int colour)
		{
			this.rightColour = new Color(colour);
			this.repaint();
		}
		
		public int getLeftColour()
		{
			return (((leftColour.getAlpha() & 0xff) << 24) | ((leftColour.getRed() & 0xff) << 16) | ((leftColour.getGreen() & 0xff) << 8) | (leftColour
					.getBlue() & 0xff));
		}
		
		public int getRightColour()
		{
			return (((rightColour.getAlpha() & 0xff) << 24) | ((rightColour.getRed() & 0xff) << 16) | ((rightColour.getGreen() & 0xff) << 8) | (rightColour
					.getBlue() & 0xff));
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			g.setColor(leftColour);
			g.fillRect(0, 0, this.getWidth() / 2, this.getHeight());
			g.setColor(rightColour);
			g.fillRect(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
		}
	}
	
	public JDialog getDialog()
	{
		return dialog;
	}
}