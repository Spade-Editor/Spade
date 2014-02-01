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

package heroesgrave.paint.gui;

import heroesgrave.utils.misc.IFunc;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class SimpleActionTextField extends JTextField implements DocumentListener
{
	private IFunc<String> call;
	
	public SimpleActionTextField(String string, IFunc<String> call)
	{
		super(string);
		this.call = call;
		
		this.getDocument().addDocumentListener(this);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e)
	{
		call.action(this.getText());
	}
	
	@Override
	public void removeUpdate(DocumentEvent e)
	{
		call.action(this.getText());
	}
	
	@Override
	public void changedUpdate(DocumentEvent e)
	{
		call.action(this.getText());
	}
	
}