package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotateRightBy90 extends ImageOp
{
	public void operation()
	{
		BufferedImage old = Paint.main.gui.canvas.getImage();
		BufferedImage newImage = new BufferedImage(old.getHeight(), old.getWidth(), BufferedImage.TYPE_INT_ARGB);
		
		
		Graphics2D g2d = (Graphics2D) newImage.getGraphics();
		
        // create the transform, note that the transformations happen
        // in reversed order (so check them backwards)
        AffineTransform at = new AffineTransform();
        
        // 4. translate it to the center of the component ???
        at.translate(newImage.getWidth(), 0);
        
        // 3. do the actual rotation
        at.rotate(Math.toRadians(90));
        
        g2d.drawImage(old, at, null);
		
		Paint.addChange(new ImageChange(newImage));
	}
}
