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
import heroesgrave.spade.main.Spade;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public abstract class Effect
{
	public final String name;
	protected ImageIcon image;
	
	public Effect(String name)
	{
		this.name = name;
		loadIcon();
	}
	
	public Effect(String name, boolean loadIcon)
	{
		this.name = name;
		if(loadIcon)
			loadIcon();
	}
	
	public abstract void perform(Layer layer);
	
	public ImageIcon getIcon()
	{
		return image;
	}
	
	protected void loadIcon()
	{
		try
		{
			URL url = this.getClass().getResource("/res/icons/effects/" + name + ".png");
			if(this.image == null)
			{
				if(url != null)
				{
					this.image = new ImageIcon(ImageIO.read(url));
				}
				else
				{
					this.image = new ImageIcon(ImageIO.read(Spade.questionMarkURL));
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
