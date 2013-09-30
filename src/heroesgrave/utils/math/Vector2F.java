package heroesgrave.utils.math;

public class Vector2F
{
	public float x, y;
	
	public Vector2F()
	{
		this(0F, 0F);
	}

	public Vector2F(Vector2F vec)
	{
		this(vec.x, vec.y);
	}
	
	public Vector2F(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2F sub(Vector2F vec)
	{
		return new Vector2F(x-vec.x, y-vec.y);
	}
	
	public Vector2F diff(Vector2F vec)
	{
		return new Vector2F(Math.abs(vec.x-x), Math.abs(vec.y-y));
	}
	
	public Vector2F abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}
	
	public float mag()
	{
		return (float) Math.sqrt(x*x+y*y);
	}
	
	public Vector2F norm()
	{
		double mag = mag();
		x /= mag;
		y /= mag;
		return this;
	}
	
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
}
