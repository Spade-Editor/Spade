package heroesgrave.paint.image;

import heroesgrave.paint.io.ImageExporter;
import heroesgrave.paint.io.ImageImporter;
import heroesgrave.utils.misc.Metadata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Document
{
	private int width, height;
	private File file;
	private Metadata info;
	private Layer root, current;
	private History history;
	
	public boolean saved;
	
	private ArrayList<Layer> flatmap = new ArrayList<Layer>();
	
	public Document(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.info = new Metadata();
		this.history = new History(this);
		
		RawImage image = new RawImage(width, height);
		/* Fill the image with gradient + noise.
		for(int i = 0; i < 512; i++)
		{
			for(int j = 0; j < 512; j++)
			{
				int color = 0xff000000;
				color |= (i / 2) << 0;
				color |= (j / 2) << 8;
				color |= RandomUtils.rInt(0xff) << 16;
				image.setPixel(i, j, color);
			}
		}
		/**/
		this.current = this.root = new Layer(this, image, new Metadata());
		this.reconstructFlatmap();
	}
	
	public Document(File f)
	{
		this.info = new Metadata();
		this.history = new History(this);
		this.file = f;
		ImageImporter.loadImage(file.getAbsolutePath(), this);
		this.reconstructFlatmap();
	}
	
	public void reconstructFlatmap()
	{
		this.flatmap.clear();
		root.constructFlatMap(flatmap);
	}
	
	public History getHistory()
	{
		return history;
	}
	
	public Layer getRoot()
	{
		return root;
	}
	
	public Layer getLayer()
	{
		return current;
	}
	
	public void setRoot(Layer root)
	{
		this.current = this.root = root;
		this.reconstructFlatmap();
	}
	
	public void setCurrent(Layer current)
	{
		this.current = current;
	}
	
	public Metadata getMetadata()
	{
		return info;
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void save()
	{
		String fileName = this.file.getAbsolutePath();
		
		String extension = "";
		
		int i = fileName.lastIndexOf('.');
		
		if(i > 0)
		{
			extension = fileName.substring(i + 1);
		}
		
		ImageExporter exporter = ImageExporter.get(extension);
		
		try
		{
			exporter.export(this, new File(fileName));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					"An error occurred while saving the Image:\n"
							+ e.getLocalizedMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public String getDir()
	{
		if(file != null)
			return this.file.getParent();
		else
			return System.getProperty("user.dir");
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public ArrayList<Layer> getFlatMap()
	{
		return flatmap;
	}
	
	/*
	 * This should only be called when loading an image. I need to find another way to do this.
	 */
	public void setDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public BufferedImage getRenderedImage()
	{
		BufferedImage image =
				new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		{
			Graphics2D g = image.createGraphics();
			root.render(g);
			g.dispose();
		}
		return image;
	}
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
}
