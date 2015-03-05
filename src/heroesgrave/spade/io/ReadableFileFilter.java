package heroesgrave.spade.io;

import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class ReadableFileFilter extends FileFilter
{

	@Override
	public boolean accept(File f)
	{
		String filename = f.getName();
		int i = filename.lastIndexOf(".");
		if (i > -1)
		{
    		String extension = filename.substring(i+1).toLowerCase();
    		return (!f.isDirectory() && 
    				((Arrays.asList(lower(ImageIO.getReaderFormatNames())).contains(extension)) || 
    						ImageImporter.get(extension) != null));
		} else return false;
	}

	@Override
	public String getDescription()
	{
		return "All supported file formats";
	}
	
	public String[] lower(String[] original)
	{
		String[] newArr = new String[original.length];
		for (int i = 0; i < original.length; i++)
			newArr[i] = original[i].toLowerCase();
		
		return newArr;
	}
}
