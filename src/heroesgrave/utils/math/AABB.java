package heroesgrave.utils.math;

public class AABB
{
	public float x, y;
	public float w, h;
	
	public AABB(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public static AABB fromBounds(float x1, float y1, float x2, float y2)
	{
		return new AABB(MathUtils.interp(x1, x2), MathUtils.interp(y1, y2), MathUtils.difference(x1, x2)/2F, MathUtils.difference(y1, y2)/2F);
	}
	
	public boolean collides(AABB aabb)
	{
		if(Math.abs(x-aabb.x) < w + aabb.w)
		{
			if(Math.abs(y-aabb.y) < h + aabb.h)
			{
				return true;
			}
		}
		return false;
	}

	public boolean inside(Vector2F vec)
	{
		if(Math.abs(x-vec.x) < w)
		{
			if(Math.abs(y-vec.y) < h)
			{
				return true;
			}
		}
		return false;
	}
}
