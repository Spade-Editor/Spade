/*
 *	Copyright 2013 HeroesGrave & markbernard
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

package heroesgrave.paint.main;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

/**
 * 
 * @author markbernard
 *
 */
public class UserPreferences {
    
    private static final String WINDOW_X = "window.x";
    private static final String WINDOW_Y = "window.y";
    private static final String WINDOW_WIDTH = "window.width";
    private static final String WINDOW_HEIGHT = "window.height";
    private static final String WINDOW_MAXIMIZED = "window.maximized";
    
    private static int windowX;
    private static int windowY;
    private static int windowWidth;
    private static int windowHeight;

    /**
     * Setup the provided
     * 
     * @param frame
     */
    public static void loadPrefs(JFrame frame) {
        Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);
        
        if(prefs.getBoolean(WINDOW_MAXIMIZED, false)) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        windowX = prefs.getInt(WINDOW_X, 0);
        windowY = prefs.getInt(WINDOW_Y, 0);
        windowWidth = prefs.getInt(WINDOW_WIDTH, 800);
        windowHeight = prefs.getInt(WINDOW_HEIGHT, 600);
        frame.setBounds(windowX, windowY, windowWidth, windowHeight);
    }
    
    /**
     * Save user preferences.
     * 
     * @param frame
     */
    public static void savePrefs(JFrame frame) {
        Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);
        
        if((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            prefs.putBoolean(WINDOW_MAXIMIZED, true);
        }
        else {
            windowX = frame.getX();
            windowY = frame.getY();
            windowWidth = frame.getWidth();
            windowHeight = frame.getHeight();
            prefs.putInt(WINDOW_X, windowX);
            prefs.putInt(WINDOW_Y, windowY);
            prefs.putInt(WINDOW_WIDTH, windowWidth);
            prefs.putInt(WINDOW_HEIGHT, windowHeight);
            prefs.putBoolean(WINDOW_MAXIMIZED, false);
        }
    }
}