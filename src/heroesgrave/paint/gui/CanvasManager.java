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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;

public class CanvasManager
{
	private final Canvas canvas;
	private BufferedImage image, preview;
	private LinkedList<Change> changes = new LinkedList<Change>();
	private LinkedList<Change> reverted = new LinkedList<Change>();
	private LinkedList<Change> buffering = new LinkedList<Change>();
	private LinkedList<Change> previewing = new LinkedList<Change>();
	private int size;

	private static final int MAX_SIZE = 2 << 21;

	private float zoom = 1;

	public CanvasManager()
	{
	    initImage();
	    
		canvas = new Canvas(image);
	}

	private void initImage() {
        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, 800, 600);
        g.dispose();
	}
	public void clearPreview()
	{
		previewing.clear();
		canvas.setImage(image);
		canvas.repaint();
		preview = null;
	}

	public void preview(Change change)
	{
//		if(preview == null)
//		{
			preview = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			preview.getGraphics().drawImage(image, 0, 0, null);
			canvas.setImage(preview);
			canvas.repaint();
//		}
		
		if(!previewing.contains(change)) {
		    previewing.add(change);
		}
		change.apply(preview);
		canvas.repaint();
	}

	public void applyPreview()
	{
		Change[] c = new Change[previewing.size()];
		previewing.toArray(c);

		addChange(new MultiChange(c));
		clearPreview();
	}

	public void addChange(Change change)
	{
		BufferedImage nimage = change.apply(image);
		if(nimage != image)
		{
			setImage(nimage);
		}

		if(!changes.contains(change)) {
    		changes.add(change);
    		size += change.getSize();
		}
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

	public void bufferChange(Change change)
	{
		change.apply(image);
		if(!buffering.contains(change)) {
		    buffering.add(change);
		}
		canvas.repaint();
	}

	public void flushChanges()
	{
		Change[] c = new Change[buffering.size()];
		buffering.toArray(c);

		MultiChange bc = new MultiChange(c);
		changes.add(bc);
		size += bc.getSize();

		buffering.clear();

		if(!reverted.isEmpty())
		{
			for(Change ch : reverted)
			{
				size -= ch.getSize();
			}
			reverted.clear();
		}

		while(size > MAX_SIZE)
		{
			size -= changes.removeFirst().getSize();
		}
		Paint.main.saved = false;
	}

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

	public void revertChange()
	{
		if(changes.isEmpty())
			return;
		Change change = changes.removeLast();
        reverted.add(change);

		initImage();
		
		for(Change c:changes) {
		    c.apply(image);
		}
		setImage(image);
		canvas.repaint();
		Paint.main.saved = false;
	}

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

	public void setImage(BufferedImage image)
	{
		this.image = image;
		canvas.setImage(this.image);
		canvas.repaint();
		canvas.revalidate();
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

		public void setImage(BufferedImage image)
		{
			this.image = image;
			this.setPreferredSize(new Dimension(MathUtils.floor(image.getWidth() * scale), MathUtils.floor(image.getHeight() * scale)));
		}

		public void paint(Graphics g)
		{
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(image, 0, 0, MathUtils.floor(image.getWidth() * scale), MathUtils.floor(image.getHeight() * scale), null);
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

	public BufferedImage getImage()
	{
		return image;
	}
}