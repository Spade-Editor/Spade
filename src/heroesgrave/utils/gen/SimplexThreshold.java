package heroesgrave.utils.gen;


public class SimplexThreshold
{
	private final SimplexNoise simplex;
	private final int xoff, yoff;
	private final double scale, min, max;
	private final boolean inverted;
	
	public SimplexThreshold(SimplexNoise simplex, double scale, int xoff, int yoff, double min, double max)
	{
		this.simplex = simplex;
		this.scale = scale;
		this.xoff = xoff;
		this.yoff = yoff;
		this.min = min;
		this.max = max;
		inverted = max < min;
	}
	
	public boolean passes(double x, double y)
	{
		double value = simplex.noise((x+xoff)/scale, (y+yoff)/scale);
		if(inverted)
		{
			if(value < max || value > min)
				return true;
		}
		else
		{
			if(value < max && value > min)
				return true;
		}
		return false;
	}
}
