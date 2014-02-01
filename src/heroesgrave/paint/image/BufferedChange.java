/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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

package heroesgrave.paint.image;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BufferedChange extends Frame
{
	public LinkedList<Frame> changes = new LinkedList<Frame>();
	
	public void buffer(Frame frame)
	{
		changes.add(frame);
	}
	
	public void apply(BufferedImage image)
	{
		for(Frame c : changes)
		{
			c.apply(image);
		}
	}
}