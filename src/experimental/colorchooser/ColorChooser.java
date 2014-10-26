/*
 * Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
package experimental.colorchooser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import experimental.colorchooser.event.ColorEventBroadcaster;
import experimental.colorchooser.event.ColorListener;
import static experimental.colorchooser.ColorUtils.*;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorChooser extends JDialog implements ColorEventBroadcaster {
	
	private int r, g, b, a = 255, h, s, v;
	private int tr, tg, tb, ta, th, ts, tv;
	private Object lastSource;
	
	private List<ColorListener> listeners;
	
	private PalletPanel palletPanel;
	
	public ColorChooser() {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		listeners = new ArrayList<>();
		
		palletPanel = new PalletPanel(this, Pallet.defaultPallet());
		ColorWheel wheel = new ColorWheel(this);
		ColorIndicator indicator = new ColorIndicator(this);
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < Channel.values.length; i++) {
			sliderPanel.add(new ColorSlider(Channel.values[i], this));
			sliderPanel.add(new Spacer(0, (i == 2 || i == 5) ? 16 : 4));
		}
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(indicator);
		top.add(wheel);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(top);
		bottom.add(palletPanel);
		
		JPanel all = new JPanel();
		
		all.add(bottom);
		all.add(sliderPanel);
		
		add(all);
		
		indicator.setAlignmentX(LEFT_ALIGNMENT);
		indicator.setAlignmentY(TOP_ALIGNMENT);
		
		broadcastChanges(null);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Completely for testing
	 * @throws IOException 
	 */
	public static void main(String[] a) throws IOException {
		new ColorChooser();
	}
	
	@Override
	public void addColorListener(ColorListener c) {
		listeners.add(c);
	}
	
	@Override
	public void removeColorListener(ColorListener c) {
		listeners.remove(c);
	}
	
	@Override
	public void broadcastChanges(ColorListener source) {
		
		for (ColorListener l : listeners)
			l.changeColor(r, g, b, h, s, v, a);
		
		getContentPane().repaint();
	}
	
	@Override
	public void makeChange(Object source, Channel channel, int val) {
		
		switch (channel) {
			case Alpha:
				a = val;
				break;
			case Blue:
				b = val;
				break;
			case Green:
				g = val;
				break;
			case Red:
				r = val;
				break;
			case Hue:
				h = val;
				break;
			case Saturation:
				s = val;
				break;
			case Value:
				v = val;
				break;
		}
		
		int mode = channel.ordinal() / 3;
		
		if (mode == 0) {
			if (!(r == 0 && g == 0 && b == 0)) {
				long hsva = toHSVA(r / 255., g / 255., b / 255., 1);
				h = (int) ((hsva >> 32) & 0xFFF);
				s = (int) ((hsva >> 16) & 0xFF);
				v = (int) ((hsva >> 8) & 0xFF);
			} else {
				v = 0;
			}
		} else if (mode == 1) {
			int argb = toARGB(h / 1024., s / 255., v / 255., 1);
			r = (argb >> 16) & 0xFF;
			g = (argb >> 8) & 0xFF;
			b = (argb >> 0) & 0xFF;
		}
		
		if (source == listeners.get(3 + Channel.Hue.ordinal()) && s == 0) {
			
		}
		
		lastSource = source;
	}
}
