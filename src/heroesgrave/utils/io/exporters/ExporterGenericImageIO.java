package heroesgrave.utils.io.exporters;

import heroesgrave.utils.io.ImageExporter;
import heroesgrave.utils.io.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * GENERIC Exporter.
 * 
 * This exporter uses the ImageIO API to export images. It can only handle the registered formats of the JRE.
 * 
 * @author Longor1996 & /ORACLE/
 **/
public class ExporterGenericImageIO extends ImageExporter
{
	public final String format;
	public final String description;
	
	public ExporterGenericImageIO(String format, String description){
		this.format = format;
		this.description = description;
	}
	
	@Override public String getFileExtension() {
		return format;
	}
	
	@Override public void exportImage(BufferedImage image, File destination) {
		ImageLoader.writeImage(image, getFileExtension().toUpperCase(), destination.getAbsolutePath());
	}

	@Override
	public String getFileExtensionDescription() {
		return description;
	}
	
	
	
}
