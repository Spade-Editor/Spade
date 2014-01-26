package heroesgrave.utils.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TxtFileFilter extends FileFilter {

	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory())
			return true;
		
		return pathname.getName().endsWith(".txt");
	}

	@Override
	public String getDescription() {
		return "TXT - Color Pallet File";
	}
	
}
