package heroesgrave.paint.main;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

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
