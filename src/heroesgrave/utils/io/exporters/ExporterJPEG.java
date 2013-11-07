package heroesgrave.utils.io.exporters;

import heroesgrave.utils.io.ImageExporter;
import heroesgrave.utils.io.ImageLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * JPEG Exporter.<br>
 * This exporter was made, because there was some kind of 'corruption' effect going on,
 * if the image had transparency. This special exporter fixes the corruption issue.
 * 
 * @author Longor1996
 **/
public class ExporterJPEG extends ImageExporter {
	@Override
	public String getFileExtension() {
		return "jpeg";
	}

	@Override
	public void exportImage(BufferedImage imageIn, File destination) {
		// 'Color Corruption' Fix
		BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = imageOut.getGraphics();
		g.drawImage(imageIn, 0, 0, null);
		
		ImageLoader.writeImage(imageOut, getFileExtension().toUpperCase(), destination.getAbsolutePath());
	}

	@Override
	public String getFileExtensionDescription() {
		return "JPEG - JPEG File Interchange Format";
	}

}
