package heroesgrave.utils.math;

public class Vector3F
{
	public float x, y, z;

	public Vector3F()
	{
		this(0F, 0F, 0F);
	}

	public Vector3F(Vector3F vec)
	{
		this(vec.x, vec.y, vec.z);
	}

	public Vector3F(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3F sub(Vector3F vec)
	{
		return new Vector3F(x - vec.x, y - vec.y, z - vec.z);
	}

	public Vector3F diff(Vector3F vec)
	{
		return new Vector3F(Math.abs(vec.x - x), Math.abs(vec.y - y), Math.abs(vec.z - z));
	}

	public void dec(Vector3F vec)
	{
		if (this.x > 0)
			x = Math.max(this.x - vec.x, 0);
		else if (this.x < 0)
			x = Math.min(this.x + vec.x, 0);
		if (this.y > 0)
			y = Math.max(this.y - vec.y, 0);
		else if (this.y < 0)
			y = Math.min(this.y + vec.y, 0);
		if (this.z > 0)
			z = Math.max(this.z - vec.z, 0);
		else if (this.z < 0)
			z = Math.min(this.z + vec.z, 0);
	}

	public Vector3F abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}

	public float mag()
	{
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3F norm()
	{
		double mag = mag();
		x /= mag;
		y /= mag;
		z /= mag;
		return this;
	}

	public String toString()
	{
		return "(" + x + "," + y + "," + z + ")";
	}
}
