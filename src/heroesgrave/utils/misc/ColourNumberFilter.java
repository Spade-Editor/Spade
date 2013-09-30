package heroesgrave.utils.misc;

import heroesgrave.utils.math.MathUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ColourNumberFilter extends DocumentFilter
{
	public void insertString(FilterBypass fb, int off, String str, AttributeSet att) throws BadLocationException
	{
		int v = Integer.parseInt(str.replaceAll("\\D++", ""));
		v = MathUtils.clamp(v, 255, 0);
		fb.insertString(off, "" + v, att);
	}

	public void replace(FilterBypass fb, int off, int len, String str, AttributeSet att) throws BadLocationException
	{
		int v = Integer.parseInt(str.replaceAll("\\D++", ""));
		v = MathUtils.clamp(v, 255, 0);
		fb.replace(off, len, "" + v, att);
	}
}
