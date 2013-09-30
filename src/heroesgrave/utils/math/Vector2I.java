package heroesgrave.utils.math;

public class Vector2I
{
	public int x, y;
	
	public Vector2I()
	{
		this(0, 0);
	}
	
	public Vector2I(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}

	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Vector2I other = (Vector2I) obj;
		if(x != other.x)
			return false;
		if(y != other.y)
			return false;
		return true;
	}
}
