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

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.image.change.doc.MetadataChange;
import heroesgrave.paint.main.Paint;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;

import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;

public class LayerSettings
{
	private WebDialog dialog;
	private Layer layer;
	private WebTextField label;
	private WebComboBox blendMode;
	
	public LayerSettings(JFrame frame)
	{
		this.dialog = new WebDialog(frame, "Layer Settings");
		dialog.setSize(200, 120);
		dialog.setVisible(false);
		dialog.setResizable(false);
		dialog.setLayout(new GridLayout(0, 2));
		
		blendMode = new WebComboBox();
		
		addAllBlendModes();
		
		label = new WebTextField("");
		
		WebButton done = new WebButton("Done");
		
		done.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(!layer.getMetadata().get("name").equals(label.getText()))
					Paint.getDocument().addChange(new MetadataChange(layer, "name", label.getText()));
				if(blendMode.getSelectedItem() != null)
					if(!layer.getMetadata().get("blend").equals(blendMode.getSelectedItem()))
						Paint.getDocument().addChange(new MetadataChange(layer, "blend", (String) blendMode.getSelectedItem()));
				dialog.setVisible(false);
			}
		});
		
		dialog.add(new WebLabel("Layer Name:", WebLabel.CENTER));
		dialog.add(label);
		dialog.add(new WebLabel("Blend Mode:", WebLabel.CENTER));
		dialog.add(blendMode);
		dialog.add(new WebLabel());
		dialog.add(done);
		
		blendMode.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent arg0)
			{
				if(layer == null || blendMode.getSelectedItem() == null)
					return;
				if(!blendMode.getSelectedItem().equals(layer.getMetadata().get("blend")))
				{
					// TODO Preview?
				}
			}
		});
		
		label.setHorizontalAlignment(WebLabel.CENTER);
	}
	
	@SuppressWarnings("unchecked")
	public void addAllBlendModes()
	{
		blendMode.removeAllItems();
		for(String mode : BlendMode.getBlendModeNames())
			blendMode.addItem(mode);
	}
	
	public void updateIfVisible(Layer layer)
	{
		if(dialog.isVisible())
		{
			this.layer = layer;
			label.setText(layer.getMetadata().get("name"));
			blendMode.setSelectedItem(layer.getMetadata().get("blend"));
		}
	}
	
	public void showFor(Layer layer)
	{
		this.layer = layer;
		label.setText(layer.getMetadata().get("name"));
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(Paint.main.gui.frame);
		blendMode.setSelectedItem(layer.getMetadata().get("blend"));
	}
	
	public void dispose()
	{
		dialog.dispose();
	}
}
