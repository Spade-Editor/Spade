package heroesgrave.paint.gui;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.io.IOUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.alee.laf.button.WebButton;

@SuppressWarnings("serial")
public class DocumentButton extends WebButton implements ActionListener
{
	protected Document doc;
	
	public DocumentButton(Document doc)
	{
		super("Untitled");
		this.doc = doc;
		this.addActionListener(this);
		this.setFocusable(false);
		checkName();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Paint.setDocument(doc);
	}
	
	public void checkName()
	{
		File f = doc.getFile();
		String s;
		if(f != null)
		{
			s = IOUtils.relativeFrom(System.getProperty("user.dir"), f.getPath());
		}
		else
		{
			s = "Untitled";
		}
		if(!doc.saved())
		{
			s += "*";
		}
		this.setText(s);
	}
}
