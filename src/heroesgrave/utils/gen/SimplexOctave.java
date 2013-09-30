package heroesgrave.utils.gen;


public class SimplexOctave
{
	private final SimplexNoise simplex;
	
	public SimplexOctave(SimplexNoise simplex)
	{
		this.simplex = simplex;
	}
	
	public double noise(double x, double y)
	{
		return simplex.noise(x, y);
	}
}
