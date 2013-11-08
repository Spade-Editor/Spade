package heroesgrave.paint.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class PluginFileFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		
		if(file.isDirectory())
			return true;
		
		if(file.getName().endsWith(".jar"))
			return true;
		
		return false;
	}
	
}
