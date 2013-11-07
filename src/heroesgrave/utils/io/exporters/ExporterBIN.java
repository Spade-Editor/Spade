package heroesgrave.utils.io.exporters;

import heroesgrave.utils.io.ImageExporter;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * BIN Image Exporter.
 * 
 * Format is as follows:
 * signed_int32 width
 * signed_int32 height
 * INT_RGBA[width*height] imageData
 * 
 * @author Longor1996
 **/
public class ExporterBIN extends ImageExporter
{
	
	@Override public String getFileExtension() {
		return "bin";
	}
	
	@Override public void exportImage(BufferedImage bufferedImage, File destination) throws IOException {
		DataOutputStream output = new DataOutputStream(new FileOutputStream(destination));
		
		// Get width and height.
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		// Write width and height as int32 (Signed 32-Bit Integer).
		output.writeInt(width);
		output.writeInt(height);
		
		// Buffer the Image-Data
		int ai[] = new int[width * height];
		byte abyte0[] = new byte[width * height * 4];
		bufferedImage.getRGB(0, 0, width, height, ai, 0, width);
		
		// Go trough ALL the pixels and convert from INT_ARGB to INT_RGBA
		for (int k = 0; k < ai.length; k++)
		{
			int A = (ai[k] >> 24) & 0xff;
			int R = (ai[k] >> 16) & 0xff;
			int G = (ai[k] >> 8) & 0xff;
			int B = ai[k] & 0xff;
			
			abyte0[(k * 4) + 0] = (byte) R;//R
			abyte0[(k * 4) + 1] = (byte) G;//G
			abyte0[(k * 4) + 2] = (byte) B;//B
			abyte0[(k * 4) + 3] = (byte) A;//A
		}
		
		// Write image as INT_RGBA
		output.write(abyte0);
		
		// Write End Sequence
		output.write('E');
		output.write('O');
		output.write('I');
		output.write('D');
		
		// Done!
		output.close();
	}
	
	@Override
	public String getFileExtensionDescription() {
		return "BIN - Raw Binary Image Data Format";
	}
}
