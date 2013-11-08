package heroesgrave.paint.effects;

import heroesgrave.paint.imageops.ImageChange;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class SimpleEdgeDetect extends ImageOp {

	@Override
	public void operation() {
		BufferedImage source = Paint.main.gui.canvas.getImage();
		BufferedImage dest = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
	    float data[] = { 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f };
	    
	    Kernel kernel = new Kernel(3, 3, data);
	    ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    convolve.filter(source, dest);
		
		Paint.addChange(new ImageChange(dest));
	}

}
