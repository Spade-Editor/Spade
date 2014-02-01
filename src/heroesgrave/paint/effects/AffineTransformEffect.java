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

import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class AffineTransformEffect extends ImageOp
{
	
	@Override
	public void operation()
	{
		
		// TODO: Implement GUI for AffineTransform!
		// Use springLayout this time, as anything else looks stupid.
		
		// I don't know how the GUI should be done...
		// Either by sliders, a table of values (matrix) or
		// some clearly visible options (translation, shear, rotation, etc.etc.).
		
		// dialog:creation -> transform_do();
		// dialog:anyAction -> transform_do();
		// dialog:done -> transform_do_apply();
		// dialog:cancel -> transform_do_cancel();
		
	}
	
	/**
	 * The actual transformation of the image is VERY simple, and it is hardware accelerated by Java2D at default!
	 **/
	public void transform_do()
	{
		// Create an -identity- AffineTransform
		AffineTransform transform = new AffineTransform();
		
		// merge user selected transformation into the AffineTransform
		// TODO: AffineTransform
		
		// Old/New Images
		BufferedImage old = Paint.main.gui.canvas.getCanvas().getImage();
		BufferedImage newImage = new BufferedImage(old.getWidth(), old.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		// Do it!
		Graphics2D g = (Graphics2D) newImage.getGraphics();
		g.setTransform(transform);
		g.drawImage(old, 0, 0, null);
		
		// Add the change!
		Paint.main.gui.canvas.preview(new KeyFrame(newImage));
	}
	
	public void transform_do_apply()
	{
		Paint.main.gui.canvas.applyPreview();
	}
	
	public void transform_do_cancel()
	{
		Paint.main.gui.canvas.preview(null);
	}
	
}