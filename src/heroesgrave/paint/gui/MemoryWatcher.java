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

import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.misc.StringUtil;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * This class is a simple MemoryWatcher.<br>
 * It consists solely of a ProgressBar that displays how much memory is used by the Application.
 **/
public class MemoryWatcher extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8135215618742759998L;
	
	/**
	 * The Memory-Watcher Thread Instance
	 **/
	private final Thread THIS;
	
	/**
	 * The Runtime object used to fetch the Memory-State.
	 **/
	private final Runtime RUNTIME;
	
	/**
	 * The JProgressBar that is used to display the Memory-State
	 **/
	private final JProgressBar PB;
	
	public MemoryWatcher()
	{
		// Create the progressbar that is to be used as memory state display.
		PB = new JProgressBar(0, 100);
		PB.setStringPainted(true);
		PB.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				System.gc();
			}
		});
		PB.setToolTipText("<html><body><b>Memory Display</b><br>Display's the state of the memory in the Application.<br>Click to run the garbage-Collector!</body></html>");
		
		// Add Progressbar to this JPanel
		this.add(PB, BorderLayout.CENTER);
		
		// Create the Memory-Watcher Thread
		THIS = new Thread(this);
		THIS.setDaemon(true);
		THIS.setName("MemoryWatcherThread");
		
		// Get the Runtime object.
		RUNTIME = Runtime.getRuntime();
		
		// Start the watcher-thread!
		THIS.start();
	}
	
	@Override
	public void run()
	{
		
		while(!Thread.interrupted())
		{
			// get info
			long total = RUNTIME.totalMemory();
			long free = RUNTIME.freeMemory();
			
			// 0..max
			long used = total - free;
			
			// -1..+1 (We don't care about rounding errors BTW)
			double usedD = ((double) used / (double) total) * 100D;
			
			// Clamp and set the value, and the text of the progressbar, to reflect the state of the memory usage.
			PB.setValue(MathUtils.clamp((int) usedD, 100, 0));
			PB.setString(StringUtil.humanReadableByteCount(used, true) + "  of  " + StringUtil.humanReadableByteCount(total, true));
			
			try
			{
				// Sleep for a bit!
				Thread.sleep(500);
			}
			catch(InterruptedException e)
			{
				// We don't care about this, really!
				// Just set to intermediate-mode to signal that something is wrong.
				PB.setIndeterminate(true);
			}
		}
		
	}
	
}