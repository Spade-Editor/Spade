package heroesgrave.utils.gen;


public class BasicOctave extends SimplexOctave
{
	private final int xoff, yoff, hoff;
	private final double scale, height;
	
	public BasicOctave(SimplexNoise simplex, double height, double scale, int xoff, int yoff, int hoff)
	{
		super(simplex);
		this.height = height;
		this.scale = scale;
		this.xoff = xoff;
		this.yoff = yoff;
		this.hoff = hoff;
	}
	
	public double noise(double x, double y)
	{
		return hoff+super.noise((x+xoff)/scale, (y+yoff)/scale)*height;
	}
}
