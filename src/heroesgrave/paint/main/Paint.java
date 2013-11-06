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

package heroesgrave.paint.main;

import heroesgrave.paint.gui.GUIManager;
import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.imageops.ImageOp;
import heroesgrave.paint.plugin.PluginManager;
import heroesgrave.paint.tools.Tool;
import heroesgrave.utils.app.Application;
import heroesgrave.utils.io.ImageExporter;
import heroesgrave.utils.io.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Paint extends Application
{
	public static Paint main = new Paint();
	
	public GUIManager gui;
	public heroesgrave.paint.plugin.PluginManager pluginManager;
	
	public File openFile;
	public File openDir;
	
	private File toOpen;
	
	public Tool currentTool;
	
	public Selection selection;
	
	public static int leftColour = 0xff000000;
	public static int rightColour = 0xffffffff;
	
	public boolean saved = true;
	
	private static HashMap<String, Tool> tools = new HashMap<String, Tool>();
	private static HashMap<String, ImageOp> imageOps = new HashMap<String, ImageOp>();
	
	public void init()
	{
		pluginManager = PluginManager.instance(this);
		selection = new Selection();
		gui = new GUIManager();
		setRightColour(0xffffffff);
		setLeftColour(0xff000000);
		setTool(currentTool);
		pluginManager.onLaunch();
		
		if(toOpen != null)
		{
			Paint.main.openFile = toOpen;
			Paint.main.openDir = toOpen.getParentFile();
			Paint.main.gui.canvas.setImage(ImageLoader.loadImage(toOpen.getAbsolutePath()));
		}
	}
	
	public void update()
	{
		gui.info.setSaved(saved);
		gui.info.setScale(gui.canvas.getScale());
		gui.setFile(openFile);
	}
	
	public void render()
	{
		
	}
	
	public void dispose()
	{
		
	}
	
	public void newImage(final int width, final int height)
	{
		if(!saved)
		{
			final JDialog newImage = new JDialog();
			newImage.setTitle("Save current image?");
			newImage.setAlwaysOnTop(true);
			newImage.setAutoRequestFocus(true);
			newImage.setLayout(new BorderLayout());
			
			JButton save = new JButton("Save & Create New Image");
			JButton dispose = new JButton("Create new image without saving");
			JButton cancel = new JButton("Don't create new image");
			
			newImage.add(save, BorderLayout.NORTH);
			newImage.add(dispose, BorderLayout.CENTER);
			newImage.add(cancel, BorderLayout.SOUTH);
			
			newImage.pack();
			newImage.setResizable(false);
			newImage.setVisible(true);
			newImage.setLocationRelativeTo(null);
			
			save.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Paint.save();
					newImage.dispose();
					createImage(width, height);
				}
			});
			dispose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newImage.dispose();
					createImage(width, height);
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newImage.dispose();
				}
			});
		}
		else
		{
			createImage(width, height);
		}
	}
	
	private void createImage(int width, int height)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, width, height);
		g.dispose();
		gui.canvas.setImage(image);
		this.openFile = null;
	}
	
	public static void addTool(String key, Tool tool)
	{
		tools.put(key.toLowerCase(), tool);
	}
	
	public static void addImageOp(String key, ImageOp op)
	{
		imageOps.put(key.toLowerCase(), op);
	}
	
	public static Tool getTool(String key)
	{
		return tools.get(key.toLowerCase());
	}
	
	public static ImageOp getImageOp(String key)
	{
		return imageOps.get(key.toLowerCase());
	}
	
	public static void addChange(Change change)
	{
		main.gui.canvas.addChange(change);
	}
	
	public static void setTool(Tool tool)
	{
		main.currentTool = tool;
		main.gui.info.setTool(tool);
		main.gui.setToolOption(tool.getOptions());
	}
	
	public static void save()
	{
		if(Paint.main.openFile != null)
		{
			String fileName = Paint.main.openFile.getAbsolutePath();
			
			String extension = "";
			
			int i = fileName.lastIndexOf('.');
			
			if(i > 0)
			{
				extension = fileName.substring(i + 1);
			}
			
			ImageExporter exporter = ImageExporter.get(extension);
			
			if(fileName.endsWith("." + exporter.getFileExtension()))
			{
				try {
					exporter.exportImage(Paint.main.gui.canvas.getImage(), new File(fileName));
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"An error occurred while saving the Image:\n"+e.getLocalizedMessage(),"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else
			{
				try {
					exporter.exportImage(Paint.main.gui.canvas.getImage(), new File(fileName));
					Paint.main.openFile = new File(fileName + ".png");
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"An error occurred while saving the Image:\n"+e.getLocalizedMessage(),"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			main.saved = true;
		}
		else
		{
			saveAs();
		}
	}
	
	public static void saveAs()
	{
		
		/**
		 * How the new system works:
		 *
		JFileChooser fileChooser = new JFileChooser();  
		
		// Add ALL the FileFilter's!
		fileChooser.addChoosableFileFilter(...);  
		fileChooser.addChoosableFileFilter(...);  
		fileChooser.addChoosableFileFilter(...);  
		fileChooser.addChoosableFileFilter(...);  
		
		...  
		
		int result = fileChooser.showSaveDialog(parentComponent);  
		if (result == JFileChooser.APPROVE_OPTION)  
		{  
		// the user pressed OK  
		File file = fileChooser.getSelectedFile();  
		FileFilter fileFilter = fileChooser.getFileFilter();  
		
		...  
		}  
		
		 * 
		 **/
		
		JFileChooser chooser = new JFileChooser(Paint.main.openDir);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		for(ImageExporter exporter : ImageExporter.exporters)
		{
			chooser.addChoosableFileFilter(exporter);
		}
		
		int returned = chooser.showSaveDialog(new CentredJDialog());
		ImageExporter formatToSaveIn = (ImageExporter) chooser.getFileFilter();
		
		if(returned == JFileChooser.APPROVE_OPTION)
		{
			Paint.main.openFile = chooser.getSelectedFile();
			
			if(Paint.main.openFile.getAbsolutePath().endsWith("." + formatToSaveIn.getFileExtension()))
			{
				// Do nothing.
			}
			else
			{
				// Put the format at the end of the File-Name!
				Paint.main.openFile = new File(Paint.main.openFile.getAbsolutePath() + "." + formatToSaveIn.getFileExtension());
			}
			
			Paint.main.openDir = Paint.main.openFile.getParentFile();
			
			try {
				formatToSaveIn.exportImage(Paint.main.gui.canvas.getImage(), Paint.main.openFile);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "An error occurred while saving the Image:\n"+e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			main.saved = true;
		}
	}
	
	public void setLeftColour(int c)
	{
		gui.chooser.setLeftColour(c);
		gui.info.setLeftColour(c);
		Paint.leftColour = c;
	}
	
	public int getLeftColour()
	{
		return Paint.leftColour;
	}
	
	public void setRightColour(int c)
	{
		gui.chooser.setRightColour(c);
		gui.info.setRightColour(c);
		Paint.rightColour = c;
	}
	
	public int getRightColour()
	{
		return Paint.rightColour;
	}
	
	public static void main(String[] args)
	{
		if(args.length == 1)
		{
			System.out.println(args[0]);
			File f = new File(args[0]);
			if(f.exists())
			{
				main.toOpen = f;
			}
		}
		Application.launch(main);
	}
}