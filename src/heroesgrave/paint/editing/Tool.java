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

package heroesgrave.paint.editing;

import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.IChange;
import heroesgrave.paint.main.Input;
import heroesgrave.paint.main.Paint;

import javax.swing.BoxLayout;

import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.separator.WebSeparator;
import com.alee.managers.popup.PopupStyle;
import com.alee.managers.popup.WebPopup;

public abstract class Tool
{
	public final String name;
	protected final WebPopup menu;
	
	public Tool(String name)
	{
		this.name = name;
		
		menu = new WebPopup();
		menu.add(new WebMenuItem(name + " Settings"));
		menu.add(new WebSeparator());
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.setPopupStyle(PopupStyle.light);
	}
	
	public WebPopup createOptions()
	{
		return menu;
	}
	
	public abstract void onPressed(Layer layer, short x, short y, int button);
	
	public abstract void onReleased(Layer layer, short x, short y, int button);
	
	public abstract void whilePressed(Layer layer, short x, short y, int button);
	
	public void whileReleased(Layer layer, short x, short y, int button)
	{
		
	}
	
	public static void preview(IChange change)
	{
		Paint.getDocument().preview(change);
	}
	
	public static void applyPreview()
	{
		Paint.getDocument().applyPreview();
	}
	
	public static void repaint()
	{
		Paint.getDocument().repaint();
	}
	
	public static int getColour(int button)
	{
		return Paint.main.getColor(button);
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
