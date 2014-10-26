/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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
package heroesgrave.utils.io;

import heroesgrave.paint.image.BufferedChange;
import heroesgrave.paint.image.IFrame;
import heroesgrave.paint.image.KeyFrame;
import heroesgrave.paint.image.PixelChange;
import heroesgrave.paint.image.ShapeChange;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author BurntPizza
 *
 */
public class HistorySerializer {
	
	private static File log;
	private static DataInputStream reader;
	private static DataOutputStream writer;
	
	static {
		try {
			log = File.createTempFile("PAINTDOTJAVA", "log");
			reader = new DataInputStream(new FileInputStream(log));
			writer = new DataOutputStream(new FileOutputStream(log));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void push(IFrame frame) {
		if(frame instanceof PixelChange) {
			PixelChange p = (PixelChange) frame;
			try {
				writer.writeChar('p');
				writer.writeInt(p.x);
				writer.writeInt(p.y);
				writer.writeInt(p.n);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(frame instanceof BufferedChange) {
			
		}
		else if(frame instanceof KeyFrame) {
			KeyFrame k = (KeyFrame) frame;
			int w = k.getImage().getWidth();
			int h = k.getImage().getHeight();
			int[] data = ((DataBufferInt)k.takeImage().getData().getDataBuffer()).getData();
			
			try {
				writer.writeChar('k');
				writer.writeInt(w);
				writer.writeInt(h);
				
				for(int i = 0;i < data.length;i++) {
					writer.writeInt(data[i]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else if(frame instanceof ShapeChange) {
			
		}
	}
	
	public static IFrame pop() {
		try {
			char type = reader.readChar();
			if(type == 'p') {
				return new PixelChange(reader.readInt(), reader.readInt(),reader.readInt());
			}
			else if(type == 'k') {
				int w = reader.readInt();
				int h = reader.readInt();
				BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				int[] data = ((DataBufferInt)b.getData().getDataBuffer()).getData();
				
				for(int i = 0;i < w * h;i++) {
					data[i] = reader.readInt();
				}
				return new KeyFrame(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void close() {
		try {
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HistorySerializer() {}
}
