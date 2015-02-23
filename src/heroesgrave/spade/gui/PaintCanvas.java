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

package heroesgrave.spade.gui;

import heroesgrave.spade.gui.menus.Menu;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.blend.BlendMode;
import heroesgrave.spade.image.change.IChange;
import heroesgrave.spade.image.change.IEditChange;
import heroesgrave.spade.image.change.IImageChange;
import heroesgrave.spade.image.change.IMaskChange;
import heroesgrave.spade.main.Spade;
import heroesgrave.utils.math.MathUtils;

import java.awt.BasicStroke;
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
	int mouseX, mouseY, mouseLastDragPosX, mouseLastDragPosY;
	int lastButton;
	
	// Paint
	Rectangle2D backgroundRectangle;
	TexturePaint paintLight, paintDark;
	
	// Document rendering stuff 
	private BufferedImage frozen, unselected, preview, cachedTBG;
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
			mouseX = x;
			mouseY = y;
			Spade.main.gui.checkDynamicInfo();
			Spade.main.currentTool.whilePressed(document.getCurrent(), x, y, lastButton);
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
		Point _p = e.getPoint();
		Point2D p = this.transformCanvasPointToImagePoint(new Point2D.Float(_p.x, _p.y));
		short x = (short) p.getX();
		short y = (short) p.getY();
		mouseX = x;
		mouseY = y;
		Spade.main.gui.checkDynamicInfo();
		Spade.main.currentTool.whileReleased(document.getCurrent(), x, y, lastButton);
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
			mouseX = x;
			mouseY = y;
			Spade.main.gui.checkDynamicInfo();
			Spade.main.currentTool.onPressed(document.getCurrent(), x, y, e.getButton());
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
			mouseX = x;
			mouseY = y;
			Spade.main.gui.checkDynamicInfo();
			Spade.main.currentTool.onReleased(document.getCurrent(), x, y, e.getButton());
		}
		lastButton = 0;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
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
	
	@Override
	public void paint(Graphics _g)
	{
		Graphics2D g = (Graphics2D) _g;
		
		if(Menu.DARK_BACKGROUND)
		{
			g.setColor(new Color(0x1f1f1f));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		if(document == null)
			return;
		
		if(document.repaint)
			composeImage();
		
		renderImage(g);

		// Optionally draw the grid
		if(Menu.GRID_ENABLED && cam_zoom >= 8)
		{
			drawGrid(g);
		}
	}
	
	// Could probably be split up further, but I'll leave as-is for now.
	private void composeImage()
	{
		ArrayList<Layer> flatmap = document.getFlatMap();
		IChange previewChange = document.getPreview();
		
		// Create graphics and clear image.
		Graphics2D cg = image.createGraphics();
		cg.setBackground(PaintCanvas.TRANSPARENT);
		cg.clearRect(0, 0, image.getWidth(), image.getHeight());
		
		int index = flatmap.indexOf(document.getCurrent()) - 1;
		if(index >= 0)
		{
			if(Math.min(index, document.lowestChange - 1) < frozenTo)
			{
				Graphics2D fg = frozen.createGraphics();
				fg.setBackground(PaintCanvas.TRANSPARENT);
				fg.clearRect(0, 0, document.getWidth(), document.getHeight());
				for(int i = 0; i <= index; i++)
				{
					flatmap.get(i).render(fg);
				}
				document.lowestChange = frozenTo = index;
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
		
		cg.dispose();
		document.repaint = false;
	}
	
	private void renderImage(Graphics2D g)
	{
		int translate_x = MathUtils.floor((-this.cam_positionX * this.cam_zoom) + this.getWidth()/2);
		int translate_y = MathUtils.floor((-this.cam_positionY * this.cam_zoom) + this.getHeight()/2);
		int width = MathUtils.floor(image.getWidth()*this.cam_zoom);
		int height = MathUtils.floor(image.getHeight()*this.cam_zoom);
		
		validateTBG();
		
		int left = Math.max(translate_x, 0);
		int top = Math.max(translate_y, 0);
		
		g.drawImage(cachedTBG, left, top, cachedTBG.getWidth(), cachedTBG.getHeight(), null);
		g.drawImage(image, translate_x, translate_y, width, height, null);
	}
	
	public void drawGrid(Graphics2D g)
	{
		int translate_x = MathUtils.floor((-this.cam_positionX * this.cam_zoom) + this.getWidth()/2);
		int translate_y = MathUtils.floor((-this.cam_positionY * this.cam_zoom) + this.getHeight()/2);
		
		int step = MathUtils.floor(this.cam_zoom);
		int tx = MathUtils.floor(this.cam_positionX * this.cam_zoom);
		int ty = MathUtils.floor(this.cam_positionY * this.cam_zoom);
		
		int top = Math.max(ty - this.getHeight() / 2, 0) / step * step;
		int bottom = Math.min(ty + this.getHeight() / 2, image.getHeight() * step);
		
		int left = Math.max(tx - this.getWidth() / 2, 0) / step * step;
		int right = Math.min(tx + this.getWidth() / 2, image.getWidth() * step);
		
		g.setColor(Color.gray);
		if(cam_zoom > 32)
			g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{cam_zoom * 0.25f, cam_zoom * 0.25f},
					cam_zoom * 0.125f));
		// Vertical
		for(int i = left; i < right; i += step)
		{
			g.drawLine(translate_x+i, translate_y+top, translate_x+i, translate_y+bottom);
		}
		// Horizontal
		for(int i = top; i < bottom; i += step)
		{
			g.drawLine(translate_x+left, translate_y+i, translate_x+right, translate_y+i);
		}
	}
	
	public void maskChanged()
	{
		this.maskChanged = true;
	}
	
	public void validateTBG()
	{
		int targetWidth = Math.min(MathUtils.floor(image.getWidth()*cam_zoom), this.getWidth());
		int targetHeight = Math.min(MathUtils.floor(image.getHeight()*cam_zoom), this.getHeight());
		
		if(cachedTBG != null && cachedTBG.getWidth() == targetWidth && cachedTBG.getHeight() == targetHeight)
			return;
		
		this.cachedTBG = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = cachedTBG.createGraphics();
		g.scale(cam_zoom, cam_zoom);
		if(Menu.DARK_BACKGROUND)
		{
			g.setPaint(paintDark);
		}
		else
		{
			g.setPaint(paintLight);
		}
		g.fillRect(0, 0, cachedTBG.getWidth(), cachedTBG.getHeight());
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
		Spade.main.gui.info.setSize(width, height);
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
			Spade.main.gui.checkDynamicInfo();
		}
		this.repaint();
	}
	
	public void setScale(float scale)
	{
		this.cam_zoom = scale;
		this.updateBG();
		this.repaint();
		Spade.main.gui.checkDynamicInfo();
	}
	
	public final Point2D.Float transformCanvasPointToImagePoint(Point2D in)
	{
		float x = (float) in.getX();
		float y = (float) in.getY();
		x -= this.getWidth()/2;
		y -= this.getHeight()/2;
		x /= this.cam_zoom;
		y /= this.cam_zoom;
		x += this.cam_positionX;
		y += this.cam_positionY;
		
		return new Point2D.Float(x, y);
	}

	public final Point2D.Float transformImagePointToCanvasPoint(Point2D in)
	{
		float x = (float) in.getX();
		float y = (float) in.getY();
		x -= this.cam_positionX;
		y -= this.cam_positionY;
		x *= this.cam_zoom;
		y *= this.cam_zoom;
		x += this.getWidth()/2;
		y += this.getHeight()/2;
		
		return new Point2D.Float(x, y);
	}
	
	private void updateBG()
	{
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 16f / cam_zoom, 16f / cam_zoom);
		paintLight = new TexturePaint(backgroundLight, rect);
		paintDark = new TexturePaint(backgroundDark, rect);
	}
}
