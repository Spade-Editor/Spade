package heroesgrave.paint.gui.colourChooser;

import heroesgrave.paint.gui.Menu.CentredJDialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MultiColourChooser
{
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**                 INNER CLASSES                **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	// none
	
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**                 OBJECT FIELDS                **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	JDialog dialog;
	
	/**
	 * The 'left'-colour.
	 **/
	private int leftColour;
	
	/**
	 * The 'right'-colour.
	 **/
	private int rightColour;
	
	
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**               OBJECT CONSTRUCTOR             **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	public MultiColourChooser(JFrame mainFrame)
	{
		
		// Create the Dialog
		dialog = new CentredJDialog(mainFrame, "Colour-Chooser");
		
		// Do the typical configurations for it.
		dialog.setSize(400, 300);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		
	}
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**             STATE CHANGING METHODS           **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	/**
	 * This method makes the colour-chooser show up.
	 **/
	public void show()
	{
		// Check if the chooser is already visible.
		// We do this because we don't wan't to spend an awful lot of time on the Swing-side of Java internally changing states.
		if(!dialog.isVisible())
			dialog.setVisible(true);
		
	}
	
	/**
	 * This method makes the colour-chooser invisible again.
	 **/
	public void hide()
	{
		// Check if the chooser is already invisible.
		// We do this because we don't wan't to spend an awful lot of time on the Swing-side of Java internally changing states.
		if(dialog.isVisible())
			dialog.setVisible(false);
		
	}
	
	/**
	 * This method toggles the visibility of the colour-chooser.
	 **/
	public void toggle()
	{
		// If the dialog is visible, make it invisible.
		// If the dialog is invisible, make it visible.
		dialog.setVisible(!dialog.isVisible());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**              SETTERS AND GETTERS             **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	/**
	 * Changes the 'Left' (primary) colour to the given color.
	 * 
	 * @param packedColorARGB The colour to change to.
	 **/
	public void setLeftColour(int packedColorARGB)
	{
		leftColour = packedColorARGB;
	}
	
	/**
	 * Changes the 'Right' (secondary) colour to the given color.
	 * 
	 * @param packedColorARGB The colour to change to.
	 **/
	public void setRightColour(int packedColorARGB)
	{
		rightColour = packedColorARGB;
	}
	
	/**
	 * Returns the 'Left' (primary) colour.
	 * @return The 'Left' (primary) colour.
	 **/
	public int getLeftColour()
	{
		return leftColour;
	}
	
	/**
	 * Returns the 'Right' (secondary) colour.
	 * @return The 'Right' (secondary) colour.
	 **/
	public int getRightColour()
	{
		return rightColour;
	}
	
	/**
	 * Return's the actual JDialog instance that is the root for the components of the colour-chooser.<br><br>
	 * The colour-choosers JDialog should <b>never</b> be modified by another class except by itself.<br>
	 * Adding listeners to it is an exception, as they don't actually change the JDialog.<br>
	 * 
	 * @return The colour-choosers JDialog instance.
	 **/
	public JDialog getDialog()
	{
		return dialog;
	}
	
	/**
	 * @return <i>True</i>, if the chooser is currently visible. <i>False</i> if not.
	 **/
	public boolean isVisible()
	{
		return dialog.isVisible();
	}
	
}
