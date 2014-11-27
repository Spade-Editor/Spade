// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
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
import heroesgrave.spade.image.change.SingleChange;
import heroesgrave.spade.main.Spade;
import heroesgrave.spade.plugin.Plugin;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class SimpleEffect extends Effect
{
	private SingleChange change;
	private Class<? extends Plugin> class_;
	
	public SimpleEffect(Class<? extends Plugin> class_, String name, SingleChange change)
	{
		super(name, false);
		this.class_ = class_;
		this.change = change;
		this.loadIcon();
	}
	
	@Override
	public void perform(Layer layer)
	{
		layer.addChange(change);
	}
	
	@Override
	protected void loadIcon()
	{
		try
		{
			URL url = this.class_.getResource("/res/icons/effects/" + name + ".png");
			if(super.image == null)
			{
				if(url != null)
				{
					super.image = new ImageIcon(ImageIO.read(url));
				}
				else
				{
					super.image = new ImageIcon(ImageIO.read(Spade.questionMarkURL));
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
