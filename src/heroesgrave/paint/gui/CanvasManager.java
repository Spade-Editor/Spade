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

package heroesgrave.paint.gui;

import heroesgrave.paint.main.Change;
import heroesgrave.paint.main.MultiChange;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CanvasManager
{
	
	/*
	 * Notes for the layer-system:
	 * 
	 * - Layers must be able to be disabled, deleted, moved, removed, added.
	 * - Layers must be able to change the way they are applied onto each other, also called 'Blending' or 'Image Compositing'.
	 * - Layers should be able to have a name (that can be changed).
	 *   - Layers should IGNORE their names! The names do NOT matter! Don't use them for indexing or any inter-system communications/actions.
	 *   - The identifier (position in layer list/array) is what counts, and what should be used for selecting layers!
	 * 
	 * - Layers should be their own class!
	 *   - Means: 'image' and 'preview' have to become lists of layers (Put that in a special class too?), and images consist of layers.
	 *     - Resulting classes:
	 *       - ImageLayer
	 *         {Name,ID,BlendingMode,Visible}
	 *       - LayeredImage
	 *         {List<ImageLayer>,Width,Height, etc.etc.}
	 * 
	 */
	
	
	/**
	 * The special Canvas that draws the Image.
	 **/
	private final Canvas canvas;
	
	/**
	 * The active+loaded image itself.
	 * XXX: LayerSystemModificationMark
	 **/
	private BufferedImage image;
	
	/**
	 * The preview of the image. The preview displays a 'change' before it is applied to the actual image.
	 * XXX: LayerSystemModificationMark
	 **/
	private BufferedImage preview;
	
	/**
	 * The Image used for rendering the 'Transparency' Background-Image.
	 **/
	private static BufferedImage transparenzyBG;
	
	/**
	 * XXX: LayerSystemModificationMark
	 **/
	private LinkedList<Change> changes = new LinkedList<Change>();
	
	/**
	 * XXX: LayerSystemModificationMark
	 **/
	private LinkedList<Change> reverted = new LinkedList<Change>();
	
	/**
	 * XXX: LayerSystemModificationMark
	 **/
	private LinkedList<Change> previewing = new LinkedList<Change>();
	
	/**
	 * The maximum amount of changes allowed to reside in the changes list.
	 **/
	private static final int MAX_SIZE = 2 << 21;
	
	/**
	 * ???
	 * The size of the changes list.
	 **/
	private int size;
	
	/****/
	private float zoom = 1;
	
	public CanvasManager()
	{
		// XXX: LayerSystemModificationMark (~Image Class?)
		// Create the startup Image.
		image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, 800, 600);
		g.dispose();
		
		// Load the 'transparency'-background image.
		try
		{
			transparenzyBG = ImageIO.read(this.getClass().getResource("/heroesgrave/paint/res/tbg.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			transparenzyBG = null;
		}
		
		// Crea the canvas that displays the Image.
		canvas = new Canvas(image);
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID??)
	public void clearPreview()
	{
		previewing.clear();
		canvas.setImage(image);
		canvas.repaint();
		preview = null;
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID??)
	public void preview(Change change)
	{
		if(preview == null)
		{
			preview = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			preview.getGraphics().drawImage(image, 0, 0, null);
			canvas.setImage(preview);
			canvas.repaint();
		}
		
		previewing.add(change);
		change.apply(preview);
		canvas.repaint();
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID??)
	public void applyPreview()
	{
		Change[] c = new Change[previewing.size()];
		previewing.toArray(c);
		
		addChange(new MultiChange(c));
		clearPreview();
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID)
	public void addChange(Change change)
	{
		BufferedImage nimage = change.apply(image);
		if(nimage != image)
		{
			setImage(nimage);
		}
		
		changes.add(change);
		size += change.getSize();
		canvas.repaint();
		
		if(!reverted.isEmpty())
		{
			for(Change c : reverted)
			{
				size -= c.getSize();
			}
			reverted.clear();
		}
		
		while(size > MAX_SIZE)
		{
			size -= changes.removeFirst().getSize();
		}
		Paint.main.saved = false;
	}
	
	/*
	 * (Longor1996) Small Note here:
	 * The Zoom function currently has the bug, that it zooms into the top-left corner of the image, which is WRONG.
	 * To fix this, we need to apply the cursor or half-screen-size,
	 * divided/multiplied with the zoom factor, onto the 'camera' position.
	 * 
	 * I don't know how to do it exactly, so I won't do anything with it, but it should/could work this way.
	 * 
	 */
	
	public void incZoom()
	{
		if(zoom < 1)
		{
			zoom = zoom + 1 / 10f;
		}
		else if(zoom < 32)
		{
			zoom = (int) zoom + 1;
		}
		
		canvas.setScale(zoom);
		canvas.repaint();
		canvas.revalidate();
	}
	
	public void decZoom()
	{
		if(zoom > 1)
		{
			zoom = (int) zoom - 1;
		}
		else if(zoom > 1 / 10f)
		{
			zoom = zoom - 1 / 10f;
		}
		
		canvas.setScale(zoom);
		canvas.repaint();
		canvas.revalidate();
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID)
	public void revertChange()
	{
		if(changes.isEmpty())
			return;
		Change change = changes.removeLast();
		
		BufferedImage nimage = change.revert(image);
		if(nimage != image)
		{
			setImage(nimage);
		}
		
		reverted.add(change);
		canvas.repaint();
		Paint.main.saved = false;
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID)
	public void repeatChange()
	{
		if(reverted.isEmpty())
			return;
		Change change = reverted.removeLast();
		
		BufferedImage nimage = change.apply(image);
		if(nimage != image)
		{
			setImage(nimage);
		}
		
		changes.add(change);
		canvas.repaint();
		Paint.main.saved = false;
	}
	
	// XXX: LayerSystemModificationMark (+Param:LayerID//Override?)
	public void setImage(BufferedImage image)
	{
		this.image = image;
		canvas.setImage(this.image);
		canvas.repaint();
		canvas.revalidate();
		changes.clear();
	}
	
	public JPanel getCanvas()
	{
		return canvas;
	}
	
	public float getScale()
	{
		return zoom;
	}
	
	private static class Canvas extends JPanel
	{
		private static final long serialVersionUID = 4162295507195065688L;
		
		// XXX: LayerSystemModificationMark (???)
		private BufferedImage image;
		private float scale = 1;
		private int lastButton = 0;
		
		public Canvas(BufferedImage i)
		{
			setImage(i);
			this.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					lastButton = e.getButton();
					Paint.main.currentTool.onPressed(MathUtils.floor(e.getX() / scale), MathUtils.floor(e.getY() / scale), e.getButton());
				}
				
				public void mouseReleased(MouseEvent e)
				{
					lastButton = e.getButton();
					Paint.main.currentTool.onReleased(MathUtils.floor(e.getX() / scale), MathUtils.floor(e.getY() / scale), e.getButton());
				}
			});
			this.addMouseMotionListener(new MouseMotionListener()
			{
				public void mouseDragged(MouseEvent e)
				{
					Paint.main.currentTool.whilePressed(MathUtils.floor(e.getX() / scale), MathUtils.floor(e.getY() / scale), lastButton);
				}
				
				public void mouseMoved(MouseEvent e)
				{
					Paint.main.currentTool.whileReleased(MathUtils.floor(e.getX() / scale), MathUtils.floor(e.getY() / scale), lastButton);
				}
			});
		}
		
		public void setScale(float scale)
		{
			this.scale = scale;
			this.setPreferredSize(new Dimension(MathUtils.floor(image.getWidth() * scale), MathUtils.floor(image.getHeight() * scale)));
		}
		
		// XXX: LayerSystemModificationMark (+Param:LayerID)
		public void setImage(BufferedImage image)
		{
			this.image = image;
			this.setPreferredSize(new Dimension(MathUtils.floor(image.getWidth() * scale), MathUtils.floor(image.getHeight() * scale)));
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			
			// Draw the 'transparency' background.
			g2d.setPaint(new TexturePaint(transparenzyBG, new Rectangle2D.Float(0, 0, 16, 16)));
			g2d.fillRect(0, 0, MathUtils.floor(image.getWidth() * scale), MathUtils.floor(image.getHeight() * scale));
			
			// XXX: LayerSystemModificationMark (~Layered Rendering)
			// Draw the actual Image
			g2d.setPaint(null);
			g2d.drawImage(image, 0, 0, MathUtils.floor(image.getWidth() * scale), MathUtils.floor(image.getHeight() * scale), null);
			
			// if the Pixel-Grid is active, draw it.
			if(Menu.GRID_ENABLED && scale >= 4)
			{
				g2d.setColor(Color.gray);
				for(int i = 0; i < image.getWidth(); i++)
				{
					g2d.drawLine(MathUtils.floor(i * scale), 0, MathUtils.floor(i * scale), MathUtils.floor(image.getHeight() * scale));
				}
				for(int j = 0; j < image.getHeight(); j++)
				{
					g2d.drawLine(0, MathUtils.floor(j * scale), MathUtils.floor(image.getWidth() * scale), MathUtils.floor(j * scale));
				}
			}
		}
	}
	
	/**
	 * Returns the Image.
	 * XXX: LayerSystemModificationMark (+Param:LayerID)
	 **/
	public BufferedImage getImage()
	{
		return image;
	}
}