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
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.io.TxtFileFilter;
import heroesgrave.utils.math.MathUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;

public class ColourChooser
{
	
	public class SelectedEditColorSelector extends JComponent implements MouseListener
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4059082708126254566L;
		
		public SelectedEditColorSelector()
		{
			
			this.setSize(64, 64);
			this.setMinimumSize(new Dimension(64, 64));
			this.setPreferredSize(new Dimension(64, 64));
			this.addMouseListener(this);
			this.setToolTipText("Click to edit the secondary color!");
			
		}
		
		@Override
		public void paint(Graphics $g)
		{
			Graphics2D g = (Graphics2D) $g;
			
			g.setPaint(transparenzyImagePaint);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setPaint(null);
			
			if(isEditingLeft)
			{
				this.setToolTipText("Click to edit the secondary color!");
				dialog.setTitle("Colour-Chooser - Editing PRIMARY Color");
				
				// BACK
				g.setColor(new Color(rightColour, true));
				g.fillRect(getWidth() / 2, 0, getWidth(), getHeight());
				
				// FRONT
				g.setColor(new Color(leftColour, true));
				g.fillRect(0, 0, getWidth() / 2, getHeight());
			}
			else
			{
				this.setToolTipText("Click to edit the primary color!");
				dialog.setTitle("Colour-Chooser - Editing SECONDARY Color");
				
				// BACK
				g.setColor(new Color(leftColour, true));
				g.fillRect(getWidth() / 2, 0, getWidth(), getHeight());
				
				// FRONT
				g.setColor(new Color(rightColour, true));
				g.fillRect(0, 0, getWidth() / 2, getHeight());
			}
			
			g.setColor(Color.WHITE);
			g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
			
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			isEditingLeft = !isEditingLeft;
			
			updateAllChooserSubComponents_EditChanged();
			this.repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
		}
		
	}
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**                 INNER CLASSES                **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	@SuppressWarnings("serial")
	private class ColorChooser_HexColor extends JTextField implements ActionListener
	{
		
		public ColorChooser_HexColor(Container parent)
		{
			super(Integer.toHexString(leftColour).toUpperCase());
			this.addActionListener(this);
			this.setToolTipText("Type in (or paste in) any Hexadecimal Color, then press Enter to apply the change.");
			this.setColumns(6);
		}
		
		private void reset()
		{
			if(isEditingLeft)
			{
				
				invokeTextChangeLater("000000");
				setSelectedEditColour((getSelectedEditColor() & 0xFF000000));
			}
			else
			{
				invokeTextChangeLater("FFFFFF");
				setSelectedEditColour((getSelectedEditColor() & 0xFF000000) | 0xFFFFFF);
			}
		}
		
		private void invokeTextChangeLater(final String newText)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					setText(newText);
				}
			});
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			sliderLastChanged = -1;
			try
			{
				int newColor = Integer.valueOf(getText().toLowerCase(), 16);
				setSelectedEditColour((getSelectedEditColor() & 0xFF000000) | newColor);
			}
			catch(Exception e)
			{
				// Fully ignore all exceptions and just reset things!
				// Resetting things should put everything back in order.
				reset();
			}
			
			// Update the Paint-GUI with the new colour!
			updatePaintGUI();
		}
		
		public void outerColourUpdate()
		{
			invokeTextChangeLater(bufferZeros(Integer.toHexString(getSelectedEditColor() & 0xFFFFFF).toUpperCase()));
		}
		
		// Add leading zeros in case there is no red component.
		private String bufferZeros(String s)
		{
			while(s.length() < 6)
			{
				s = "0" + s;
			}
			return s;
		}
	}
	
	private abstract class ColorChooser_ColourSlider extends JComponent implements MouseListener, MouseMotionListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4914193922841062277L;
		
		float sliderValue;
		
		// These are ONLY there for visial stuff!
		int gradientLeft;
		int gradientRight;
		
		boolean mouseHover;
		boolean sliderGlow;
		
		public ColorChooser_ColourSlider(int LEFT, int RIGHT, float initialValue)
		{
			setGradientColors(LEFT, RIGHT);
			sliderValue = initialValue;
			mouseHover = false;
			sliderGlow = false;
			
			this.setPreferredSize(new Dimension(48, 16));
			this.setMinimumSize(new Dimension(48, 16));
			
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
		}
		
		public void setGradientColors(int LEFT, int RIGHT)
		{
			gradientLeft = LEFT;
			gradientRight = RIGHT;
			this.repaint();
		}
		
		@Override
		public void paint(Graphics $g)
		{
			Graphics2D g = (Graphics2D) $g;
			g.setPaint(transparenzyImagePaint);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setPaint(new GradientPaint(0, 0, new Color(gradientLeft, true), getWidth(), 0, new Color(gradientRight, true)));
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setPaint(null);
			
			if(sliderGlow)
			{
				g.setColor(Color.BLUE);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
			
			int posX = (int) (sliderValue * getWidth());
			
			g.setColor(Color.WHITE);
			g.drawRect(posX - 1, 0, 3, getHeight() - 1);
			g.setColor(Color.BLACK);
			g.drawRect(posX - 2, 0, 5, getHeight() - 1);
			
		}
		
		public void update(int mouseX, int currentWidth)
		{
			sliderLastChanged = -1;
			// first out-of-bounds check
			if(mouseX < 0)
				mouseX = 0;
			if(mouseX > currentWidth)
				mouseX = currentWidth;
			
			sliderValue = (float) mouseX / (float) currentWidth;
			;
			
			// second out-of-bounds check
			if(sliderValue < 0)
				sliderValue = 0;
			if(sliderValue > 1)
				sliderValue = 1;
			
			this.onSliderUpdate(sliderValue, (int) MathUtils.clamp(sliderValue * 256, 0, 255));
			
			this.repaint();
			
		}
		
		public void onSliderUpdate(float sliderValue)
		{
			// out-of-bounds check
			if(sliderValue < 0)
				sliderValue = 0;
			if(sliderValue > 1)
				sliderValue = 1;
			
			this.onSliderUpdate(sliderValue, (int) MathUtils.clamp(sliderValue * 256, 0, 255));
		}
		
		public abstract void onSliderUpdate(float sliderValue, int sliderValueInt);
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			sliderGlow = true;
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(!mouseHover)
				sliderGlow = false;
			this.repaint();
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			mouseHover = true;
			sliderGlow = true;
			this.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			mouseHover = false;
			sliderGlow = false;
			this.repaint();
		}
		
	}
	
	private class ColorChooser_ColourSliderHSBimplH extends JComponent implements MouseListener, MouseMotionListener
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7592259528579628771L;
		
		private float sliderValue;
		
		boolean mouseHover;
		boolean sliderGlow;
		
		public ColorChooser_ColourSliderHSBimplH()
		{
			
			this.setPreferredSize(new Dimension(48, 18));
			this.setMinimumSize(new Dimension(48, 18));
			
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
			
		}
		
		public void update(int mouseX, int currentWidth)
		{
			sliderLastChanged = 1;
			// first out-of-bounds check
			if(mouseX < 0)
				mouseX = 0;
			if(mouseX > currentWidth)
				mouseX = currentWidth;
			
			sliderValue = (float) mouseX / (float) currentWidth;
			;
			
			// second out-of-bounds check
			if(sliderValue < 0)
				sliderValue = 0;
			if(sliderValue > 1)
				sliderValue = 1;
			
			this.onSliderUpdate(sliderValue, (int) MathUtils.clamp(sliderValue * 256, 0, 255));
			
			this.repaint();
			
		}
		
		public void onSliderUpdate(float sliderValue, int sliderValueInt)
		{
			int colorIn = getSelectedEditColor();
			float[] vals = new float[3];
			RGBtoHSB((colorIn >> 16) & 0xFF, (colorIn >> 8) & 0xFF, (colorIn) & 0xFF, vals);
			
			float h = sliderValue;
			float s = vals[1];
			float b = vals[2];
			int newColor = Color.HSBtoRGB(h, s, b);
			
			newColor &= 0xFFFFFF;
			newColor |= colorIn & 0xFF000000;
			
			setSelectedEditColour(newColor);
			updateAllChooserSubComponents_EditChanged();
			
		}
		
		@Override
		public void paint(Graphics $g)
		{
			Graphics2D g = (Graphics2D) $g;
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			for(int i = 0; i < getWidth(); i++)
			{
				g.setColor(Color.getHSBColor((float) i / (float) getWidth(), 1F, 1F));
				g.fillRect(i, 0, 2, getHeight());
			}
			
			if(sliderGlow)
			{
				g.setColor(Color.BLUE);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
			
			int posX = (int) (sliderValue * getWidth());
			
			g.setColor(Color.WHITE);
			g.drawRect(posX - 1, 0, 3, getHeight() - 1);
			g.setColor(Color.BLACK);
			g.drawRect(posX - 2, 0, 5, getHeight() - 1);
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			sliderGlow = true;
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(!mouseHover)
				sliderGlow = false;
			this.repaint();
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			mouseHover = true;
			sliderGlow = true;
			this.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			mouseHover = false;
			sliderGlow = false;
			this.repaint();
		}
		
		public void updateColorIntoSlider(int colorIn)
		{
			float[] vals = new float[3];
			RGBtoHSB((colorIn >> 16) & 0xFF, (colorIn >> 8) & 0xFF, (colorIn) & 0xFF, vals);
			this.sliderValue = vals[0];
			HSBSquare.rebuildSquare();
			HSBSquare.repaint();
			this.repaint();
		}
	}
	
	private class ColorChooser_ColourSliderHSBimplS extends JComponent implements MouseListener, MouseMotionListener
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7592259528579628771L;
		
		private float sliderValue;
		
		boolean mouseHover;
		boolean sliderGlow;
		
		public ColorChooser_ColourSliderHSBimplS()
		{
			
			this.setPreferredSize(new Dimension(48, 18));
			this.setMinimumSize(new Dimension(48, 18));
			
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
			
		}
		
		public void update(int mouseX, int currentWidth)
		{
			sliderLastChanged = 2;
			// first out-of-bounds check
			if(mouseX < 0)
				mouseX = 0;
			if(mouseX > currentWidth)
				mouseX = currentWidth;
			
			sliderValue = (float) mouseX / (float) currentWidth;
			;
			
			// second out-of-bounds check
			if(sliderValue < 0)
				sliderValue = 0;
			if(sliderValue > 1)
				sliderValue = 1;
			
			this.onSliderUpdate(sliderValue, (int) MathUtils.clamp(sliderValue * 256, 0, 255));
			
			this.repaint();
			
		}
		
		public void onSliderUpdate(float sliderValue, int sliderValueInt)
		{
			int colorIn = getSelectedEditColor();
			float[] vals = new float[3];
			RGBtoHSB((colorIn >> 16) & 0xFF, (colorIn >> 8) & 0xFF, (colorIn) & 0xFF, vals);
			
			float h = vals[0];
			float s = sliderValue;
			float b = vals[2];
			int newColor = Color.HSBtoRGB(h, s, b);
			
			newColor &= 0xFFFFFF;
			newColor |= colorIn & 0xFF000000;
			
			setSelectedEditColour(newColor);
			updateAllChooserSubComponents_EditChanged();
			
		}
		
		@Override
		public void paint(Graphics $g)
		{
			Graphics2D g = (Graphics2D) $g;
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			int _r = (getSelectedEditColor() >> 16) & 0xFF;
			int _g = (getSelectedEditColor() >> 8) & 0xFF;
			int _b = (getSelectedEditColor()) & 0xFF;
			float[] vals = RGBtoHSB(_r, _g, _b, null);
			
			for(int i = 0; i < getWidth(); i++)
			{
				g.setColor(Color.getHSBColor(vals[0], (float) i / (float) getWidth(), 1F));
				g.fillRect(i, 0, 2, getHeight());
			}
			
			if(sliderGlow)
			{
				g.setColor(Color.BLUE);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
			
			int posX = (int) (sliderValue * getWidth());
			
			g.setColor(Color.WHITE);
			g.drawRect(posX - 1, 0, 3, getHeight() - 1);
			g.setColor(Color.BLACK);
			g.drawRect(posX - 2, 0, 5, getHeight() - 1);
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			sliderGlow = true;
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(!mouseHover)
				sliderGlow = false;
			this.repaint();
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			mouseHover = true;
			sliderGlow = true;
			this.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			mouseHover = false;
			sliderGlow = false;
			this.repaint();
		}
		
		public void updateColorIntoSlider(int colorIn)
		{
			float[] vals = new float[3];
			RGBtoHSB((colorIn >> 16) & 0xFF, (colorIn >> 8) & 0xFF, (colorIn) & 0xFF, vals);
			this.sliderValue = vals[1];
			this.repaint();
		}
	}
	
	private class ColorChooser_ColourSliderHSBimplB extends JComponent implements MouseListener, MouseMotionListener
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7592259528579628771L;
		
		private float sliderValue;
		
		boolean mouseHover;
		boolean sliderGlow;
		
		public ColorChooser_ColourSliderHSBimplB()
		{
			
			this.setPreferredSize(new Dimension(48, 18));
			this.setMinimumSize(new Dimension(48, 18));
			
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
			
		}
		
		public void update(int mouseX, int currentWidth)
		{
			sliderLastChanged = 2;
			// first out-of-bounds check
			if(mouseX < 0)
				mouseX = 0;
			if(mouseX > currentWidth)
				mouseX = currentWidth;
			
			sliderValue = (float) mouseX / (float) currentWidth;
			;
			
			// second out-of-bounds check
			if(sliderValue < 0)
				sliderValue = 0;
			if(sliderValue > 1)
				sliderValue = 1;
			
			this.onSliderUpdate(sliderValue, (int) MathUtils.clamp(sliderValue * 256, 0, 255));
			
			this.repaint();
			
		}
		
		public void onSliderUpdate(float sliderValue, int sliderValueInt)
		{
			int colorIn = getSelectedEditColor();
			float[] vals = new float[3];
			RGBtoHSB((colorIn >> 16) & 0xFF, (colorIn >> 8) & 0xFF, (colorIn) & 0xFF, vals);
			
			float h = vals[0];
			float s = vals[1];
			float b = sliderValue;
			int newColor = Color.HSBtoRGB(h, s, b);
			
			newColor &= 0xFFFFFF;
			newColor |= colorIn & 0xFF000000;
			
			setSelectedEditColour(newColor);
			updateAllChooserSubComponents_EditChanged();
			
		}
		
		@Override
		public void paint(Graphics $g)
		{
			Graphics2D g = (Graphics2D) $g;
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			int _r = (getSelectedEditColor() >> 16) & 0xFF;
			int _g = (getSelectedEditColor() >> 8) & 0xFF;
			int _b = (getSelectedEditColor()) & 0xFF;
			float[] vals = RGBtoHSB(_r, _g, _b, null);
			
			for(int i = 0; i < getWidth(); i++)
			{
				g.setColor(Color.getHSBColor(vals[0], vals[1], (float) i / (float) getWidth()));
				g.fillRect(i, 0, 2, getHeight());
			}
			
			if(sliderGlow)
			{
				g.setColor(Color.BLUE);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
			
			int posX = (int) (sliderValue * getWidth());
			
			g.setColor(Color.WHITE);
			g.drawRect(posX - 1, 0, 3, getHeight() - 1);
			g.setColor(Color.BLACK);
			g.drawRect(posX - 2, 0, 5, getHeight() - 1);
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			sliderGlow = true;
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			update(e.getX(), this.getWidth() - 1);
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			// IGNORE
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(!mouseHover)
				sliderGlow = false;
			this.repaint();
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			mouseHover = true;
			sliderGlow = true;
			this.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			mouseHover = false;
			sliderGlow = false;
			this.repaint();
		}
		
		public void updateColorIntoSlider(int colorIn)
		{
			float[] vals = new float[3];
			RGBtoHSB((colorIn >> 16) & 0xFF, (colorIn >> 8) & 0xFF, (colorIn) & 0xFF, vals);
			this.sliderValue = vals[2];
			this.repaint();
		}
		
	}
	
	public class ColorPalletEntryButton extends JComponent implements MouseListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2473933582943592038L;
		
		Color color;
		boolean mouseHover;
		
		public ColorPalletEntryButton(Color color)
		{
			this.color = color;
			this.mouseHover = false;
			
			this.setSize(16, 16);
			this.setPreferredSize(new Dimension(16, 16));
			//this.setMaximumSize(new Dimension(16, 16));
			this.setMinimumSize(new Dimension(16, 16));
			this.addMouseListener(this);
			
		}
		
		@Override
		public void paint(Graphics $g)
		{
			Graphics2D g = (Graphics2D) $g;
			
			g.setPaint(new TexturePaint(transparenzyImage, new Rectangle2D.Float(0, 0, getWidth() / 2, getHeight() / 2)));
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setPaint(null);
			
			g.setColor(color);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			if(mouseHover)
			{
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				g.setColor(Color.WHITE);
				g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			
			if(e.getButton() == MouseEvent.BUTTON1)
				leftColour = color.getRGB();
			
			if(e.getButton() == MouseEvent.BUTTON3)
				rightColour = color.getRGB();
			
			if(e.getButton() == MouseEvent.BUTTON2)
				leftColour = rightColour = color.getRGB();
			
			sliderLastChanged = -1;
			
			updateAllChooserSubComponents_EditChanged();
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			mouseHover = true;
			repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			mouseHover = false;
			repaint();
		}
		
	}
	
	private class ColorSquare extends JPanel implements MouseListener, MouseMotionListener
	{
		
		private static final long serialVersionUID = 4402136903377952923L;
		
		private BufferedImage square;
		
		private int button = -1;
		
		private int lastX = 0, lastY = 0;
		
		private final int CURSOR_RADIUS = 6;
		
		public ColorSquare(int w, int h)
		{
			setSize(w, h);
			//setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		@Override
		public void paint(Graphics g)
		{
			if(square == null || square.getWidth() != getHeight() || square.getHeight() != getHeight())
				rebuildSquare();
			
			g.drawImage(square, 0, 0, getHeight(), getHeight(), null);
			
			lastX = (int) (chooserRightHSBimplS.sliderValue * getHeight());
			lastY = (int) ((getHeight() - chooserRightHSBimplB.sliderValue * getHeight()));
			
			g.setColor(Color.black);
			g.setXORMode(Color.white);
			g.drawOval(lastX - CURSOR_RADIUS / 2, lastY - CURSOR_RADIUS / 2, CURSOR_RADIUS, CURSOR_RADIUS);
			g.setPaintMode();
			
		}
		
		private void rebuildSquare()
		{
			square = new BufferedImage(getHeight(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = square.createGraphics();
			
			for(int y = 0; y < getHeight(); y++)
				for(int x = 0; x < getHeight(); x++)
				{
					g.setColor(new Color(Color.HSBtoRGB(chooserRightHSBimplH.sliderValue, 1f * x / (getHeight() - 1), 1f * ((getHeight() - 1) - y)
							/ (getHeight() - 1))));
					g.drawLine(x, y, x, y);
				}
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			mouseReleased(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			int x = e.getX();
			int y = e.getY();
			
			sliderLastChanged = y >= getHeight() - 1 ? 2 : 1;
			
			if(x < 0)
				x = 0;
			else if(x >= square.getWidth())
				x = square.getWidth() - 1;
			
			if(y < 0)
				y = 0;
			else if(y >= square.getHeight())
				y = square.getHeight() - 1;
			
			if(button == MouseEvent.BUTTON1)
				leftColour = square.getRGB(x, y);
			
			if(button == MouseEvent.BUTTON3)
				rightColour = square.getRGB(x, y);
			
			if(button == MouseEvent.BUTTON2)
				leftColour = square.getRGB(x, y);
			
			lastX = x;
			lastY = y;
			
			updateAllChooserSubComponents_EditChanged();
			updatePaintGUI();
			
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			button = e.getButton();
			mouseReleased(e);
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
		}
		
	}
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**                 OBJECT FIELDS                **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	WebDialog dialog;
	SpringLayout dialogLayout;
	
	JPanel chooserLeft;
	SpringLayout chooserLeftLayout;
	JPanel chooserRight;
	SpringLayout chooserRightLayout;
	
	SelectedEditColorSelector chooserLeftColourSelectorEditColourSelector;
	
	JPanel chooserLeftColourSelector;
	SpringLayout chooserLeftColourSelectorLayout;
	JPanel chooserLeftColourCircle;
	JPanel chooserLeftColourPallete;
	
	JPanel chooserRightRGB;
	JPanel chooserRightHEX;
	JPanel chooserRightHSB;
	JPanel chooserRightALPHA;
	
	ColorChooser_ColourSlider chooserRightRGBimplR;
	ColorChooser_ColourSlider chooserRightRGBimplG;
	ColorChooser_ColourSlider chooserRightRGBimplB;
	ColorChooser_HexColor chooserRightHEXimpl;
	ColorChooser_ColourSliderHSBimplH chooserRightHSBimplH;
	ColorChooser_ColourSliderHSBimplS chooserRightHSBimplS;
	ColorChooser_ColourSliderHSBimplB chooserRightHSBimplB;
	ColorChooser_ColourSlider chooserRightALPHAimpl;
	
	ColorSquare HSBSquare;
	
	/**
	 * The 'left'-colour.
	 **/
	private int leftColour = 0xFFFFFF;
	
	/**
	 * The 'right'-colour.
	 **/
	private int rightColour = 0;
	
	/**
	 * Flag that tells us if we are editing the LEFT or RIGHT color.
	 **/
	private boolean isEditingLeft = true;
	
	private boolean initialized = false;
	
	/**
	 * A 'transparency' background for usage in the colour-chooser's components.
	 **/
	private static BufferedImage transparenzyImage;
	private static Rectangle2D transparenzyImageRect;
	private static TexturePaint transparenzyImagePaint;
	
	/**                                              **/
	/**                                              **/
	/**                                              **/
	/**               OBJECT CONSTRUCTOR             **/
	/**                                              **/
	/**                                              **/
	/**                                              **/
	
	static
	{
		transparenzyImage = new BufferedImage(2, 2, BufferedImage.TYPE_BYTE_GRAY);
		transparenzyImage.setRGB(0, 0, 0xDDDDDD);
		transparenzyImage.setRGB(1, 1, 0xDDDDDD);
		transparenzyImage.setRGB(1, 0, 0xAAAAAA);
		transparenzyImage.setRGB(0, 1, 0xAAAAAA);
		
		transparenzyImageRect = new Rectangle2D.Float(0, 0, 8, 8);
		transparenzyImagePaint = new TexturePaint(transparenzyImage, transparenzyImageRect);
		
	}
	
	@SuppressWarnings("serial")
	public ColourChooser(WebFrame mainFrame)
	{
		
		// ----- Create the Dialog
		dialog = new WebDialog(mainFrame, "Colour-Chooser - Editing PRIMARY Color");
		dialogLayout = new SpringLayout();
		
		// ----- Do the typical configurations for it.
		dialog.setSize(430, 290);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.setLayout(dialogLayout);
		dialog.setFocusable(false);
		dialog.setAutoRequestFocus(false);
		
		// ----- Build the LEFT/RIGHT components and set their layout.
		// build left
		chooserLeft = new JPanel();
		chooserLeftLayout = new SpringLayout();
		chooserLeft.setBorder(BorderFactory.createLoweredBevelBorder());
		chooserLeft.setLayout(chooserLeftLayout);
		
		// build right
		chooserRight = new JPanel();
		chooserRightLayout = new SpringLayout();
		chooserRight.setBorder(BorderFactory.createLoweredBevelBorder());
		chooserRight.setLayout(chooserRightLayout);
		
		// temporary stuff
		// ...
		
		// build layout
		buildLayoutForChooserRoot();
		
		// add
		dialog.add(chooserLeft);
		dialog.add(chooserRight);
		
		// ----- Construction of chooser content LEFT
		chooserLeftColourSelector = new JPanel();
		chooserLeftColourCircle = new JPanel();
		chooserLeftColourPallete = new JPanel();
		
		chooserLeftColourSelectorLayout = new SpringLayout();
		
		chooserLeftColourSelector.setLayout(chooserLeftColourSelectorLayout);
		
		SpringLayout colorSquareLayout = new SpringLayout();
		
		chooserLeftColourCircle.setLayout(colorSquareLayout);
		chooserLeftColourPallete.setLayout(new GridLayout(0, 16, 0, 0));
		
		HSBSquare = new ColorSquare(256, 256);
		
		// borders
		//chooserLeftColourCircle.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), "Color Circle"));
		chooserLeftColourPallete.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), "Color Pallet"));
		
		chooserLeftColourCircle.setSize(256, 256);
		JLabel title = new JLabel("Color Square");
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		chooserLeftColourCircle.add(title);
		chooserLeftColourCircle.add(HSBSquare);
		
		colorSquareLayout.putConstraint(SpringLayout.NORTH, title, 6, SpringLayout.NORTH, chooserLeftColourCircle);
		colorSquareLayout.putConstraint(SpringLayout.WEST, title, 6, SpringLayout.WEST, chooserLeftColourCircle);
		
		colorSquareLayout.putConstraint(SpringLayout.NORTH, HSBSquare, 0, SpringLayout.NORTH, chooserLeftColourCircle);
		colorSquareLayout.putConstraint(SpringLayout.SOUTH, HSBSquare, 0, SpringLayout.SOUTH, chooserLeftColourCircle);
		colorSquareLayout.putConstraint(SpringLayout.WEST, HSBSquare, -76, SpringLayout.EAST, chooserLeftColourCircle);
		colorSquareLayout.putConstraint(SpringLayout.EAST, HSBSquare, 0, SpringLayout.EAST, chooserLeftColourCircle);
		
		Color[] pallet = readColorPalletFromURL(ClassLoader.getSystemResource("heroesgrave/paint/res/defaultColorPallet.txt"));
		
		// chooserLeftColourPallete.add(new JLabel("Not yet implemented!"));
		for(int i = 0; i < pallet.length; i++)
			chooserLeftColourPallete.add(new ColorPalletEntryButton(pallet[i]));
		
		// sub-components
		chooserLeftColourSelectorEditColourSelector = new SelectedEditColorSelector();
		
		JButton colourResetButton = new JButton(new AbstractAction("Reset Colors")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				leftColour = 0xFF000000;
				rightColour = 0xFFFFFFFF;
				updateAllChooserSubComponents_EditChanged();
			}
		});
		
		JButton changeColourPalletButton = new JButton(new AbstractAction("Change Pallet")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setCurrentDirectory(new File(IOUtils.assemblePath(System.getProperty("user.home"), ".paint-java", "palettes")));
				
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(new TxtFileFilter());
				fileChooser.setDialogTitle("Open Color-Pallet");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(dialog);
				
				if(result != JFileChooser.APPROVE_OPTION)
					return;
				
				Color[] newPallet = null;
				
				try
				{
					newPallet = readColorPalletFromURL(fileChooser.getSelectedFile().toURI().toURL());
				}
				catch(MalformedURLException e1)
				{
					e1.printStackTrace();
					newPallet = null;
				}
				
				if(newPallet == null)
					return;
				
				chooserLeftColourPallete.removeAll();
				chooserLeftColourPallete.revalidate();
				chooserLeftColourPallete.setLayout(new GridLayout(0, 16, 0, 0));
				
				for(int i = 0; i < newPallet.length; i++)
					chooserLeftColourPallete.add(new ColorPalletEntryButton(newPallet[i]));
				
				chooserLeftColourPallete.revalidate();
				chooserLeftColourPallete.repaint();
				
			}
		});
		
		JButton colourSwapButton = new JButton(new AbstractAction("SWAP")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sliderLastChanged = -1;
				int SWAP = leftColour;
				leftColour = rightColour;
				rightColour = SWAP;
				
				updateAllChooserSubComponents_EditChanged();
			}
		});
		
		// layout for ((chooserLeftColourSelector))
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.NORTH, chooserLeftColourSelectorEditColourSelector, 0, SpringLayout.NORTH,
				chooserLeftColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.WEST, chooserLeftColourSelectorEditColourSelector, 0, SpringLayout.WEST,
				chooserLeftColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.SOUTH, chooserLeftColourSelectorEditColourSelector, 64, SpringLayout.NORTH,
				chooserLeftColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.EAST, chooserLeftColourSelectorEditColourSelector, 64, SpringLayout.WEST,
				chooserLeftColourSelector);
		
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.NORTH, colourResetButton, 0, SpringLayout.NORTH, chooserLeftColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.WEST, colourResetButton, 0, SpringLayout.EAST, chooserLeftColourSelectorEditColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.SOUTH, colourResetButton, 32, SpringLayout.NORTH, chooserLeftColourSelector);
		
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.NORTH, changeColourPalletButton, 0, SpringLayout.SOUTH, colourResetButton);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.WEST, changeColourPalletButton, 0, SpringLayout.EAST,
				chooserLeftColourSelectorEditColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.SOUTH, changeColourPalletButton, 64, SpringLayout.NORTH, chooserLeftColourSelector);
		
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.EAST, changeColourPalletButton, 0, SpringLayout.WEST, colourSwapButton);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.EAST, colourResetButton, 0, SpringLayout.WEST, colourSwapButton);
		
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.NORTH, colourSwapButton, 0, SpringLayout.NORTH, chooserLeftColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.WEST, colourSwapButton, -64, SpringLayout.EAST, chooserLeftColourSelector);
		chooserLeftColourSelectorLayout.putConstraint(SpringLayout.SOUTH, colourSwapButton, 64, SpringLayout.NORTH, chooserLeftColourSelector);
		
		chooserLeftColourSelector.add(chooserLeftColourSelectorEditColourSelector);
		chooserLeftColourSelector.add(changeColourPalletButton);
		chooserLeftColourSelector.add(colourResetButton);
		chooserLeftColourSelector.add(colourSwapButton);
		
		// layout
		
		buildLayoutForChooserLeftContent();
		
		chooserLeft.add(chooserLeftColourSelector);
		chooserLeft.add(chooserLeftColourCircle);
		chooserLeft.add(chooserLeftColourPallete);
		
		// ----- Construction of chooser content RIGHT
		// build
		chooserRightRGB = new JPanel();
		chooserRightHEX = new JPanel();
		chooserRightHSB = new JPanel();
		chooserRightALPHA = new JPanel();
		
		chooserRightHEXimpl = new ColorChooser_HexColor(chooserRightHEX);
		
		chooserRightALPHAimpl = new ColorChooser_ColourSlider(0x00000000, 0xFF000000, 1F)
		{
			@Override
			public void onSliderUpdate(float sliderValue, int sliderValueInt)
			{
				int currentColor = getSelectedEditColor();
				int withoutAlpha = currentColor & 0xFFFFFF;
				int withNewAlpha = withoutAlpha | ((sliderValueInt & 0xFF) << 24);
				setSelectedEditColour(withNewAlpha);
			}
		};
		
		chooserRightRGBimplR = new ColorChooser_ColourSlider(0xFF000000, 0xFFFF0000, 0F)
		{
			@Override
			public void onSliderUpdate(float sliderValue, int sliderValueInt)
			{
				int currentColor = getSelectedEditColor();
				int withoutCOLOR = currentColor & 0xFF00FFFF;
				int withNewCOLOR = withoutCOLOR | ((sliderValueInt & 0xFF) << 16);
				setSelectedEditColour(withNewCOLOR);
			}
		};
		chooserRightRGB.add(chooserRightRGBimplR);
		
		chooserRightRGBimplG = new ColorChooser_ColourSlider(0xFF000000, 0xFF00FF00, 0F)
		{
			@Override
			public void onSliderUpdate(float sliderValue, int sliderValueInt)
			{
				int currentColor = getSelectedEditColor();
				int withoutCOLOR = currentColor & 0xFFFF00FF;
				int withNewCOLOR = withoutCOLOR | ((sliderValueInt & 0xFF) << 8);
				setSelectedEditColour(withNewCOLOR);
			}
		};
		chooserRightRGB.add(chooserRightRGBimplG);
		
		chooserRightRGBimplB = new ColorChooser_ColourSlider(0xFF000000, 0xFF0000FF, 0F)
		{
			@Override
			public void onSliderUpdate(float sliderValue, int sliderValueInt)
			{
				int currentColor = getSelectedEditColor();
				int withoutCOLOR = currentColor & 0xFFFFFF00;
				int withNewCOLOR = withoutCOLOR | ((sliderValueInt & 0xFF));
				setSelectedEditColour(withNewCOLOR);
			}
		};
		chooserRightRGB.add(chooserRightRGBimplB);
		
		chooserRightHSBimplH = new ColorChooser_ColourSliderHSBimplH();
		chooserRightHSBimplS = new ColorChooser_ColourSliderHSBimplS();
		chooserRightHSBimplB = new ColorChooser_ColourSliderHSBimplB();
		
		// border
		chooserRightRGB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), "RGB"));
		chooserRightHEX.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), "HEX"));
		chooserRightHSB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), "HSB"));
		chooserRightALPHA.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), "Alpha"));
		
		// inner layout
		chooserRightRGB.setLayout(new GridLayout(0, 1, 4, 4));
		chooserRightHEX.setLayout(new BorderLayout());
		chooserRightHSB.setLayout(new GridLayout(0, 1, 4, 4));
		chooserRightALPHA.setLayout(new BorderLayout());
		
		// Add implementation components to the color choosers sub's.
		chooserRightHEX.add(chooserRightHEXimpl);
		chooserRightHSB.add(chooserRightHSBimplH);
		chooserRightHSB.add(chooserRightHSBimplS);
		chooserRightHSB.add(chooserRightHSBimplB);
		chooserRightALPHA.add(chooserRightALPHAimpl);
		
		buildLayoutForChooserRightContent();
		
		chooserRight.add(chooserRightRGB);
		chooserRight.add(chooserRightHEX);
		chooserRight.add(chooserRightHSB);
		chooserRight.add(chooserRightALPHA);
		
		// ----- ???
		chooserRightRGBimplR.onSliderUpdate(chooserRightRGBimplR.sliderValue);
		chooserRightRGBimplG.onSliderUpdate(chooserRightRGBimplG.sliderValue);
		chooserRightRGBimplB.onSliderUpdate(chooserRightRGBimplB.sliderValue);
		
		updateAllChooserSubComponents_ColorChanged();
		
		initialized = true;
		
	}
	
	private void buildLayoutForChooserRoot()
	{
		// (only for construction) Fetch the contentPane from the dialog.
		Container dialogContentPane = dialog.getContentPane();
		
		// layout (top/bottom springs)
		dialogLayout.putConstraint(SpringLayout.NORTH, chooserLeft, 0, SpringLayout.NORTH, dialogContentPane);
		dialogLayout.putConstraint(SpringLayout.NORTH, chooserRight, 0, SpringLayout.NORTH, dialogContentPane);
		dialogLayout.putConstraint(SpringLayout.SOUTH, chooserLeft, 0, SpringLayout.SOUTH, dialogContentPane);
		dialogLayout.putConstraint(SpringLayout.SOUTH, chooserRight, 0, SpringLayout.SOUTH, dialogContentPane);
		
		// layout (left/right springs)
		dialogLayout.putConstraint(SpringLayout.WEST, chooserLeft, 0, SpringLayout.WEST, dialogContentPane);
		dialogLayout.putConstraint(SpringLayout.EAST, chooserRight, 0, SpringLayout.EAST, dialogContentPane);
		
		//split-point
		dialogLayout.putConstraint(SpringLayout.EAST, chooserLeft, 0, SpringLayout.WEST, chooserRight); // MORE LEFT
		dialogLayout.putConstraint(SpringLayout.WEST, chooserRight, -(128 + 32), SpringLayout.EAST, dialogContentPane); // MORE RIGHT
		
	}
	
	private void buildLayoutForChooserLeftContent()
	{
		Container contentPane = chooserLeft;
		// chooserLeftLayout
		
		chooserLeftLayout.putConstraint(SpringLayout.WEST, chooserLeftColourSelector, 0, SpringLayout.WEST, contentPane);
		chooserLeftLayout.putConstraint(SpringLayout.WEST, chooserLeftColourCircle, 0, SpringLayout.WEST, contentPane);
		chooserLeftLayout.putConstraint(SpringLayout.WEST, chooserLeftColourPallete, 0, SpringLayout.WEST, contentPane);
		
		chooserLeftLayout.putConstraint(SpringLayout.EAST, chooserLeftColourSelector, 0, SpringLayout.EAST, contentPane);
		chooserLeftLayout.putConstraint(SpringLayout.EAST, chooserLeftColourCircle, 0, SpringLayout.EAST, contentPane);
		chooserLeftLayout.putConstraint(SpringLayout.EAST, chooserLeftColourPallete, 0, SpringLayout.EAST, contentPane);
		
		chooserLeftLayout.putConstraint(SpringLayout.NORTH, chooserLeftColourSelector, 0, SpringLayout.NORTH, contentPane);
		chooserLeftLayout.putConstraint(SpringLayout.SOUTH, chooserLeftColourSelector, 0, SpringLayout.NORTH, chooserLeftColourCircle);
		chooserLeftLayout.putConstraint(SpringLayout.SOUTH, chooserLeftColourSelector, 64, SpringLayout.NORTH, contentPane);
		chooserLeftLayout.putConstraint(SpringLayout.SOUTH, chooserLeftColourCircle, 0, SpringLayout.NORTH, chooserLeftColourPallete);
		chooserLeftLayout.putConstraint(SpringLayout.SOUTH, chooserLeftColourPallete, 0, SpringLayout.SOUTH, contentPane);
		
		chooserLeftLayout.putConstraint(SpringLayout.NORTH, chooserLeftColourCircle, 64, SpringLayout.NORTH, contentPane);
		
	}
	
	private void buildLayoutForChooserRightContent()
	{
		Container contentPane = chooserRight;
		// chooserRightLayout
		
		// link left springs
		chooserRightLayout.putConstraint(SpringLayout.WEST, chooserRightRGB, 0, SpringLayout.WEST, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.WEST, chooserRightHEX, 0, SpringLayout.WEST, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.WEST, chooserRightHSB, 0, SpringLayout.WEST, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.WEST, chooserRightALPHA, 0, SpringLayout.WEST, contentPane);
		
		// link right springs
		chooserRightLayout.putConstraint(SpringLayout.EAST, chooserRightRGB, 0, SpringLayout.EAST, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.EAST, chooserRightHEX, 0, SpringLayout.EAST, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.EAST, chooserRightHSB, 0, SpringLayout.EAST, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.EAST, chooserRightALPHA, 0, SpringLayout.EAST, contentPane);
		
		// link top/bottoms together (nightmare)
		chooserRightLayout.putConstraint(SpringLayout.NORTH, chooserRightRGB, 0, SpringLayout.NORTH, contentPane);
		chooserRightLayout.putConstraint(SpringLayout.NORTH, chooserRightHEX, 0, SpringLayout.SOUTH, chooserRightRGB);
		chooserRightLayout.putConstraint(SpringLayout.NORTH, chooserRightHSB, 0, SpringLayout.SOUTH, chooserRightHEX);
		chooserRightLayout.putConstraint(SpringLayout.SOUTH, chooserRightHSB, 0, SpringLayout.NORTH, chooserRightALPHA);
		chooserRightLayout.putConstraint(SpringLayout.SOUTH, chooserRightALPHA, 0, SpringLayout.SOUTH, contentPane);
		
	}
	
	private Color[] readColorPalletFromURL(URL pallet)
	{
		if(pallet == null)
			throw new IllegalArgumentException("Given URL is null!");
		
		try
		{
			Scanner sc = new Scanner(pallet.openStream());
			ArrayList<Color> colorList = new ArrayList<Color>();
			int lineCount = 0;
			
			while(sc.hasNextLine())
			{
				String line = sc.nextLine().trim();
				
				if(line.isEmpty())
					continue;
				
				if(line.startsWith(";"))
					continue;
				
				int COLOR = 0;
				try
				{
					COLOR = (int) Long.parseLong(line.toLowerCase(), 16);
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
					COLOR = Color.WHITE.getRGB();
				}
				
				colorList.add(new Color(COLOR, line.length() > 6));
				lineCount++;
				
				// We never wan't to load more than 96 colors, so we make a stop here.
				if(lineCount >= 96)
				{
					break;
				}
			}
			
			sc.close();
			
			while(colorList.size() < 96)
				colorList.add(Color.BLACK);
			
			Color[] colors = new Color[colorList.size()];
			colorList.toArray(colors);
			
			return colors;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return new Color[]{Color.BLACK, Color.WHITE};
		}
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
	
	public void updatePaintGUI()
	{
		if(initialized)
		{
			Paint.main.setLeftColour(leftColour, true);
			Paint.main.setRightColour(rightColour, true);
		}
	}
	
	public void updateAllChooserSubComponents_ColorChanged()
	{
		
		int COLOUR = getSelectedEditColor();
		
		chooserRightHEXimpl.outerColourUpdate();
		
		chooserRightRGBimplR.setGradientColors((COLOUR & 0x00FFFF) | 0xFF000000, (COLOUR & 0xFF00FFFF) | 0xFFFF0000);
		chooserRightRGBimplG.setGradientColors((COLOUR & 0xFF00FF) | 0xFF000000, (COLOUR & 0xFFFF00FF) | 0xFF00FF00);
		chooserRightRGBimplB.setGradientColors((COLOUR & 0xFFFF00) | 0xFF000000, (COLOUR & 0xFFFFFF00) | 0xFF0000FF);
		
		chooserRightHSBimplH.updateColorIntoSlider(COLOUR);
		chooserRightHSBimplS.updateColorIntoSlider(COLOUR);
		chooserRightHSBimplB.updateColorIntoSlider(COLOUR);
		
		chooserRightALPHAimpl.setGradientColors(COLOUR & 0xFFFFFF, (COLOUR & 0xFFFFFF) | 0xFF000000);
		
		updateColorLabels();
		
		chooserLeftColourSelectorEditColourSelector.repaint();
		
		dialog.getContentPane().repaint();
	}
	
	public void updateAllChooserSubComponents_EditChanged()
	{
		int COLOUR = getSelectedEditColor();
		
		int A = (COLOUR >> 24) & 0xFF;
		int R = (COLOUR >> 16) & 0xFF;
		int G = (COLOUR >> 8) & 0xFF;
		int B = (COLOUR) & 0xFF;
		
		chooserRightRGBimplR.sliderValue = (float) MathUtils.clamp((float) R / 256F, 0, 1);
		chooserRightRGBimplG.sliderValue = (float) MathUtils.clamp((float) G / 256F, 0, 1);
		chooserRightRGBimplB.sliderValue = (float) MathUtils.clamp((float) B / 256F, 0, 1);
		chooserRightALPHAimpl.sliderValue = (float) MathUtils.clamp((float) A / 256F, 0, 1);
		
		updatePaintGUI();
		updateAllChooserSubComponents_ColorChanged();
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
		updateAllChooserSubComponents_EditChanged();
	}
	
	/**
	 * Changes the 'Right' (secondary) colour to the given color.
	 * 
	 * @param packedColorARGB The colour to change to.
	 **/
	public void setRightColour(int packedColorARGB)
	{
		rightColour = packedColorARGB;
		updateAllChooserSubComponents_EditChanged();
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
	
	public void setSelectedEditColour(boolean isEditingLeft)
	{
		this.isEditingLeft = isEditingLeft;
		
		updateAllChooserSubComponents_ColorChanged();
	}
	
	public int getSelectedEditColor()
	{
		return isEditingLeft ? leftColour : rightColour;
	}
	
	public void setSelectedEditColour(int c)
	{
		if(isEditingLeft)
			leftColour = c;
		else
			rightColour = c;
		
		this.updateAllChooserSubComponents_ColorChanged();
		
		this.updatePaintGUI();
	}
	
	public void setSelectedEditColour(int c, boolean invert)
	{
		boolean flag = invert ? !isEditingLeft : isEditingLeft;
		
		if(flag)
			leftColour = c;
		else
			rightColour = c;
		
		this.updateAllChooserSubComponents_ColorChanged();
		
		this.updatePaintGUI();
	}
	
	/**
	 * Return's the actual JDialog instance that is the root for the components of the colour-chooser.<br><br>
	 * The colour-choosers JDialog should <b>never</b> be modified by another class except by itself.<br>
	 * Adding listeners to it is an exception, as they don't actually change the JDialog.<br>
	 * 
	 * @return The colour-choosers JDialog instance.
	 **/
	public WebDialog getDialog()
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
	
	// 1=S, 2=B
	private static int sliderLastChanged = -1;
	
	/*
	 * Tweak of Color.RGBtoHSB()
	 * Doesn't lock hue if saturation or brightness is zero, and doesn't lock saturation if brightness is zero.
	 */
	private float[] RGBtoHSB(int r, int g, int b, float[] hsbvals)
	{
		hsbvals = Color.RGBtoHSB(r, g, b, hsbvals);
		
		if(sliderLastChanged == 1)
			hsbvals[0] = chooserRightHSBimplH.sliderValue;
		if(sliderLastChanged == 2)
		{
			hsbvals[0] = chooserRightHSBimplH.sliderValue;
			hsbvals[1] = chooserRightHSBimplS.sliderValue;
		}
		
		return hsbvals;
	}
	
	private void updateColorLabels()
	{
		int c = getSelectedEditColor();
		((TitledBorder) chooserRightHSB.getBorder()).setTitle("HSB (" + (int) (chooserRightHSBimplH.sliderValue * 360) + ", "
				+ (int) (chooserRightHSBimplS.sliderValue * 255) + ", " + (int) (chooserRightHSBimplB.sliderValue * 255) + ")");
		((TitledBorder) chooserRightRGB.getBorder()).setTitle("RGB (" + ((c >> 16) & 0xFF) + ", " + ((c >> 8) & 0xFF) + ", " + (c & 0xFF) + ")");
		((TitledBorder) chooserRightALPHA.getBorder()).setTitle("Alpha (" + ((c >> 24) & 0xFF) + ")");
	}
}
