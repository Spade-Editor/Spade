package heroesgrave.spade.io.exporters;

import java.io.File;
import java.io.IOException;

import heroesgrave.spade.image.Document;
import heroesgrave.spade.io.ImageImporter;

public class ImporterGenericImageIO extends ImageImporter
{
	public final String format;
	public final String description;
	
	public ImporterGenericImageIO(String format, String description)
	{
		this.format = format;
		this.description = description;
	}

	@Override
	public boolean load(File file, Document doc) throws IOException
	{
		return loadImage(file.getAbsolutePath(), doc);
	}

	@Override
	public String getFileExtension()
	{
		return format;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

}
