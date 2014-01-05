package heroesgrave.paint.effects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

public class AffineTransformEffect extends ImageOp {
	
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
