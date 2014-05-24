package heroesgrave.paint.gui;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.math.MathUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class Renderer extends JPanel
{
	public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
	public static BufferedImage transparencyBG;
	public static BufferedImage transparencyBGDark;
	
	private Document doc;
	private TexturePaint bgPaint;
	private BufferedImage image;
	private float scale = 1f;
	private int width, height;
	
	public Renderer(Document doc)
	{
		this.doc = doc;
		this.revalidateBG();
		this.image =
				new BufferedImage(doc.getWidth(), doc.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
	}
	
	public Document getDocument()
	{
		return this.doc;
	}
	
	public void revalidateBG()
	{
		this.width = MathUtils.floor(doc.getWidth() * scale);
		this.height = MathUtils.floor(doc.getHeight() * scale);
		this.setPreferredSize(new Dimension(width, height));
		bgPaint =
				new TexturePaint(Menu.DARK_BACKGROUND	? transparencyBGDark
														: transparencyBG,
						new Rectangle2D.Float(0, 0, 16, 16));
		this.repaint();
	}
	
	public void paint(Graphics $g)
	{
		Graphics2D g = (Graphics2D) $g;
		
		g.setPaint(bgPaint);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setPaint(null);
		
		// We have to create an intermediate image because the panel doesn't support compositing.
		// This chunk is not otherwise neccessary, as the root layer itself handles whether or
		// not it needs to redraw.
		Layer layer = doc.getRoot();
		if(layer.isChanged() || layer.childChanged())
		{
			Graphics2D cg = image.createGraphics();
			cg.setBackground(TRANSPARENT);
			cg.clearRect(0, 0, this.getWidth(), this.getHeight());
			layer.render(cg);
		}
		g.drawImage(image, 0, 0, width, height, null);
		
		if(Menu.GRID_ENABLED && scale >= 4)
		{
			g.setColor(Color.gray);
			for(int i = 0; i < doc.getWidth(); i++)
			{
				g.drawLine(MathUtils.floor(i * scale), 0,
						MathUtils.floor(i * scale),
						MathUtils.floor(doc.getHeight() * scale));
			}
			for(int j = 0; j < doc.getHeight(); j++)
			{
				g.drawLine(0, MathUtils.floor(j * scale),
						MathUtils.floor(doc.getWidth() * scale),
						MathUtils.floor(j * scale));
			}
		}
	}
	
	static
	{
		try
		{
			transparencyBG =
					ImageIO.read(Renderer.class
							.getResource("/heroesgrave/paint/res/tbg.png"));
			
			transparencyBGDark =
					ImageIO.read(Renderer.class
							.getResource("/heroesgrave/paint/res/tbgd.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			transparencyBG = null;
		}
	}
	
	public void incZoom()
	{
		float oldScale = scale;
		
		JScrollPane pane = Paint.main.gui.scroll;
		
		if(scale < 1)
		{
			scale = scale + 1 / 10f;
		}
		else if(scale < 32)
		{
			scale = (int) scale + 1;
		}
		
		this.revalidateBG();
		
		this.revalidate();
		
		float factor = scale / oldScale;
		
		Point point = pane.getMousePosition();
		if(point != null)
		{
			Point pos = pane.getViewport().getViewPosition();
			
			int newX = (int) (point.x * (factor - 1f) + factor * pos.x);
			int newY = (int) (point.y * (factor - 1f) + factor * pos.y);
			pane.getViewport().setViewPosition(new Point(newX, newY));
		}
		
		this.repaint();
	}
	
	public void decZoom()
	{
		float oldScale = scale;
		
		JScrollPane pane = Paint.main.gui.scroll;
		
		if(scale > 1)
		{
			scale = (int) scale - 1;
		}
		else if(scale > 1 / 10f)
		{
			scale = scale - 1 / 10f;
		}
		
		this.revalidateBG();
		
		this.revalidate();
		
		float factor = scale / oldScale;
		
		Point point = pane.getMousePosition();
		if(point != null)
		{
			Point pos = pane.getViewport().getViewPosition();
			
			int newX = (int) (point.x * (factor - 1f) + factor * pos.x);
			int newY = (int) (point.y * (factor - 1f) + factor * pos.y);
			pane.getViewport().setViewPosition(new Point(newX, newY));
		}
		
		this.repaint();
	}
	
	public float getScale()
	{
		return scale;
	}
}
