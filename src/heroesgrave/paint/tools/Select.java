// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.tools;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage.MaskMode;
import heroesgrave.paint.image.change.edit.MaskRectChange;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;

public class Select extends Tool
{
	private MaskRectChange rect;
	
	public Select(String name)
	{
		super(name);
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		MaskMode mode;
		if(Input.CTRL)
		{
			if(Input.ALT)
				mode = MaskMode.XOR;
			else
				mode = MaskMode.ADD;
		}
		else if(Input.ALT)
		{
			mode = MaskMode.SUB;
		}
		else if(Input.SHIFT)
		{
			mode = MaskMode.AND;
		}
		else
		{
			mode = MaskMode.REP;
		}
		rect = new MaskRectChange(x, y, x, y, mode);
		Paint.getDocument().preview(rect);
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		Paint.getDocument().applyPreview();
		rect = null;
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(rect.moveTo(x, y))
			Paint.getDocument().repaint();
	}
}
