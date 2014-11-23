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

package heroesgrave.paint.main;

import heroesgrave.paint.gui.LayerManager;
import heroesgrave.paint.gui.Menu;
import heroesgrave.paint.gui.colorchooser.ColourChooser;

import java.awt.Frame;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

public class UserPreferences
{
	private static final String WINDOW_WIDTH = "window.width";
	private static final String WINDOW_HEIGHT = "window.height";
	private static final String WINDOW_MAXIMIZED = "window.maximized";
	private static final String COLOUR_PICKER_X = "colour.x";
	private static final String COLOUR_PICKER_Y = "colour.y";
	private static final String COLOUR_PICKER_VISIBLE = "colour.visible";
	private static final String LAYERS_X = "layers.x";
	private static final String LAYERS_Y = "layers.y";
	private static final String LAYERS_WIDTH = "layers.width";
	private static final String LAYERS_HEIGHT = "layers.height";
	private static final String LAYERS_VISIBLE = "layers.visible";
	private static final String BACKGROUND_DARK = "background.dark";
	
	private static int windowWidth, windowHeight;
	private static int layersX, layersY, layersWidth, layersHeight;
	private static int colourPickerX, colourPickerY;
	
	public static void loadPrefs(JFrame frame, ColourChooser chooser, LayerManager layers)
	{
		Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);
		
		if(prefs.getBoolean(WINDOW_MAXIMIZED, false))
		{
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		windowWidth = prefs.getInt(WINDOW_WIDTH, 800);
		windowHeight = prefs.getInt(WINDOW_HEIGHT, 600);
		colourPickerX = prefs.getInt(COLOUR_PICKER_X, 0);
		colourPickerY = prefs.getInt(COLOUR_PICKER_Y, 0);
		layersX = prefs.getInt(LAYERS_X, 0);
		layersY = prefs.getInt(LAYERS_Y, 0);
		layersWidth = prefs.getInt(LAYERS_WIDTH, 200);
		layersHeight = prefs.getInt(LAYERS_HEIGHT, 300);
		
		frame.setSize(windowWidth, windowHeight);
		frame.setLocationRelativeTo(null);
		
		chooser.setLocation(colourPickerX, colourPickerY);
		if(prefs.getBoolean(COLOUR_PICKER_VISIBLE, false))
		{
			chooser.setVisible(true);
		}
		
		layers.dialog.setVisible(prefs.getBoolean(LAYERS_VISIBLE, false));
		layers.dialog.setBounds(layersX, layersY, layersWidth, layersHeight);
		
		Menu.DARK_BACKGROUND = prefs.getBoolean(BACKGROUND_DARK, false);
	}
	
	public static void savePrefs(JFrame frame, ColourChooser chooser, LayerManager layers)
	{
		Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);
		
		if((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)
		{
			prefs.putBoolean(WINDOW_MAXIMIZED, true);
		}
		else
		{
			windowWidth = frame.getWidth();
			windowHeight = frame.getHeight();
			prefs.putInt(WINDOW_WIDTH, windowWidth);
			prefs.putInt(WINDOW_HEIGHT, windowHeight);
			prefs.putBoolean(WINDOW_MAXIMIZED, false);
		}
		colourPickerX = chooser.getX();
		colourPickerY = chooser.getY();
		prefs.putInt(COLOUR_PICKER_X, colourPickerX);
		prefs.putInt(COLOUR_PICKER_Y, colourPickerY);
		prefs.putBoolean(COLOUR_PICKER_VISIBLE, chooser.isVisible());
		
		layersX = layers.dialog.getX();
		layersY = layers.dialog.getY();
		layersWidth = layers.dialog.getWidth();
		layersHeight = layers.dialog.getHeight();
		prefs.putInt(LAYERS_X, layersX);
		prefs.putInt(LAYERS_Y, layersY);
		prefs.putInt(LAYERS_WIDTH, layersWidth);
		prefs.putInt(LAYERS_HEIGHT, layersHeight);
		prefs.putBoolean(LAYERS_VISIBLE, layers.isVisible());
		
		prefs.putBoolean(COLOUR_PICKER_VISIBLE, chooser.isVisible());
		
		prefs.putBoolean(BACKGROUND_DARK, Menu.DARK_BACKGROUND);
	}
}
