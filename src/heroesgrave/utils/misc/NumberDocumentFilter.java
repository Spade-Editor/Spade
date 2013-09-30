package heroesgrave.utils.misc;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberDocumentFilter extends DocumentFilter
{
	public void insertString(FilterBypass fb, int off, String str, AttributeSet att) throws BadLocationException
	{
		fb.insertString(off, str.replaceAll("\\D++", ""), att);
	}
	
	public void replace(FilterBypass fb, int off, int len, String str, AttributeSet att) throws BadLocationException
	{
		fb.replace(off, len, str.replaceAll("\\D++", ""), att);
	}
}
