package heroesgrave.utils.misc;

public class Pointer<P>
{
	private P value;
	
	public Pointer(P value)
	{
		this.value = value;
	}
	
	public void set(P value)
	{
		this.value = value;
	}
	
	public P get()
	{
		return this.value;
	}
}
