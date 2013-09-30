package heroesgrave.utils.gen;

public class SimplexOctaves
{
	private SimplexOctave[] octaves;
	
	public SimplexOctaves(int amount)
	{
		octaves = new SimplexOctave[amount];
	}
	
	public void addOctave(SimplexOctave octave)
	{
		for(int i = 0; i < octaves.length; i++)
		{
			if(octaves[i] == null)
			{
				octaves[i] = octave;
				return;
			}
		}
	}
	
	public double noise(double x, double y)
	{
		double val = octaves[0].noise(x, y);
		if(octaves.length == 1)
			return val;
		for(int i = 1; i < octaves.length; i++)
		{
			val = Math.max(val, octaves[i].noise(x, y));
		}
		return val;
	}
	
	public SimplexOctave getActive(double x, double y)
	{
		SimplexOctave act = octaves[0];
		double val = octaves[0].noise(x, y);
		if(octaves.length == 1)
			return act;
		for(int i = 1; i < octaves.length; i++)
		{
			double nval = octaves[i].noise(x, y);
			if(nval > val)
			{
				val = nval;
				act = octaves[i];
			}
		}
		return act;
	}
}
