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

package heroesgrave.spade.main;

import heroesgrave.spade.gui.misc.ClipboardHandler;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.RawImage.MaskMode;
import heroesgrave.spade.image.change.doc.FloatLayer;
import heroesgrave.spade.image.change.doc.MergeLayer;
import heroesgrave.spade.image.change.doc.NewLayer;
import heroesgrave.spade.image.change.edit.ClearMaskChange;
import heroesgrave.spade.image.change.edit.FillImageChange;
import heroesgrave.spade.image.change.edit.FillMaskChange;
import heroesgrave.spade.image.change.edit.ResizeCanvasChange;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Input implements KeyListener
{
	public static boolean CTRL, SHIFT, ALT;
	public static Robot robot;
	
	private static HashMap<Integer, Character> keyCodeToChar = new HashMap<Integer, Character>();
	
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
		
		if(e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			Spade.getDocument().getCurrent().addChange(new FillImageChange(0x00000000));
		}
		
		int MOVE = 1;
		
		if(e.isControlDown())
		{
			MOVE = 8;
			if(!e.isShiftDown())
			{
				if(e.getKeyCode() == KeyEvent.VK_Q)
				{
					if(Spade.getDocument() != null)
					{
						Spade.closeDocument(Spade.getDocument());
					}
					else
					{
						Spade.close();
					}
				}
				else if(Spade.getDocument() != null)
				{
					if(e.getKeyCode() == KeyEvent.VK_D)
					{
						Spade.getDocument().getCurrent().addChange(new ClearMaskChange());
					}
					else if(e.getKeyCode() == KeyEvent.VK_A)
					{
						Spade.getDocument().getCurrent().addChange(new FillMaskChange(MaskMode.ADD));
					}
					else if(e.getKeyCode() == KeyEvent.VK_I)
					{
						Spade.getDocument().getCurrent().addChange(new FillMaskChange(MaskMode.XOR));
					}
					else if(e.getKeyCode() == KeyEvent.VK_F)
					{
						Document doc = Spade.getDocument();
						if(!doc.getCurrent().isFloating())
						{
							doc.addChange(new FloatLayer(Spade.getDocument().getCurrent(), e.isAltDown()));
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_M)
					{
						Layer current = Spade.getDocument().getCurrent();
						if(current.getChildCount() == 0)
							Spade.getDocument().addChange(new MergeLayer(current));
					}
					else if(e.getKeyCode() == KeyEvent.VK_C)
					{
						ClipboardHandler.setImage(Spade.getDocument().getCurrent().getImage());
					}
					else if(e.getKeyCode() == KeyEvent.VK_X)
					{
						ClipboardHandler.setImage(Spade.getDocument().getCurrent().getImage());
						Spade.getDocument().getCurrent().addChange(new FillImageChange(0x00000000));
					}
					else if(e.getKeyCode() == KeyEvent.VK_V)
					{
						Document doc = Spade.getDocument();
						RawImage image = ClipboardHandler.getImage();
						if(image != null)
						{
							if(doc.getCurrent().isFloating())
							{
								doc.addChange(new MergeLayer(doc.getCurrent()));
							}
							// Fun hack.
							if(image.width != doc.getWidth() || image.height != doc.getHeight())
							{
								image = new ResizeCanvasChange(doc.getWidth(), doc.getHeight()).apply(image);
							}
							doc.addChange(new NewLayer(doc.getCurrent(), image, "Floating Layer").floating());
						}
					}
				}
			}
		}
		Point p = MouseInfo.getPointerInfo().getLocation();
		
		MOVE = (int) (MOVE * Spade.main.gui.canvas.getScale());
		
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			robot.mouseMove(p.x, p.y - MOVE);
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			robot.mouseMove(p.x, p.y + MOVE);
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			robot.mouseMove(p.x - MOVE, p.y);
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			robot.mouseMove(p.x + MOVE, p.y);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
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
		else if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	static
	{
		keyCodeToChar.put(KeyEvent.VK_A, 'A');
		keyCodeToChar.put(KeyEvent.VK_B, 'B');
		keyCodeToChar.put(KeyEvent.VK_C, 'C');
		keyCodeToChar.put(KeyEvent.VK_D, 'D');
		keyCodeToChar.put(KeyEvent.VK_E, 'E');
		keyCodeToChar.put(KeyEvent.VK_F, 'F');
		keyCodeToChar.put(KeyEvent.VK_G, 'G');
		keyCodeToChar.put(KeyEvent.VK_H, 'H');
		keyCodeToChar.put(KeyEvent.VK_I, 'I');
		keyCodeToChar.put(KeyEvent.VK_J, 'J');
		keyCodeToChar.put(KeyEvent.VK_K, 'K');
		keyCodeToChar.put(KeyEvent.VK_L, 'L');
		keyCodeToChar.put(KeyEvent.VK_M, 'M');
		keyCodeToChar.put(KeyEvent.VK_N, 'N');
		keyCodeToChar.put(KeyEvent.VK_O, 'O');
		keyCodeToChar.put(KeyEvent.VK_P, 'P');
		keyCodeToChar.put(KeyEvent.VK_Q, 'Q');
		keyCodeToChar.put(KeyEvent.VK_R, 'R');
		keyCodeToChar.put(KeyEvent.VK_S, 'S');
		keyCodeToChar.put(KeyEvent.VK_T, 'T');
		keyCodeToChar.put(KeyEvent.VK_U, 'U');
		keyCodeToChar.put(KeyEvent.VK_V, 'V');
		keyCodeToChar.put(KeyEvent.VK_W, 'W');
		keyCodeToChar.put(KeyEvent.VK_X, 'X');
		keyCodeToChar.put(KeyEvent.VK_Y, 'Y');
		keyCodeToChar.put(KeyEvent.VK_Z, 'Z');
		
		try
		{
			robot = new Robot();
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
	}
}
