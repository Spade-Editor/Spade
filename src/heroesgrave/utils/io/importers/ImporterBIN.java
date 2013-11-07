package heroesgrave.utils.io.importers;

import heroesgrave.paint.gui.SimpleModalProgressDialog;
import heroesgrave.utils.io.ImageImporter;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Longor1996
 **/
public class ImporterBIN extends ImageImporter {

	@Override
	public BufferedImage read(File file) throws IOException {
		
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		int width = in.readInt();
		int height = in.readInt();
		int surfaceArea = width * height;
		int[] raw = new int[surfaceArea];
		
		SimpleModalProgressDialog DIALOG = new SimpleModalProgressDialog("Working!", "Loading Image...", surfaceArea);
	    
		for(int I = 0; I < surfaceArea; I++)
		{
			// get
			int pixel = in.readInt();
			
			// convert
			int R = (pixel >> 24) & 0xff;
			int G = (pixel >> 16) & 0xff;
			int B = (pixel >> 8) & 0xff;
			int A = (pixel) & 0xff;
			
			pixel = 0;
			pixel |= B;
			pixel |= G << 8;
			pixel |= R << 16;
			pixel |= A << 24;
			
			// put
			raw[I] = pixel;
			
			// Don't update the progress-bar for every value, since that can cause some serious slowdown!
			if(I % 128 == 0)
				DIALOG.setValue(I);
		}
		
		// set progress to 100
		DIALOG.setValue(surfaceArea-1);
		
		// close progress dialog
		DIALOG.close();
		
		// Read 'EOID'
		in.readInt();
		
		in.close();
		
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
		image.setRGB(0, 0, width, height, raw, 0, width);
		
		return image;
	}

	@Override
	public String getFormat() {
		return "bin";
	}

	@Override
	public String getDescription() {
		return "BIN - Raw Binary Image Data Format";
	}

}
