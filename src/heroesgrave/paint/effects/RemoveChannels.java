package heroesgrave.paint.effects;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import heroesgrave.paint.gui.Menu;
import heroesgrave.paint.gui.SimpleImageOpDialog;
import heroesgrave.paint.imageops.ImageChange;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

public class RemoveChannels extends ImageOp {

	@Override
	public void operation() {
		
		// create dialog
		final SimpleImageOpDialog dialog = new SimpleImageOpDialog("Channel Remover", new GridLayout(0,2));
		
		final JCheckBox channelR = new JCheckBox();
		final JCheckBox channelG = new JCheckBox();
		final JCheckBox channelB = new JCheckBox();
		final JCheckBox channelA = new JCheckBox();
		
		JButton create = new JButton("Remove Channels");
		JButton cancel = new JButton("Cancel");

		// create actions
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				operation_do(channelR.isSelected(),channelG.isSelected(),channelB.isSelected(),channelA.isSelected());
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
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

	protected void operation_do(boolean R, boolean G, boolean B, boolean A) {
		
		// MASK = AA.RR.GG.BB
		int AND_MASK = 0x00000000;
		int OR_MASK = 0x00000000;
		
		// If the Red channel is NOT to be removed, OR it into the MASK.
		if(!R)
			AND_MASK |= 0x00FF0000;
		
		// If the Green channel is NOT to be removed, OR it into the MASK.
		if(!G)
			AND_MASK |= 0x0000FF00;
		// If the Blue channel is NOT to be removed, OR it into the MASK.
		if(!B)
			AND_MASK |= 0x000000FF;
		// If the Alpha channel is NOT to be removed, OR it into the MASK.
		if(!A)
			AND_MASK |= 0xFF000000;
		else
			OR_MASK |= 0xFF000000;
		
		// image var's
		BufferedImage old = Paint.main.gui.canvas.getImage();
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
		
		Paint.addChange(new ImageChange(newImage));	
		
	}

}
