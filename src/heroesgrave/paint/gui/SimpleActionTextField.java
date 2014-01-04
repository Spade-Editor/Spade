package heroesgrave.paint.gui;

import heroesgrave.utils.misc.IFunc;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class SimpleActionTextField extends JTextField implements DocumentListener {
	private IFunc<String> call;
	
	public SimpleActionTextField(String string, IFunc<String> call)
	{
		super(string);
		this.call = call;
		
		this.getDocument().addDocumentListener(this);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		call.action(this.getText());
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		call.action(this.getText());
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		call.action(this.getText());
	}
	
}
