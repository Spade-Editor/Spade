// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.editing;

import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.change.edit.DrawPathChange;
import heroesgrave.spade.image.change.edit.PathChange;

public class Pencil extends Tool
{
	private PathChange path;
	
	public Pencil(String name)
	{
		super(name);
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		path = new PathChange(x, y, getColour(button), new DrawPathChange());
		preview(path);
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		applyPreview();
		path = null;
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(path.moveTo(x, y))
			repaint();
	}
}
