/*
 *	Copyright 2013 HeroesGrave
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

package heroesgrave.paint.imageops;

import heroesgrave.paint.main.Paint;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotateRightBy90 extends ImageOp
{
	public void operation()
	{
		
		Paint.addChange(new RotateCh());
	}
	
	private static class RotateCh extends ImageChange
	{
		public BufferedImage apply(BufferedImage image)
		{
			BufferedImage newImage = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2d = (Graphics2D) newImage.getGraphics();
			
			// create the transform, note that the transformations happen
			// in reversed order (so check them backwards)
			AffineTransform at = new AffineTransform();
			
			// 4. translate it to the center of the component ???
			at.translate(newImage.getWidth(), 0);
			
			// 3. do the actual rotation
			at.rotate(Math.toRadians(90));
			
			g2d.drawImage(image, at, null);
			g2d.dispose();
			
			return newImage;
		}
		
		public BufferedImage revert(BufferedImage image)
		{
			BufferedImage newImage = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2d = (Graphics2D) newImage.getGraphics();
			
			// create the transform, note that the transformations happen
			// in reversed order (so check them backwards)
			AffineTransform at = new AffineTransform();
			
			// 4. translate it to the center of the component ???
			at.translate(newImage.getWidth(), 0);
			
			// 3. do the actual rotation
			at.rotate(Math.toRadians(-90));
			
			g2d.drawImage(image, at, null);
			g2d.dispose();
			
			return newImage;
		}
		
		public int getSize()
		{
			return 1;
		}
	}
	
	/**
	 * More advanced method from Stackoverflow:

	public static BufferedImage rotate(BufferedImage image, double angle) {
	  double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	  int w = image.getWidth(), h = image.getHeight();
	  int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
	  
	  GraphicsConfiguration gc = getDefaultConfiguration();
	  BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
	  
	  Graphics2D g = result.createGraphics();
	  g.translate((neww-w)/2, (newh-h)/2);
	  g.rotate(angle, w/2, h/2);
	  g.drawRenderedImage(image, null);
	  g.dispose();
	  
	  return result;
	}

	 * 
	 **/
	
}