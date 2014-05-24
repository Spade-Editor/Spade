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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;

import com.alee.laf.menu.WebMenuItem;

public class ToolMenu
{
	public static JMenu createImageMenu()
	{
		JMenu menu = new JMenu("Image");
		/*
		menu.add(new ImageMenuItem("Blank Image", new Clear(), "B"));
		menu.add(new ImageMenuItem("Clear Image", new Clear2(), "C"));
		menu.add(new ImageMenuItem("Resize Image", new Resize(), "R"));
		menu.add(new ImageMenuItem("Resize Canvas", new ResizeCanvas(), null));
		menu.add(new ImageMenuItem("Flip Vertically", new FlipVert(), null));
		menu.add(new ImageMenuItem("Flip Horizontally", new FlipHoriz(), null));
		*/
		//heroesgrave.paint.plugin.PluginManager.instance.registerImageOps(menu);
		
		return menu;
	}
	
	public static JMenu createEffectMenu()
	{
		JMenu menu = new JMenu("Effects");
		
		/*
		menu.add(new ImageMenuItem("Invert Colour", new Invert(), "I"));
		menu.add(new ImageMenuItem("Grid-Maker", new MakeGrid(), "G"));
		menu.add(new ImageMenuItem("Channel Filter", new RemoveChannels(), "F"));
		menu.add(new ImageMenuItem("White Noise", new WhiteNoise(), null));
		menu.add(new ImageMenuItem("Simplex Noise", new SimplexNoiseOp(), null));
		menu.add(new ImageMenuItem("Simple Blur", new SimpleBlur(), null));
		menu.add(new ImageMenuItem("Simple Sharpen", new SimpleSharpen(), null));
		*/
		// BUGGED -> menu.add(new ImageMenuItem("Simple Edge Detect", new SimpleEdgeDetect(), null));
		// BUGGED -> menu.add(new ImageMenuItem("Perlin Noise", new PerlinNoiseOp(), null));
		
		//heroesgrave.paint.plugin.PluginManager.instance.registerEffects(menu);
		
		return menu;
	}
	
	@SuppressWarnings("serial")
	public static class ToolMenuItem extends WebMenuItem
	{
		private Tool tool;
		
		public ToolMenuItem(String name, Tool t, String key)
		{
			super(key == null ? (name) : (name + " (" + key + ")"));
			
			// This is here, so some Tools don't have to have a key assigned. We can't have key-code's for ALL the Tools! It's impossible!
			if(key != null)
			{
				Paint.addTool(key, t);
			}
			
			this.tool = t;
			
			// TRY to load the icon!
			try
			{
				URL url = this.getClass().getResource("/heroesgrave/paint/res/icons/tools/" + name + ".png");
				
				if(url != null)
				{
					this.setIcon(new ImageIcon(ImageIO.read(url)));
				}
				else
				{
					this.setIcon(new ImageIcon(ImageIO.read(Paint.questionMarkURL)));
				}
				
			}
			catch(IOException e1)
			{
				System.err.println("Error: Tool '" + name + "' is missing an icon!");
			}
			
			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Paint.setTool(tool);
				}
			});
		}
	}
	
	/*
	public static class ImageMenuItem extends JMenuItem
	{
		private static final long serialVersionUID = 7018700148731008154L;
		
		private ImageOp op;
		
		public ImageMenuItem(String name, ImageOp o, String key)
		{
			this(name, o, key, null);
		}
		
		public ImageMenuItem(String name, ImageOp o, String key, String toolTip)
		{
			super(key == null ? (name) : (name + " (Ctrl+Shift+" + key + ")"));
			
			// This is here, so some ImageOps don't have to have a key assigned. We can't have key-code's for ALL the ImageOp's! It's impossible!
			if(key != null)
			{
				Paint.addImageOp(key, o);
			}
			
			// If there is a ToolTip Text given over the Constructor, use it.
			if(toolTip != null)
			{
				this.setToolTipText(toolTip);
			}
			
			// 
			this.op = o;
			
			// TRY to load the icon!
			try
			{
				URL url =
						this.getClass().getResource(
								"/heroesgrave/paint/res/icons/imageops/" + name
										+ ".png");
				
				if(url != null)
				{
					this.setIcon(new ImageIcon(ImageIO.read(url)));
				}
				else
				{
					this.setIcon(new ImageIcon(ImageIO
							.read(Paint.questionMarkURL)));
				}
			}
			catch(IOException e1)
			{
				System.err.println("Error: ImageOp '" + name
						+ "' is missing an icon!");
			}
			
			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					op.operation();
				}
			});
		}
	}
	*/
}
