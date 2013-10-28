/*
 *	Copyright 2013 HeroesGrave
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

import heroesgrave.paint.gui.Menu;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.tools.Tool;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

public class Input implements KeyListener, MouseWheelListener
{
	public static boolean CTRL, SHIFT, ALT;
	
	private static HashMap<Integer, String> keyCodeToChar = new HashMap<Integer, String>();
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
		{
			CTRL = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			SHIFT = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_ALT)
		{
			ALT = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F5)
		{
			Paint.main.gui.chooser.toggle();
		}
		
		if(e.isControlDown())
		{
			if(e.isShiftDown())
			{
				if(keyCodeToChar.containsKey(e.getKeyCode()))
				{
					ImageOp op = Paint.getImageOp(keyCodeToChar.get(e.getKeyCode()).toLowerCase());
					if(op != null)
					{
						op.operation();
					}
				}
			}
			else
			{
				if(e.getKeyCode() == KeyEvent.VK_Z)
				{
					Paint.main.gui.canvas.revertChange();
				}
				else if(e.getKeyCode() == KeyEvent.VK_Y)
				{
					Paint.main.gui.canvas.repeatChange();
				}
				else if(e.getKeyCode() == KeyEvent.VK_EQUALS)
				{
					Paint.main.gui.canvas.incZoom();
				}
				else if(e.getKeyCode() == KeyEvent.VK_MINUS)
				{
					Paint.main.gui.canvas.decZoom();
				}
				else if(e.getKeyCode() == KeyEvent.VK_S)
				{
					Paint.save();
				}
				else if(e.getKeyCode() == KeyEvent.VK_N)
				{
					Menu.showNewMenu();
				}
				else if(e.getKeyCode() == KeyEvent.VK_O)
				{
					Menu.showOpenMenu();
				}
				else if(e.getKeyCode() == KeyEvent.VK_G)
				{
					Menu.GRID_ENABLED = !Menu.GRID_ENABLED;
					Paint.main.gui.canvas.getCanvas().repaint();
				}
			}
		}
		else
		{
			Tool t = Paint.getTool("" + e.getKeyChar());
			if(t != null)
			{
				Paint.setTool(t);
			}
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
		{
			CTRL = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			SHIFT = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_ALT)
		{
			ALT = false;
		}
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(e.isControlDown())
		{
			if(e.getUnitsToScroll() > 0)
			{
				Paint.main.gui.canvas.decZoom();
			}
			else if(e.getUnitsToScroll() < 0)
			{
				Paint.main.gui.canvas.incZoom();
			}
		}
	}
	
	static
	{
		keyCodeToChar.put(KeyEvent.VK_A, "A");
		keyCodeToChar.put(KeyEvent.VK_B, "B");
		keyCodeToChar.put(KeyEvent.VK_C, "C");
		keyCodeToChar.put(KeyEvent.VK_D, "D");
		keyCodeToChar.put(KeyEvent.VK_E, "E");
		keyCodeToChar.put(KeyEvent.VK_F, "F");
		keyCodeToChar.put(KeyEvent.VK_G, "G");
		keyCodeToChar.put(KeyEvent.VK_H, "H");
		keyCodeToChar.put(KeyEvent.VK_I, "I");
		keyCodeToChar.put(KeyEvent.VK_J, "J");
		keyCodeToChar.put(KeyEvent.VK_K, "K");
		keyCodeToChar.put(KeyEvent.VK_L, "L");
		keyCodeToChar.put(KeyEvent.VK_M, "M");
		keyCodeToChar.put(KeyEvent.VK_N, "N");
		keyCodeToChar.put(KeyEvent.VK_O, "O");
		keyCodeToChar.put(KeyEvent.VK_P, "P");
		keyCodeToChar.put(KeyEvent.VK_Q, "Q");
		keyCodeToChar.put(KeyEvent.VK_R, "R");
		keyCodeToChar.put(KeyEvent.VK_S, "S");
		keyCodeToChar.put(KeyEvent.VK_T, "T");
		keyCodeToChar.put(KeyEvent.VK_U, "U");
		keyCodeToChar.put(KeyEvent.VK_V, "V");
		keyCodeToChar.put(KeyEvent.VK_W, "W");
		keyCodeToChar.put(KeyEvent.VK_X, "X");
		keyCodeToChar.put(KeyEvent.VK_Y, "Y");
		keyCodeToChar.put(KeyEvent.VK_Z, "Z");
	}
}