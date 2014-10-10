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

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.alee.laf.rootpane.WebFrame;

/**
 * Handles:
 * - Handling of the Camera
 * - Drawing of any given Image.
 **/
@SuppressWarnings("serial")
public class PaintCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener,
		KeyListener
{
	public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
	public static BufferedImage backgroundLight, backgroundDark;
	
	// MISC
	final WebFrame mainframe;
	final float maxDegreeOfRotation = 45F / 4F;
	BufferedImage image;
	
	// DEBUG
	NumberFormat numberFormat;
	
	// Camera
	float cam_zoom;
	float cam_rotation;
	float cam_positionX;
	float cam_positionY;
	
	// Controls
	boolean isSpacebarDown;
	int mouseLastDragPosX;
	int mouseLastDragPosY;
	int lastButton;
	
	// Paint
	Rectangle2D backgroundRectangle;
	TexturePaint paintLight, paintDark;
	
	// Document
	Document document;
	
	public PaintCanvas(WebFrame mainframe)
	{
		this.mainframe = mainframe;
		
		this.setMinimumSize(new Dimension(8, 8));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		
		this.numberFormat = NumberFormat.getInstance();
		{
			this.numberFormat.setMaximumFractionDigits(4);
			this.numberFormat.setMinimumFractionDigits(4);
			this.numberFormat.setGroupingUsed(true);
			this.numberFormat.setMinimumIntegerDigits(8);
			this.numberFormat.setMaximumIntegerDigits(8);
		}
		
		this.cam_zoom = 1;
		this.cam_rotation = 0;
		this.cam_positionX = 0;
		this.cam_positionY = 0;
		
		this.updateBG();
		
		this.isSpacebarDown = false;
		this.mouseLastDragPosX = 0;
		this.mouseLastDragPosY = 0;
	}
	
	public void setDocument(Document document)
	{
		this.document = document;
		this.image = document.getRenderedImage();
		this.cam_zoom = 1;
		this.cam_rotation = 0;
		this.cam_positionX = image.getWidth() / 2;
		this.cam_positionY = image.getHeight() / 2;
		this.repaint();
	}
	
	/*
	public void setImage(BufferedImage image)
	{
		this.image = image;
		this.cam_zoom = 1;
		this.cam_rotation = 0;
		this.cam_positionX = image.getWidth() / 2;
		this.cam_positionY = image.getHeight() / 2;
		
		this.updateBackground();
		this.repaint();
	}

	public void setImageContent(int[] rawARGB)
	{
		int width = this.image.getWidth();
		int height = this.image.getHeight();
		int surface = width * height;
		
		if(surface != rawARGB.length)
		{
			throw new RuntimeException(
					"Given ARGB-array is not equal in surface area compared to current image. Unable to replace contents!");
		}
		
		this.image.setRGB(0, 0, width, height, rawARGB, 0, width);
		
		// full update
		this.updateBackground();
		this.repaint();
	}
	*/
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		// clear background
		g2d.setColor(Color.BLACK);
		//g2d.setPaint(this.backgroundPaint);
		if(Menu.DARK_BACKGROUND)
		{
			g.setColor(new Color(0x0f171f));
		}
		else
		{
			g.setColor(new Color(0xDDEEFF));
		}
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// rendering hints for image drawing
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		AffineTransform Tx = new AffineTransform();
		Tx.translate(this.getWidth() / 2, this.getHeight() / 2);
		Tx.scale(this.cam_zoom, this.cam_zoom);
		Tx.rotate(this.cam_rotation);
		Tx.translate(-this.cam_positionX, -this.cam_positionY);
		
		g2d.transform(Tx);
		if(Menu.DARK_BACKGROUND)
		{
			g2d.setPaint(paintDark);
		}
		else
		{
			g2d.setPaint(paintLight);
		}
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		if(document != null)
		{
			Layer layer = document.getRoot();
			if(layer.isChanged() || layer.childChanged())
			{
				Graphics2D cg = image.createGraphics();
				cg.setBackground(TRANSPARENT);
				cg.clearRect(0, 0, image.getWidth(), image.getHeight());
				layer.render(cg);
			}
		}
		
		g2d.drawImage(this.image, 0, 0, null);
		
		// System.out.println("repainted canvas");
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON2))
		{
			this.cam_zoom = 1;
			this.cam_rotation = 0;
			this.repaint();
			this.updateBG();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		this.mouseLastDragPosX = e.getX();
		this.mouseLastDragPosY = e.getY();
		lastButton = e.getButton();
		
		if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
		{
			Point2D p = this.transformCanvasPointToImagePoint(e.getPoint());
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.onPressed(document.getLayer(), x, y, e.getButton());
		}
	}
	
	public final Point2D.Float transformCanvasPointToImagePoint(Point2D in)
	{
		AffineTransform Tx = new AffineTransform();
		Tx.translate(this.getWidth() / 2, this.getHeight() / 2);
		Tx.rotate(this.cam_rotation);
		Tx.scale(this.cam_zoom, this.cam_zoom);
		Tx.translate(-this.cam_positionX, -this.cam_positionY);
		
		try
		{
			Tx.invert();
		}
		catch(NoninvertibleTransformException e1)
		{
			e1.printStackTrace();
		}
		
		return (Float) Tx.transform(in, null);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
		{
			Point2D p = this.transformCanvasPointToImagePoint(e.getPoint());
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.onReleased(document.getLayer(), x, y, e.getButton());
		}
		lastButton = 0;
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int ctrlMod = MouseWheelEvent.CTRL_MASK | MouseWheelEvent.CTRL_DOWN_MASK;
		int modifier = e.getModifiers();
		boolean ctrlDown = (modifier & ctrlMod) != 0;
		
		if(ctrlDown)
		{
			int sign = e.getWheelRotation();
			
			if(sign < 0)
			{
				this.cam_zoom_increase();
				return;
			}
			
			if(sign > 0)
			{
				this.cam_zoom_decrease();
				return;
			}
		}
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		int ctrlMod = MouseWheelEvent.CTRL_MASK | MouseWheelEvent.CTRL_DOWN_MASK;
		int middleMouseMod = MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON2_MASK;
		int modifier = e.getModifiers();
		boolean ctrlDown = (modifier & ctrlMod) != 0;
		boolean middleMouseDown = (modifier & middleMouseMod) != 0;
		boolean returned = true;
		
		if(ctrlDown && returned)
		{
			float mouseX = e.getX();
			float mouseY = e.getY();
			
			// screen center
			mouseX -= this.getWidth() / 2;
			mouseY -= this.getHeight() / 2;
			
			float mouseLength = (float) Math.sqrt((mouseX * mouseX) + (mouseY * mouseY));
			mouseX /= mouseLength;
			mouseY /= mouseLength;
			
			float rotation = (float) Math.atan2(mouseY, mouseX);
			
			if(e.isShiftDown())
			{
				rotation = (float) Math.toDegrees(rotation);
				rotation /= this.maxDegreeOfRotation;
				rotation = Math.round(rotation);
				rotation *= this.maxDegreeOfRotation;
				rotation = (float) Math.toRadians(rotation);
			}
			
			this.cam_rotation = rotation;
			this.repaint();
			returned = false;
		}
		
		if(middleMouseDown && returned)
		{
			float dragX = e.getX() - this.mouseLastDragPosX;
			float dragY = e.getY() - this.mouseLastDragPosY;
			
			float ROT = -this.cam_rotation;
			float new_dragX = (float) ((dragX * Math.cos(ROT)) - (dragY * Math.sin(ROT)));
			float new_dragY = (float) ((dragX * Math.sin(ROT)) + (dragY * Math.cos(ROT)));
			
			dragX = new_dragX;
			dragY = new_dragY;
			
			this.cam_positionX -= dragX / this.cam_zoom;
			this.cam_positionY -= dragY / this.cam_zoom;
			
			if(this.cam_positionX < 0)
			{
				this.cam_positionX = 0;
			}
			if(this.cam_positionY < 0)
			{
				this.cam_positionY = 0;
			}
			
			if(this.cam_positionX > this.image.getWidth())
			{
				this.cam_positionX = this.image.getWidth();
			}
			if(this.cam_positionY > this.image.getHeight())
			{
				this.cam_positionY = this.image.getHeight();
			}
			
			//this.updateBackground();
			this.repaint();
			returned = false;
		}
		
		if(returned && (lastButton == MouseEvent.BUTTON1 || lastButton == MouseEvent.BUTTON3))
		{
			Point2D p = this.transformCanvasPointToImagePoint(e.getPoint());
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.whilePressed(document.getLayer(), x, y, lastButton);
		}
		
		this.mouseLastDragPosX = e.getX();
		this.mouseLastDragPosY = e.getY();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(lastButton == MouseEvent.BUTTON1 || lastButton == MouseEvent.BUTTON3)
		{
			Point2D p = this.transformCanvasPointToImagePoint(e.getPoint());
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.whileReleased(document.getLayer(), x, y, lastButton);
		}
	}
	
	public void cam_zoom_increase()
	{
		float scale = this.cam_zoom;
		
		if(scale < 64f)
		{
			scale /= 0.95f;
		}
		
		// Keep the scale clean
		if(scale > 1f)
		{
			scale = MathUtils.ceil(scale);
		}
		else if(scale < 1f)
		{
			scale = 1f / MathUtils.floor(1f / scale);
		}
		
		this.setScale(scale);
	}
	
	public void cam_zoom_decrease()
	{
		float scale = this.cam_zoom;
		
		if(scale > 1 / 32f)
		{
			scale *= 0.95f;
		}
		
		// Keep the scale clean
		if(scale > 1f)
		{
			scale = MathUtils.floor(scale);
		}
		else if(scale < 1f)
		{
			scale = 1f / MathUtils.ceil(1f / scale);
		}
		
		this.setScale(scale);
	}
	
	public void setScale(float scale)
	{
		this.cam_zoom = scale;
		this.updateBG();
		this.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_SPACE)
		{
			this.isSpacebarDown = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_SPACE)
		{
			this.isSpacebarDown = false;
		}
	}
	
	private void updateBG()
	{
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 16f / cam_zoom, 16f / cam_zoom);
		paintLight = new TexturePaint(backgroundLight, rect);
		paintDark = new TexturePaint(backgroundDark, rect);
	}
	
	static
	{
		try
		{
			backgroundLight = ImageIO.read(PaintCanvas.class.getResource("/heroesgrave/paint/res/tbg.png"));
			
			backgroundDark = ImageIO.read(PaintCanvas.class.getResource("/heroesgrave/paint/res/tbgd.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public float getScale()
	{
		return cam_zoom;
	}
}
