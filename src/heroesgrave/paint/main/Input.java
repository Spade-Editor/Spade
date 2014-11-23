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

import heroesgrave.paint.editing.Effect;
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.gui.ClipboardHandler;
import heroesgrave.paint.gui.Menu;
import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.RawImage.MaskMode;
import heroesgrave.paint.image.change.doc.FloatLayer;
import heroesgrave.paint.image.change.doc.MergeLayer;
import heroesgrave.paint.image.change.doc.NewLayer;
import heroesgrave.paint.image.change.edit.ClearMaskChange;
import heroesgrave.paint.image.change.edit.FillImageChange;
import heroesgrave.paint.image.change.edit.FillMaskChange;
import heroesgrave.paint.image.change.edit.ResizeCanvasChange;

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
		
		if(e.getKeyCode() == KeyEvent.VK_F5)
		{
			Paint.main.gui.chooser.toggle();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F6)
		{
			Paint.main.gui.layers.toggle();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			Paint.getDocument().getCurrent().addChange(new FillImageChange(0x00000000));
		}
		
		int MOVE = 1;
		
		if(e.isControlDown())
		{
			MOVE = 8;
			if(e.isShiftDown())
			{
				if(keyCodeToChar.containsKey(e.getKeyCode()))
				{
					Effect effect = Paint.getEffect(keyCodeToChar.get(e.getKeyCode()));
					if(effect != null)
					{
						effect.perform(Paint.getDocument().getCurrent());
						Paint.main.gui.repaint();
					}
				}
			}
			else
			{
				if(e.getKeyCode() == KeyEvent.VK_EQUALS)
				{
					float zoom = Paint.main.gui.canvasPanel.getScale();
					if(zoom < 1f)
					{
						Paint.main.gui.canvasPanel.setScale(Math.min(zoom * 2f, 1f));
					}
					else
					{
						Paint.main.gui.canvasPanel.setScale(Math.min(zoom + 2f, 64f));
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_MINUS)
				{
					float zoom = Paint.main.gui.canvasPanel.getScale();
					if(zoom <= 1f)
					{
						Paint.main.gui.canvasPanel.setScale(Math.max(zoom / 2f, 1 / 32f));
					}
					else
					{
						Paint.main.gui.canvasPanel.setScale(Math.max(zoom - 2f, 1f));
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_0)
				{
					Paint.main.gui.canvasPanel.setScale(1f);
				}
				else if(e.getKeyCode() == KeyEvent.VK_N)
				{
					Menu.showNewDialog();
				}
				else if(e.getKeyCode() == KeyEvent.VK_O)
				{
					Menu.showOpenMenu();
				}
				else if(e.getKeyCode() == KeyEvent.VK_B)
				{
					Menu.DARK_BACKGROUND = !Menu.DARK_BACKGROUND;
					Paint.main.gui.canvasPanel.repaint();
				}
				else if(e.getKeyCode() == KeyEvent.VK_Q)
				{
					if(Paint.getDocument() != null)
					{
						Paint.closeDocument(Paint.getDocument());
					}
					else
					{
						Paint.close();
					}
				}
				else if(Paint.getDocument() != null)
				{
					if(e.getKeyCode() == KeyEvent.VK_Z)
					{
						Paint.getDocument().getHistory().revertChange();
						Paint.main.gui.repaint();
					}
					else if(e.getKeyCode() == KeyEvent.VK_Y)
					{
						Paint.getDocument().getHistory().repeatChange();
						Paint.main.gui.repaint();
					}
					else if(e.getKeyCode() == KeyEvent.VK_S)
					{
						final boolean as = e.isAltDown();
						new Thread(new Runnable()
						{
							public void run()
							{
								if(as)
									Paint.saveAs(Paint.getDocument());
								else
									Paint.save(Paint.getDocument());
							}
						}).start();
					}
					else if(e.getKeyCode() == KeyEvent.VK_G)
					{
						// FIXME Grid isn't actually working.
						Menu.GRID_ENABLED = !Menu.GRID_ENABLED;
						Paint.main.gui.repaint();
					}
					else if(e.getKeyCode() == KeyEvent.VK_D)
					{
						Paint.getDocument().getCurrent().addChange(new ClearMaskChange());
					}
					else if(e.getKeyCode() == KeyEvent.VK_A)
					{
						Paint.getDocument().getCurrent().addChange(new FillMaskChange(MaskMode.ADD));
					}
					else if(e.getKeyCode() == KeyEvent.VK_I)
					{
						Paint.getDocument().getCurrent().addChange(new FillMaskChange(MaskMode.XOR));
					}
					else if(e.getKeyCode() == KeyEvent.VK_F)
					{
						Document doc = Paint.getDocument();
						if(!doc.getCurrent().isFloating())
						{
							doc.addChange(new FloatLayer(Paint.getDocument().getCurrent(), e.isAltDown()));
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_M)
					{
						Layer current = Paint.getDocument().getCurrent();
						if(current.getChildCount() == 0)
							Paint.getDocument().addChange(new MergeLayer(current));
					}
					else if(e.getKeyCode() == KeyEvent.VK_C)
					{
						ClipboardHandler.setImage(Paint.getDocument().getCurrent().getImage());
					}
					else if(e.getKeyCode() == KeyEvent.VK_X)
					{
						ClipboardHandler.setImage(Paint.getDocument().getCurrent().getImage());
						Paint.getDocument().getCurrent().addChange(new FillImageChange(0x00000000));
					}
					else if(e.getKeyCode() == KeyEvent.VK_V)
					{
						Document doc = Paint.getDocument();
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
		else
		{
			if(keyCodeToChar.containsKey(e.getKeyCode()))
			{
				Tool tool = Paint.getTool(keyCodeToChar.get(e.getKeyCode()));
				if(tool != null)
				{
					Paint.setTool(tool);
				}
			}
		}
		Point p = MouseInfo.getPointerInfo().getLocation();
		
		MOVE = (int) (MOVE * Paint.main.gui.canvasPanel.getScale());
		
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
