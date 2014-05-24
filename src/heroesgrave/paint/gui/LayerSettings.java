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

package heroesgrave.paint.gui;

public class LayerSettings
{
	/* FIXME
	private JDialog dialog;
	private Layer layer;
	private JTextField label;
	private JComboBox<BlendMode> blendMode;
	
	public LayerSettings()
	{
		this.dialog =
				new CentredJDialog(Paint.main.gui.frame, "Layer Settings");
		dialog.setSize(200, 120);
		dialog.setVisible(false);
		dialog.setResizable(false);
		dialog.setLayout(new GridLayout(0, 2));
		
		blendMode = new JComboBox<BlendMode>();
		
		HashSet<BlendMode> modes = BlendMode.getBlendModes();
		for(BlendMode mode : modes)
			blendMode.addItem(mode);
		
		label = new JTextField("");
		
		JButton done = new JButton("Done");
		
		done.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Paint.main.history.addChange(new LayerNameChange(canvas, label
						.getText()));
				dialog.setVisible(false);
			}
		});
		
		dialog.add(new CentredJLabel("Layer Name:"));
		dialog.add(label);
		dialog.add(new CentredJLabel("Blend Mode:"));
		dialog.add(blendMode);
		dialog.add(new JLabel());
		dialog.add(done);
		
		blendMode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(blendMode.getSelectedItem() != Paint.main.gui.canvas
						.getCanvas().mode)
				{
					Paint.main.history.addChange(new LayerBlendChange(canvas,
							(BlendMode) blendMode.getSelectedItem()));
					Paint.main.gui.canvas.getPanel().repaint();
				}
			}
		});
		
		label.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void updateIfVisible(Canvas canvas)
	{
		if(dialog.isVisible())
		{
			this.canvas = canvas;
			label.setText(canvas.name);
			blendMode.setSelectedItem(canvas.mode);
		}
	}
	
	public void showFor(Canvas canvas)
	{
		this.canvas = canvas;
		label.setText(canvas.name);
		dialog.setVisible(true);
		blendMode.setSelectedItem(canvas.mode);
	}
	
	public void dispose()
	{
		dialog.dispose();
	}
	*/
}
