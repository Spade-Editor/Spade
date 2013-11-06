/*
 *	Copyright 2013 HeroesGrave & Longor1996
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.utils.io;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.filechooser.FileFilter;

public abstract class ImageImporter extends FileFilter {

	/**
	 * Reads an Image.
	 **/
	public abstract BufferedImage read(File file);

	public abstract String getFormat();

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		if(f.getAbsolutePath().endsWith("." + getFormat()))
			return true;
		
		return false;
	}

	@Override
	public abstract String getDescription();
	
}
