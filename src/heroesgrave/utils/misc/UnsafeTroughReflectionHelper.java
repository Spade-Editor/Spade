/*
 *	Copyright 2013 Longor1996
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

package heroesgrave.utils.misc;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * Do NOT use this class in/for high-performance code, since it is REALLY slow.
 **/
public class UnsafeTroughReflectionHelper {
	public static final Unsafe theUnsafe;
	public static final int addressSize;
	public static final int pageSize;
	
	static {
		Field unsafeInstanceField = null;
		Unsafe unsafeT = null;
		
		try {
			unsafeInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeInstanceField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException("FATAL EXCEPTION! Unable to retrieve 'Unsafe'-Field!");
		}
		
		try {
			unsafeT = (Unsafe) unsafeInstanceField.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException("FATAL EXCEPTION! Unable to retrieve 'Unsafe'-Object!");
		}
		
		theUnsafe = unsafeT;
		
		if(theUnsafe != null)
		{
			pageSize = theUnsafe.pageSize();
			addressSize = theUnsafe.addressSize();
			
			System.out.println("Address-Size: " + addressSize);
			System.out.println("Page-Size: " + addressSize);
			
			// Try to do some memory allocation and modification. If it crashes, there is something seriosly WRONG with the Unsafe class.
			long pointer = theUnsafe.allocateMemory(16L);
			theUnsafe.putInt(pointer, 12345678);
			theUnsafe.freeMemory(pointer);
		}
		else
		{
			pageSize = -1;
			addressSize = -1;
		}
		
	}
	
 	/**
	 * Throw ANY exception, no matter if there is a 'throws'-table in the method-header or not.
	 * Useful for fully dynamic exception-throwing.
	 * 
	 * if there is no Unsafe instance avialbe, the exception will be packed into a RuntimeException, that will then be thrown.(failsafe!)
 	 **/
 	public static final void throwException(Throwable e){
		assert e != null : "You can not throw null as an exception!";
		
		if(theUnsafe != null)
		{
			theUnsafe.throwException(e);
		}
		else
		{
			System.err.println("theUnsafe is not avaible for usage! rt.jar does not support sun.misc.Unsafe, Aborting!");
			throw new RuntimeException(e);
		}
 	}
}
