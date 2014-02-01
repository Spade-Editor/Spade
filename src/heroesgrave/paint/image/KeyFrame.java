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

public class KeyFrame implements IFrame
{
	private BufferedImage image;
	private Canvas canvas;
	
	public KeyFrame(BufferedImage image)
	{
		this.image = image;
	}
	
	/**
	 * Gets the image held by the keyframe.
	 * For most cases, you should be using {@link #takeImage()}
	 * 
	 * @return The image held by the keyframe
	 */
	public BufferedImage getImage()
	{
		return image;
	}
	
	/**
	 * Gets a copy of the image for use as a canvas
	 * 
	 * @return A copy of the image held by the keyframe
	 */
	public BufferedImage takeImage()
	{
		BufferedImage give = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		give.getGraphics().drawImage(image, 0, 0, null);
		return give;
	}
	
	@Override
	public void setCanvas(Canvas canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public Canvas getCanvas()
	{
		return canvas;
	}
}