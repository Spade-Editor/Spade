package heroesgrave.utils.io.exporters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import heroesgrave.utils.io.ImageExporter;

/**
 * TGA Exporter.
 * 
 * @author Longor1996 & /INTERNET/
 **/
public class ExporterTGA extends ImageExporter {
	@Override
	public String getFileExtension() {
		return "tga";
	}

	@Override
	public void exportImage(BufferedImage image, File destination)
			throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(destination));
		boolean writeAlpha = image.getTransparency() != BufferedImage.OPAQUE;

		// ID Length
		out.writeByte((byte) 0);

		// Color Map
		out.writeByte((byte) 0);

		// Image Type
		out.writeByte((byte) 2);

		// Color Map - Ignored
		out.writeShort(flipEndian((short) 0));
		out.writeShort(flipEndian((short) 0));
		out.writeByte((byte) 0);

		// X, Y Offset
		out.writeShort(flipEndian((short) 0));
		out.writeShort(flipEndian((short) 0));

		// Width, Height, Depth
		out.writeShort(flipEndian((short) image.getWidth()));
		out.writeShort(flipEndian((short) image.getHeight()));

		if (writeAlpha) {
			out.writeByte((byte) 32);
			// Image Descriptor (can't be 0 since we're using 32-bit TGAs)
			// needs to not have 0x20 set to indicate it's not a flipped image
			out.writeByte((byte) 1);
		} else {
			out.writeByte((byte) 24);
			// Image Descriptor (must be 0 since we're using 24-bit TGAs)
			// needs to not have 0x20 set to indicate it's not a flipped image
			out.writeByte((byte) 0);
		}
		
		// Write out the image data
		Color c;
		
		for (int y = image.getHeight()-1; y >= 0; y--) {
			for (int x = 0; x < image.getWidth(); x++) {
				c = new Color(image.getRGB(x, y));
				
				out.writeByte((byte) (c.getBlue()));
				out.writeByte((byte) (c.getGreen()));
				out.writeByte((byte) (c.getRed()));
				
				if (writeAlpha) {
					out.writeByte((byte) (c.getAlpha()));
				}
			}
		}
		
		out.close();
	}

	private short flipEndian(short signedShort) {
		int input = signedShort & 0xFFFF;
		return (short) (input << 8 | (input & 0xFF00) >>> 8);
	}

	@Override
	public String getFileExtensionDescription() {
		return "TGA - Tagged Image File Format";
	}
}
