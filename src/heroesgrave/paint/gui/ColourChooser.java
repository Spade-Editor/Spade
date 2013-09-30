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

package heroesgrave.paint.gui;

import heroesgrave.paint.gui.InfoMenu.ColourPanel;
import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.misc.ColourNumberFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	private ColourPanel colour;

	public ColourChooser()
	{
		dialog = new CentredJDialog();
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
				Paint.main.setColour(getColour());
			}
		});
		g.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				tg.setText("" + g.getValue());
				Paint.main.setColour(getColour());
			}
		});
		b.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				tb.setText("" + b.getValue());
				Paint.main.setColour(getColour());
			}
		});
		a.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				ta.setText("" + a.getValue());
				Paint.main.setColour(getColour());
			}
		});

		tr.addFocusListener(new FocusListener()
		{
			public void focusLost(FocusEvent e)
			{
				r.setValue(MathUtils.clamp(Integer.parseInt(tr.getText()), 255, 0));
				tr.setText("" + MathUtils.clamp(Integer.parseInt(tr.getText()), 255, 0));
				Paint.main.setColour(getColour());
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
				Paint.main.setColour(getColour());
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
				Paint.main.setColour(getColour());
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
				Paint.main.setColour(getColour());
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

		top.add(left, BorderLayout.CENTER);
		top.add(right, BorderLayout.EAST);
		panel.add(top, BorderLayout.NORTH);
		panel.add(bottom, BorderLayout.SOUTH);

		dialog.pack();
		dialog.setResizable(false);
		dialog.setAlwaysOnTop(true);
	}

	public int getColour()
	{
		return (a.getValue() << 24) + (r.getValue() << 16) + (g.getValue() << 8) + b.getValue();
	}

	public void setColour(int colour)
	{
		a.setValue((colour >> 24) & 0xFF);
		r.setValue((colour >> 16) & 0xFF);
		g.setValue((colour >> 8) & 0xFF);
		b.setValue(colour & 0xFF);
		ta.setText("" + ((colour >> 24) & 0xFF));
		tr.setText("" + ((colour >> 16) & 0xFF));
		tg.setText("" + ((colour >> 8) & 0xFF));
		tb.setText("" + (colour & 0xFF));
		this.colour.setColour(colour);
	}

	public void sync()
	{

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

	private static class CentredLabel extends JLabel
	{
		private static final long serialVersionUID = -6858861607042506044L;

		public CentredLabel(String text)
		{
			super(text);
			this.setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
}
