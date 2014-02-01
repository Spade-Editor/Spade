/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * A simple, modal, Progress-Dialog, that is tied to the applications main-frame.<br>
 * Usage:
 * 
 * <pre>
 * SimpleModalProgressDialog dialog = new SimpleModalProgressDialog(...);
 * 
 * ... do stuff, and call setText, setValue to tell the user that something is going on ...
 * 
 * dialog.close();
 * </pre>
 * 
 * It is THAT simple!<br><br>
 * 
 * @author Longor1996
 **/
public class SimpleModalProgressDialog implements AutoCloseable
{
	// ALL FIELDS ARE PRIVATE, because there are nice methods, and you should use them Sir!
	private final JDialog dlg;
	private final JProgressBar dpb;
	private final JLabel dpm;
	private final int maxValue;
	private boolean closed = false;
	
	/**
	 * Constructor
	 **/
	public SimpleModalProgressDialog(String title, String message, int maxValue)
	{
		
		// Create/Assign variables.
		this.maxValue = maxValue;
		this.dlg = new JDialog(Paint.main.gui.frame, title, true);
		this.dpb = new JProgressBar(0, maxValue);
		this.dpm = new JLabel(message);
		
		// set text
		dpb.setString("Working...");
		dpb.setStringPainted(true);
		
		// borders and stuff
		dpb.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		dpm.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		// Create Dialog
		dlg.add(BorderLayout.CENTER, dpb);
		dlg.add(BorderLayout.NORTH, dpm);
		dlg.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dlg.setSize(400, 80);
		dlg.setLocationRelativeTo(Paint.main.gui.frame);
		
		// Create a new thread, so the call of setVisible(true) doesn't block the Thread that called this constructor.
		// + Make the new Thread a daemon, so the application can be closed while the dialog is still opened.
		// + Make the new Thread run on the lowest priority, because the Thread will sleep forever, until the dialog is closed.
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
				dlg.setVisible(true);
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Closes this dialog and cleans up all the resources.<br>
	 * The dialog cannot be opened again after being closed!
	 **/
	public void close()
	{
		if(closed)
			throw new RuntimeException("Dialog was already closed!");
		
		dlg.setVisible(false);
		closed = true;
	}
	
	/**
	 * Sets the value of the progress-bar.<br>
	 * The value must be in between 0 and the value given at creation, as in:<br>
	 * <pre>0 <= X < maxValue</pre>
	 **/
	public void setValue(int value)
	{
		if(value < 0)
			throw new IllegalArgumentException("'value' cannot be smaller than 0!");
		
		if(value >= maxValue)
			throw new IllegalArgumentException("'value' cannot be bigger or equal than 'maxValue'!");
		
		dpb.setValue(value);
	}
	
	/**
	 * Sets the message of the dialog.
	 * 
	 * @param text The new message to be displayed. Cannot be null.
	 **/
	public void setMessage(String text)
	{
		if(text == null)
			throw new IllegalArgumentException("'text' cannot be null!");
		
		dpm.setText(text);
	}
	
}