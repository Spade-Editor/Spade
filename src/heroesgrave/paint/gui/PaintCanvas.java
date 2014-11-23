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
import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.image.change.IChange;
import heroesgrave.paint.image.change.IEditChange;
import heroesgrave.paint.image.change.IImageChange;
import heroesgrave.paint.image.change.IMaskChange;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Handles:
 * - Handling of the Camera
 * - Drawing of any given Image.
 **/
@SuppressWarnings("serial")
public class PaintCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener
{
	public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
	public static final int SELECTION_OVERLAY = 0x3f3f5f7f;
	public static BufferedImage backgroundLight, backgroundDark;
	
	// MISC
	final JFrame mainframe;
	BufferedImage image;
	
	// Camera
	float cam_zoom;
	float cam_positionX;
	float cam_positionY;
	
	// Controls
	int mouseLastDragPosX;
	int mouseLastDragPosY;
	int lastButton;
	
	// Paint
	Rectangle2D backgroundRectangle;
	TexturePaint paintLight, paintDark;
	
	// Document rendering stuff 
	private BufferedImage frozen, unselected, preview;
	private RawImage unselectedRaw, previewRaw;
	private boolean maskChanged;
	private int frozenTo;
	
	// Document
	Document document;
	
	static
	{
		try
		{
			backgroundLight = ImageIO.read(PaintCanvas.class.getResource("/res/tbg.png"));
			
			backgroundDark = ImageIO.read(PaintCanvas.class.getResource("/res/tbgd.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public PaintCanvas(JFrame mainframe)
	{
		this.mainframe = mainframe;
		
		this.setMinimumSize(new Dimension(8, 8));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		
		this.cam_zoom = 1;
		this.cam_positionX = 0;
		this.cam_positionY = 0;
		
		this.updateBG();
		
		this.mouseLastDragPosX = 0;
		this.mouseLastDragPosY = 0;
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
	
	public float getScale()
	{
		return cam_zoom;
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(document == null)
			return;
		if((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON2))
		{
			this.cam_zoom = 1;
			this.cam_positionX = image.getWidth() / 2;
			this.cam_positionY = image.getHeight() / 2;
			this.repaint();
			this.updateBG();
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(document == null)
			return;
		// Don't use BUTTON2_MASK because it will trigger when ALT is pressed. Sometimes Java is stupid.
		int middleMouseMod = MouseEvent.BUTTON2_DOWN_MASK;
		int modifier = e.getModifiersEx();
		boolean middleMouseDown = (modifier & middleMouseMod) != 0;
		boolean returned = true;
		
		if(middleMouseDown && returned)
		{
			float dragX = e.getX() - this.mouseLastDragPosX;
			float dragY = e.getY() - this.mouseLastDragPosY;
			
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
			Paint.main.currentTool.whilePressed(document.getCurrent(), x, y, lastButton);
		}
		
		this.mouseLastDragPosX = e.getX();
		this.mouseLastDragPosY = e.getY();
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
	public void mouseMoved(MouseEvent e)
	{
		if(document == null)
			return;
		if(lastButton == MouseEvent.BUTTON1 || lastButton == MouseEvent.BUTTON3)
		{
			Point _p = e.getPoint();
			Point2D p = this.transformCanvasPointToImagePoint(new Point2D.Float(_p.x, _p.y));
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.whileReleased(document.getCurrent(), x, y, lastButton);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		this.mouseLastDragPosX = e.getX();
		this.mouseLastDragPosY = e.getY();
		lastButton = e.getButton();
		
		if(document == null)
			return;
		if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
		{
			Point _p = e.getPoint();
			Point2D p = this.transformCanvasPointToImagePoint(new Point2D.Float(_p.x, _p.y));
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.onPressed(document.getCurrent(), x, y, e.getButton());
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(document == null)
			return;
		if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
		{
			Point _p = e.getPoint();
			Point2D p = this.transformCanvasPointToImagePoint(new Point2D.Float(_p.x, _p.y));
			short x = (short) p.getX();
			short y = (short) p.getY();
			Paint.main.currentTool.onReleased(document.getCurrent(), x, y, e.getButton());
		}
		lastButton = 0;
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
	public void paint(Graphics _g)
	{
		Graphics2D g = (Graphics2D) _g;
		
		if(Menu.DARK_BACKGROUND)
		{
			g.setColor(new Color(0x1f1f1f));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		if(image == null)
			return;
		
		AffineTransform Tx = new AffineTransform();
		Tx.translate(this.getWidth() / 2, this.getHeight() / 2);
		Tx.scale(this.cam_zoom, this.cam_zoom);
		Tx.translate(-this.cam_positionX, -this.cam_positionY);
		
		g.transform(Tx);
		if(Menu.DARK_BACKGROUND)
		{
			g.setPaint(paintDark);
		}
		else
		{
			g.setPaint(paintLight);
		}
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		if(document != null && document.repaint)
		{
			ArrayList<Layer> flatmap = document.getFlatMap();
			IChange previewChange = document.getPreview();
			
			long start, end;
			start = System.nanoTime();
			
			// Create graphics and clear image.
			Graphics2D cg = image.createGraphics();
			cg.setBackground(PaintCanvas.TRANSPARENT);
			cg.clearRect(0, 0, image.getWidth(), image.getHeight());
			
			int index = flatmap.indexOf(document.getCurrent()) - 1;
			if(index >= 0)
			{
				if(Math.min(index, document.lowestChange) < frozenTo)
				{
					document.lowestChange = frozenTo = index;
					Graphics2D fg = frozen.createGraphics();
					fg.setBackground(PaintCanvas.TRANSPARENT);
					fg.clearRect(0, 0, document.getWidth(), document.getHeight());
					for(int i = 0; i <= frozenTo; i++)
					{
						flatmap.get(i).render(fg);
					}
				}
				else if(index > frozenTo)
				{
					Graphics2D fg = frozen.createGraphics();
					for(int i = frozenTo + 1; i <= index; i++)
					{
						flatmap.get(i).render(fg);
					}
					document.lowestChange = frozenTo = index;
				}
				cg.drawImage(frozen, 0, 0, null);
			}
			else
			{
				frozenTo = -1;
				Graphics2D fg = frozen.createGraphics();
				fg.setBackground(PaintCanvas.TRANSPARENT);
				fg.clearRect(0, 0, document.getWidth(), document.getHeight());
			}
			
			boolean masked = true;
			
			if(previewChange != null)
			{
				previewRaw.copyFrom(document.getCurrent().getImage(), true);
				
				if(previewChange instanceof IEditChange)
				{
					((IEditChange) previewChange).apply(previewRaw);
					maskChanged = maskChanged || (previewChange instanceof IMaskChange);
				}
				else if(previewChange instanceof IImageChange)
				{
					previewRaw.copyFrom(((IImageChange) previewChange).apply(previewRaw), true);
				}
				
				if(previewRaw.isMaskEnabled())
				{
					if(maskChanged)
					{
						unselectedRaw.setMask(previewRaw.borrowMask());
						unselectedRaw.clear(SELECTION_OVERLAY);
						unselectedRaw.fill(0);
						maskChanged = false;
					}
				}
				else
				{
					masked = false;
				}
				
				// Draw Preview
				cg.setComposite(document.getCurrent().getBlendMode());
				cg.drawImage(preview, 0, 0, null);
			}
			else
			{
				// Render Current Layer
				flatmap.get(index + 1).render(cg);
				
				RawImage current = document.getCurrent().getImage();
				
				if(current.isMaskEnabled())
				{
					if(maskChanged)
					{
						unselectedRaw.setMask(current.borrowMask());
						unselectedRaw.clear(SELECTION_OVERLAY);
						unselectedRaw.fill(0);
						maskChanged = false;
					}
				}
				else
				{
					masked = false;
				}
			}
			
			if(masked) // Draw the selection overlay.
			{
				cg.setComposite(BlendMode.NORMAL);
				cg.drawImage(unselected, 0, 0, null);
			}
			
			for(int i = index + 2; i < flatmap.size(); i++)
			{
				flatmap.get(i).render(cg);
			}
			
			end = System.nanoTime();
			if(Paint.debug)
				System.out.printf("Render Time: %dms\n", (end - start) / 1000000);
			document.repaint = false;
		}
		
		g.drawImage(image, 0, 0, null);
		/*
		if(Menu.GRID_ENABLED && cam_zoom >= 4)
		{
			Tx = new AffineTransform();
			Tx.scale(1f / this.cam_zoom, 1f / this.cam_zoom);
			g.transform(Tx);
			g.setColor(Color.gray);
			final int top = MathUtils.floor(ty * cam_zoom);
			final int bottom = MathUtils.floor((ty + image.getHeight()) * cam_zoom);
			final int left = MathUtils.floor(tx * cam_zoom);
			final int right = MathUtils.floor((tx + image.getWidth()) * cam_zoom);
			for(int i = 0; i < image.getWidth(); i++)
			{
				int ix = MathUtils.floor((tx + i) * cam_zoom);
				g.drawLine(ix, top, ix, bottom);
			}
			for(int j = 0; j < image.getHeight(); j++)
			{
				int iy = MathUtils.floor((ty + j) * cam_zoom);
				g.drawLine(left, iy, right, iy);
			}
		}
		*/
	}
	
	public void maskChanged()
	{
		this.maskChanged = true;
	}
	
	public void resized(int width, int height)
	{
		this.image = document.getRenderedImage();
		this.frozen = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.unselected = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.unselectedRaw = RawImage.unwrapBufferedImage(unselected);
		this.preview = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.previewRaw = RawImage.unwrapBufferedImage(preview);
		this.cam_zoom = 1;
		this.cam_positionX = image.getWidth() / 2;
		this.cam_positionY = image.getHeight() / 2;
		this.repaint();
	}
	
	public void setDocument(Document document)
	{
		this.document = document;
		if(document == null)
		{
			unselectedRaw = previewRaw = null;
			unselected = preview = frozen = image = null;
			this.cam_positionX = 0;
			this.cam_positionY = 0;
		}
		else
		{
			this.image = document.getRenderedImage();
			this.frozen = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.unselected = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.unselectedRaw = RawImage.unwrapBufferedImage(unselected);
			this.preview = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.previewRaw = RawImage.unwrapBufferedImage(preview);
			this.cam_positionX = image.getWidth() / 2;
			this.cam_positionY = image.getHeight() / 2;
		}
		this.repaint();
	}
	
	public void setScale(float scale)
	{
		this.cam_zoom = scale;
		this.updateBG();
		this.repaint();
	}
	
	public final Point2D.Float transformCanvasPointToImagePoint(Point2D in)
	{
		AffineTransform Tx = new AffineTransform();
		Tx.translate(this.getWidth() / 2, this.getHeight() / 2);
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
		
		return (Point2D.Float) Tx.transform(in, null);
	}
	
	private void updateBG()
	{
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 16f / cam_zoom, 16f / cam_zoom);
		paintLight = new TexturePaint(backgroundLight, rect);
		paintDark = new TexturePaint(backgroundDark, rect);
	}
}
