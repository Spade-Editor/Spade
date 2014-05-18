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

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import experimental.colorchooser.event.ColorEventBroadcaster;
import experimental.colorchooser.event.ColorEvent;
import experimental.colorchooser.event.ColorListener;

/**
 * @author BurntPizza
 * 
 */
@SuppressWarnings("serial")
public class ColorChooser extends JDialog implements ColorEventBroadcaster {
	
	PalletPanel palletPanel;
	ColorWheel colorWheel;
	
	private List<ColorListener> listeners;
	
	public ColorChooser() {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		listeners = new ArrayList<>();
		
		PalletPanel pp = new PalletPanel(this, Pallet.defaultPallet());
		ColorWheel wheel = new ColorWheel(this);
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		
		for(int i=0;i<Channel.values.length;i++) {
			sliderPanel.add(new ColorSlider(Channel.values[i], this));
			sliderPanel.add(new Spacer(0,i==2?16:4));
		}
		
		JPanel pan = new JPanel();
		
		pan.add(pp);
		pan.add(wheel);
		pan.add(sliderPanel);
		
		add(pan);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Completely for testing
	 */
	public static void main(String[] a) {
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
	public void broadcastEvent(ColorEvent e) {
		for (ColorListener l : listeners)
			if (l != e.getSource())
				l.colorChanged(e);
	}
}
