// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
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

package heroesgrave.paint.gui.colorchooser;

import static heroesgrave.paint.gui.colorchooser.ColorUtils.toARGB;
import static heroesgrave.paint.gui.colorchooser.ColorUtils.toHSVA;
import heroesgrave.paint.gui.colorchooser.PalettePanel.PaletteMode;
import heroesgrave.paint.gui.colorchooser.event.ColourEventBroadcaster;
import heroesgrave.paint.gui.colorchooser.event.ColourListener;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alee.laf.rootpane.WebDialog;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColourChooser extends WebDialog implements ColourEventBroadcaster
{
	private static final int MODE_COUNT = 4;
	
	private int[] primary = {0, 0, 0, 0, 0, 0, 255};
	private int[] secondary = {0, 0, 0, 0, 0, 0, 255};
	private int mode;
	private Object lastSource; // Is this needed?
	
	private List<ColourListener> listeners;
	
	private PalettePanel palletPanel;
	
	private JComponent wheel, /*fieldPanel, sliderPanel,*/rgbPanel, hsvPanel, alphaPanel, indicator, modePanel, settings;
	private JPanel top, sliders, left;
	
	public ColourChooser()
	{
		super();
		this.setTitle("Colour Chooser");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		listeners = new ArrayList<>();
		
		palletPanel = new PalettePanel(this, Palette.defaultPallet());
		wheel = new ColorWheel(this);
		indicator = new ColorIndicator(this);
		modePanel = new ModePanel(this);
		
		rgbPanel = new JPanel();
		hsvPanel = new JPanel();
		alphaPanel = new JPanel();
		rgbPanel.setLayout(new BoxLayout(rgbPanel, BoxLayout.X_AXIS));
		hsvPanel.setLayout(new BoxLayout(hsvPanel, BoxLayout.X_AXIS));
		alphaPanel.setLayout(new BoxLayout(alphaPanel, BoxLayout.X_AXIS));
		
		JPanel sliderPanel = new JPanel();
		JPanel fieldPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		
		fieldPanel.add(new Spacer(0, 14));
		fieldPanel.add(new ColourSliderField(Channel.Red, this));
		fieldPanel.add(new ColourSliderField(Channel.Green, this));
		fieldPanel.add(new ColourSliderField(Channel.Blue, this));
		sliderPanel.add(new SpacerLabel("<html><b>RGB:</b></html>", 48, 16));
		sliderPanel.add(new ColourSlider(Channel.Red, this));
		sliderPanel.add(new Spacer(0, 8));
		sliderPanel.add(new ColourSlider(Channel.Green, this));
		sliderPanel.add(new Spacer(0, 8));
		sliderPanel.add(new ColourSlider(Channel.Blue, this));
		
		rgbPanel.add(sliderPanel);
		rgbPanel.add(fieldPanel);
		
		sliderPanel = new JPanel();
		fieldPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		
		fieldPanel.add(new Spacer(0, 14));
		fieldPanel.add(new ColourSliderField(Channel.Hue, this));
		fieldPanel.add(new ColourSliderField(Channel.Saturation, this));
		fieldPanel.add(new ColourSliderField(Channel.Value, this));
		sliderPanel.add(new SpacerLabel("<html><b>HSV:</b></html>", 48, 16));
		sliderPanel.add(new ColourSlider(Channel.Hue, this));
		sliderPanel.add(new Spacer(0, 8));
		sliderPanel.add(new ColourSlider(Channel.Saturation, this));
		sliderPanel.add(new Spacer(0, 8));
		sliderPanel.add(new ColourSlider(Channel.Value, this));
		
		hsvPanel.add(sliderPanel);
		hsvPanel.add(fieldPanel);
		
		sliderPanel = new JPanel();
		fieldPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		
		fieldPanel.add(new Spacer(0, 14));
		fieldPanel.add(new ColourSliderField(Channel.Alpha, this));
		sliderPanel.add(new SpacerLabel("<html><b>Alpha:</b></html>", 48, 16));
		sliderPanel.add(new ColourSlider(Channel.Alpha, this));
		
		alphaPanel.add(sliderPanel);
		alphaPanel.add(fieldPanel);
		
		settings = new JButton("Settings");
		
		left = new JPanel();
		left.setLayout(new BorderLayout());
		left.add(indicator, BorderLayout.NORTH);
		left.add(settings, BorderLayout.CENTER);
		left.add(modePanel, BorderLayout.SOUTH);
		
		top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(left);
		top.add(wheel);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(top);
		bottom.add(palletPanel);
		
		sliders = new JPanel();
		sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
		sliders.add(rgbPanel);
		sliders.add(hsvPanel);
		sliders.add(alphaPanel);
		
		JPanel all = new JPanel();
		
		all.add(bottom);
		all.add(sliders);
		
		add(all);
		
		indicator.setAlignmentX(LEFT_ALIGNMENT);
		indicator.setAlignmentY(TOP_ALIGNMENT);
		
		broadcastChanges(null);
		
		this.mode = 2;
		this.switchVisible();
		
		pack();
		setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}
	
	public void toggle()
	{
		this.setVisible(!this.isVisible());
	}
	
	/*
	public static void main(String[] a) throws IOException
	{
		new ColourChooser();
	}
	*/
	
	@Override
	public void addColorListener(ColourListener c)
	{
		listeners.add(c);
	}
	
	@Override
	public void removeColorListener(ColourListener c)
	{
		listeners.remove(c);
	}
	
	@Override
	public void broadcastChanges(ColourListener source)
	{
		for(ColourListener l : listeners)
		{
			if(l.equals(source))
				continue;
			l.changeColor(primary[0], primary[1], primary[2], primary[3], primary[4], primary[5], primary[6], true);
			l.changeColor(secondary[0], secondary[1], secondary[2], secondary[3], secondary[4], secondary[5], secondary[6], false);
		}
		
		getContentPane().repaint();
	}
	
	public int red(boolean primary)
	{
		return (primary ? this.primary : this.secondary)[Channel.Red.ordinal()];
	}
	
	public int green(boolean primary)
	{
		return (primary ? this.primary : this.secondary)[Channel.Green.ordinal()];
	}
	
	public int blue(boolean primary)
	{
		return (primary ? this.primary : this.secondary)[Channel.Blue.ordinal()];
	}
	
	public int hue(boolean primary)
	{
		return (primary ? this.primary : this.secondary)[Channel.Hue.ordinal()];
	}
	
	public int sat(boolean primary)
	{
		return (primary ? this.primary : this.secondary)[Channel.Saturation.ordinal()];
	}
	
	public int val(boolean primary)
	{
		return (primary ? this.primary : this.secondary)[Channel.Value.ordinal()];
	}
	
	public boolean showMore()
	{
		if(this.mode < MODE_COUNT)
		{
			mode++;
			switchVisible();
		}
		return mode != MODE_COUNT;
	}
	
	public boolean showLess()
	{
		if(this.mode > 0)
		{
			mode--;
			switchVisible();
		}
		return mode != 0;
	}
	
	public void switchVisible()
	{
		wheel.setVisible(this.mode > 1);
		rgbPanel.setVisible(this.mode > 0);
		hsvPanel.setVisible(this.mode > 1);
		alphaPanel.setVisible(this.mode > 3);
		settings.setVisible(this.mode > 1);
		
		if(this.mode > 3)
		{
			palletPanel.setMode(PaletteMode.Extended);
		}
		else if(this.mode > 1)
		{
			palletPanel.setMode(PaletteMode.Standard);
		}
		else
		{
			palletPanel.setMode(PaletteMode.Minimized);
		}
		
		if(this.mode != 1)
		{
			sliders.add(rgbPanel, 0);
			if(top.getComponent(top.getComponentCount() - 1) instanceof Spacer)
				top.remove(top.getComponentCount() - 1);
		}
		else
		{
			top.add(new Spacer(8, 0));
			top.add(rgbPanel);
		}
		
		if(this.mode > 0)
		{
			left.add(indicator, BorderLayout.NORTH);
			left.add(settings, BorderLayout.CENTER);
			left.add(modePanel, BorderLayout.SOUTH);
		}
		else
		{
			left.add(indicator, BorderLayout.WEST);
			left.add(settings, BorderLayout.CENTER);
			left.add(modePanel, BorderLayout.EAST);
		}
		
		rgbPanel.getComponent(1).setVisible(this.mode > 2);
		hsvPanel.getComponent(1).setVisible(this.mode > 2);
		alphaPanel.getComponent(1).setVisible(this.mode > 2);
		
		//this.revalidate();
		this.pack();
		this.setResizable(false);
	}
	
	@Override
	public void makeChange(Object source, Channel channel, int val, boolean selected)
	{
		int[] colour = selected ? this.primary : this.secondary;
		
		colour[channel.ordinal()] = val;
		
		int mode = channel.ordinal() / 3;
		
		if(mode == 0)
		{
			int r = red(selected);
			int g = green(selected);
			int b = blue(selected);
			if(!(r + g + b == 0))
			{
				long hsva = toHSVA(r / 255., g / 255., b / 255., 1);
				colour[Channel.Hue.ordinal()] = (int) ((hsva >> 32) & 0x3FF);
				colour[Channel.Saturation.ordinal()] = (int) ((hsva >> 16) & 0xFF);
				colour[Channel.Value.ordinal()] = (int) ((hsva >> 8) & 0xFF);
			}
			else
			{
				colour[Channel.Value.ordinal()] = 0;
			}
		}
		else if(mode == 1)
		{
			int argb = toARGB(hue(selected) / 1023., sat(selected) / 255., val(selected) / 255., 1);
			colour[Channel.Red.ordinal()] = (argb >> 16) & 0xFF;
			colour[Channel.Green.ordinal()] = (argb >> 8) & 0xFF;
			colour[Channel.Blue.ordinal()] = (argb >> 0) & 0xFF;
		}
		
		if(source == listeners.get(3 + Channel.Hue.ordinal()) && sat(selected) == 0)
		{
			
		}
		
		lastSource = source;
	}
	
	public void setColour(int colour, Object source, boolean primary)
	{
		this.makeChange(source, Channel.Blue, (colour) & 0xFF, primary);
		this.makeChange(source, Channel.Green, (colour >>= 8) & 0xFF, primary);
		this.makeChange(source, Channel.Red, (colour >>= 8) & 0xFF, primary);
		this.makeChange(source, Channel.Alpha, (colour >>= 8) & 0xFF, primary);
		this.broadcastChanges(null);
	}
}
