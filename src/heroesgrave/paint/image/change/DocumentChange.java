package heroesgrave.paint.image.change;

import heroesgrave.paint.image.Document;

public interface DocumentChange
{
	public void apply(Document doc);
	
	public void revert(Document doc);
}
