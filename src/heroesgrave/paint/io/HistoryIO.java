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

package heroesgrave.paint.io;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IMarker.Marker;
import heroesgrave.paint.image.change.SingleChange;
import heroesgrave.paint.image.change.edit.ClearMaskChange;
import heroesgrave.paint.image.change.edit.DrawPathChange;
import heroesgrave.paint.image.change.edit.FillMaskChange;
import heroesgrave.paint.image.change.edit.PathChange;
import heroesgrave.paint.image.change.edit.SetImageChange;
import heroesgrave.paint.image.change.edit.SetMaskChange;
import heroesgrave.utils.io.IOUtils;
import heroesgrave.utils.misc.RandomUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class HistoryIO
{
	private static final ArrayList<Class<? extends Serialised>> classes = new ArrayList<Class<? extends Serialised>>();
	private static File historyDir;
	
	private LinkedList<File> files = new LinkedList<File>();
	
	public static class Result
	{
		public RawImage a, b;
		public LinkedList<Serialised> changes;
	}
	
	public static void init()
	{
		historyDir = new File(IOUtils.assemblePath(System.getProperty("user.home"), ".paint-java", ".history"));
		if(!historyDir.exists())
		{
			historyDir.mkdirs();
		}
	}
	
	private static File randomFile(String extension) throws IOException
	{
		if(extension == null)
		{
			extension = "";
		}
		else
		{
			extension = "." + extension;
		}
		File file;
		do
		{
			file = new File(historyDir, Integer.toHexString(RandomUtils.rInt()) + extension);
		}
		while(!file.createNewFile());
		return file;
	}
	
	public Result read()
	{
		if(files.isEmpty())
			return null;
		try
		{
			File f = files.pop();
			
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
			
			Result result = new Result();
			result.changes = new LinkedList<Serialised>();
			result.a = readBuffer(in);
			result.b = readBuffer(in);
			read(in, result.changes);
			
			in.close();
			
			f.delete();
			
			return result;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("An error occured while reading the old history from file");
			files.clear();
			return null;
		}
	}
	
	public static RawImage readBuffer(DataInputStream in) throws IOException
	{
		int width = in.readInt();
		int height = in.readInt();
		int[] buffer = new int[width * height];
		for(int i = 0; i < buffer.length; i++)
			buffer[i] = in.readInt();
		if(in.readBoolean())
		{
			boolean[] mask = new boolean[width * height];
			for(int i = 0; i < mask.length; i++)
				mask[i] = in.readBoolean();
			return new RawImage(width, height, buffer, mask);
		}
		else
		{
			return new RawImage(width, height, buffer);
		}
	}
	
	public static void read(DataInputStream in, LinkedList<Serialised> changes) throws IOException
	{
		try
		{
			int ID;
			while((ID = in.read()) != -1)
			{
				Class<? extends Serialised> c = classes.get(ID);
				Serialised change = c.newInstance();
				if(change instanceof SingleChange)
				{
					change = ((SingleChange) change).getInstance();
				}
				change.read(in);
				changes.addLast(change);
			}
		}
		catch(IllegalAccessException | InstantiationException e)
		{
			e.printStackTrace();
			throw new RuntimeException("A reflection error occured while reading the old history from file. Perhaps it was corrupted?");
		}
	}
	
	public void write(LinkedList<Serialised> changes, RawImage a, RawImage b)
	{
		try
		{
			File f = randomFile("his");
			f.deleteOnExit();
			
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			
			writeBuffer(out, a);
			writeBuffer(out, b);
			
			write(out, changes);
			
			out.flush();
			out.close();
			
			files.push(f);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("An error occured while writing the old history to file");
		}
	}
	
	private static void writeBuffer(DataOutputStream out, RawImage image) throws IOException
	{
		out.writeInt(image.width);
		out.writeInt(image.height);
		int[] buffer = image.borrowBuffer();
		for(int i = 0; i < buffer.length; i++)
			out.writeInt(buffer[i]);
		if(image.isMaskEnabled())
		{
			out.writeBoolean(true);
			boolean[] mask = image.borrowMask();
			for(int i = 0; i < mask.length; i++)
				out.writeBoolean(mask[i]);
		}
		else
		{
			out.writeBoolean(false);
		}
	}
	
	public static void write(DataOutputStream out, LinkedList<Serialised> changes) throws IOException
	{
		while(!changes.isEmpty())
		{
			Serialised change = changes.pop();
			Class<? extends Serialised> c = change.getClass();
			if(!classes.contains(c))
			{
				System.err.println("Unregistered Serialised Type: " + c);
			}
			else
			{
				out.writeByte(getChangeID(change.getClass()));
				change.write(out);
			}
		}
	}
	
	static
	{
		classes.add(Marker.class);
		
		classes.add(DrawPathChange.class);
		classes.add(PathChange.Serial.class);
		
		classes.add(SetImageChange.class);
		classes.add(SetMaskChange.class);
		
		classes.add(ClearMaskChange.class);
		classes.add(FillMaskChange.class);
	}
	
	public static void registerClass(Class<? extends Serialised> c)
	{
		classes.add(c);
	}
	
	public static int getChangeID(Class<? extends Serialised> c)
	{
		if(!classes.contains(c))
			System.err.println("Unregistered Serialised Type: " + c);
		return classes.indexOf(c);
	}
	
	public static Serialised create(int id)
	{
		Class<? extends Serialised> c = classes.get(id);
		try
		{
			return c.newInstance();
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
