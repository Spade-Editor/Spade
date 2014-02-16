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

import heroesgrave.paint.main.Paint;

import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * Uses a system similar to keyframes in animations/videos to store the history.
 * 
 * All changes are stored in a type of frame.
 * Big changes like image operations are stored in keyframes.
 * 
 * When the image is changed, the image is taken from a keyframe and changed through each normal frame to get the final result
 * 
 * @author HeroesGrave
 *
 */
public class History
{
	/**
	 * Maximum frames between keyframes.
	 * 
	 * Higher = Less keyframes, so less RAM but more CPU.
	 * Lower = More keyframes, so more RAM but less CPU.
	 */
	public static final int MAX_FRAMES = 32;
	
	private Stack<IFrame> history = new Stack<IFrame>();
	private Stack<IFrame> reverted = new Stack<IFrame>();
	private int lastKeyFrame = 0;
	private boolean changed;
	
	public History(BufferedImage image)
	{
		addChange(new KeyFrame(image));
	}
	
	public void clear()
	{
		KeyFrame first = (KeyFrame) history.get(0);
		KeyFrame last = new KeyFrame(getUpdatedImage());
		history.clear();
		reverted.clear();
		history.push(first);
		history.push(last);
		changed = true;
		lastKeyFrame = 0;
	}
	
	public void addChange(IFrame frame)
	{
		Paint.main.saved = false;
		if(frame instanceof KeyFrame)
		{
			history.add(frame);
			lastKeyFrame = 0;
			changed = true;
			reverted.clear();
		}
		else if(lastKeyFrame++ > MAX_FRAMES)
		{
			createKeyFrame((Frame) frame);
		}
		else
		{
			history.add(frame);
			changed = true;
			reverted.clear();
		}
	}
	
	public void revertChange()
	{
		if(history.size() == 1)
			return;
		Paint.main.saved = false;
		reverted.push(history.pop());
		lastKeyFrame--;
		if(lastKeyFrame == 0)
		{
			lastKeyFrame = MAX_FRAMES;
		}
		changed = true;
	}
	
	public void repeatChange()
	{
		if(reverted.isEmpty())
			return;
		Paint.main.saved = false;
		IFrame frame = reverted.pop();
		if(frame instanceof KeyFrame)
		{
			history.push(frame);
			lastKeyFrame = 0;
			changed = true;
		}
		else
		{
			lastKeyFrame++;
			history.push(frame);
			changed = true;
		}
	}
	
	public void clean()
	{
		reverted.clear();
	}
	
	private void createKeyFrame(Frame frame)
	{
		BufferedImage image = getUpdatedImage();
		frame.apply(image);
		addChange(new KeyFrame(image));
	}
	
	public boolean wasChanged()
	{
		if(changed)
		{
			changed = false;
			return true;
		}
		return false;
	}
	
	public BufferedImage getUpdatedImage()
	{
		Stack<IFrame> rev = new Stack<IFrame>();
		while(!history.empty())
		{
			if(history.peek() instanceof KeyFrame)
			{
				break;
			}
			else
			{
				rev.push(history.pop());
			}
		}
		KeyFrame key = (KeyFrame) history.peek();
		BufferedImage image = key.takeImage();
		while(!rev.isEmpty())
		{
			Frame f = (Frame) rev.pop();
			f.apply(image);
			history.push(f);
		}
		return image;
	}
}