package heroesgrave.utils.misc;

import java.util.Random;

public class RandomUtils
{
	private static final Random r = new Random();
	
	public static int rInt(int max)
	{
		return rInt(r, max);
	}
	
	public static int rInt(Random r, int max)
	{
		return r.nextInt(max);
	}
	
	public static int rInt(int min, int max)
	{
		return rInt(r, min, max);
	}
	
	public static int rInt(Random r, int min, int max)
	{
		return r.nextInt(max-min+1)+min;
	}
}
