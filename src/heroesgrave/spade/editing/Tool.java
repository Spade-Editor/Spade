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
import heroesgrave.spade.image.change.IChange;
import heroesgrave.spade.main.Input;
import heroesgrave.spade.main.Spade;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.popup.PopupStyle;
import com.alee.managers.popup.WebPopup;

public abstract class Tool
{
	public final String name;
	protected final JComponent menu;
	
	public Tool(String name)
	{
		this.name = name;
		
		WebPopup menu = new WebPopup();
		
		menu.add(new WebMenuItem(name + " Settings"));
		menu.add(new WebSeparator());
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.setPopupStyle(PopupStyle.bordered);
		menu.setFocusable(false);
		this.menu = menu;
	}
	
	public WebPopup createOptions()
	{
		return (WebPopup) menu;
	}
	
	public abstract void onPressed(Layer layer, short x, short y, int button);
	
	public abstract void onReleased(Layer layer, short x, short y, int button);
	
	public abstract void whilePressed(Layer layer, short x, short y, int button);
	
	public void whileReleased(Layer layer, short x, short y, int button)
	{
		
	}
	
	public static void preview(IChange change)
	{
		Spade.getDocument().preview(change);
	}
	
	public static void applyPreview()
	{
		Spade.getDocument().applyPreview();
	}
	
	public static void repaint()
	{
		Spade.getDocument().repaint();
	}
	
	public static int getColour(int button)
	{
		return Spade.main.getColor(button);
	}
	
	public static boolean isShiftDown()
	{
		return Input.SHIFT;
	}
	
	public static boolean isCtrlDown()
	{
		return Input.CTRL;
	}
	
	public static boolean isAltDown()
	{
		return Input.ALT;
	}
}
