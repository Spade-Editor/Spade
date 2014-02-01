/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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

package heroesgrave.paint.effects;

import heroesgrave.paint.gui.Menu;
import heroesgrave.paint.gui.SimpleImageOpDialog;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class RemoveChannels extends ImageOp
{
	@Override
	public void operation()
	{
		// create dialog
		final SimpleImageOpDialog dialog = new SimpleImageOpDialog("Channel Filter", new GridLayout(0, 2));
		
		final JCheckBox channelR = new JCheckBox();
		final JCheckBox channelG = new JCheckBox();
		final JCheckBox channelB = new JCheckBox();
		final JCheckBox channelA = new JCheckBox();
		
		JButton create = new JButton("Remove Channels");
		JButton cancel = new JButton("Cancel");
		
		ActionListener preview = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				operation_do(channelR.isSelected(), channelG.isSelected(), channelB.isSelected(), channelA.isSelected());
			}
		};
		
		channelR.addActionListener(preview);
		channelG.addActionListener(preview);
		channelB.addActionListener(preview);
		channelA.addActionListener(preview);
		
		// create actions
		create.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				operation_do(channelR.isSelected(), channelG.isSelected(), channelB.isSelected(), channelA.isSelected());
				Paint.main.gui.canvas.applyPreview();
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Paint.main.gui.canvas.preview(null);
				dialog.close();
			}
		});
		
		dialog.add(new Menu.CentredJLabel("Red Channel"));
		dialog.add(channelR);
		dialog.add(new Menu.CentredJLabel("Green Channel"));
		dialog.add(channelG);
		dialog.add(new Menu.CentredJLabel("Blue Channel"));
		dialog.add(channelB);
		dialog.add(new Menu.CentredJLabel("Alpha Channel"));
		dialog.add(channelA);
		
		dialog.add(create);
		dialog.add(cancel);
		
		dialog.show();
	}
	
	protected void operation_do(boolean R, boolean G, boolean B, boolean A)
	{
		// MASK = AA.RR.GG.BB
		int AND_MASK = 0x00000000;
		int OR_MASK = 0x00000000;
		
		// If the Red channel is NOT to be removed, OR it into the MASK.
		if(!R)
		{
			AND_MASK |= 0x00FF0000;
		}
		
		// If the Green channel is NOT to be removed, OR it into the MASK.
		if(!G)
		{
			AND_MASK |= 0x0000FF00;
		}
		// If the Blue channel is NOT to be removed, OR it into the MASK.
		if(!B)
		{
			AND_MASK |= 0x000000FF;
		}
		// If the Alpha channel is NOT to be removed, OR it into the MASK.
		if(!A)
		{
			AND_MASK |= 0xFF000000;
		}
		else
		{
			OR_MASK |= 0xFF000000;
		}
		
		// image var's
		BufferedImage old = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		int ARGB;
		
		for(int i = 0; i < old.getWidth(); i++)
		{
			for(int j = 0; j < old.getHeight(); j++)
			{
				// get pixel
				ARGB = old.getRGB(i, j);
				
				// apply channel mask
				ARGB &= AND_MASK;
				ARGB |= OR_MASK;
				
				// put pixel back in
				newImage.setRGB(i, j, ARGB);
			}
		}
		
		Paint.main.gui.canvas.preview(new KeyFrame(newImage));
		Paint.main.gui.canvas.getPanel().repaint();
	}
	
}