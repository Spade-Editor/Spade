/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.utils.math;

/**
 * This class SEEMS to be damaned.
 * @deprecated Probably damaged!
 **/
public class Perlin
{
	
	public float persistence = 1f;
	public int numberOfOctaves = 5;
	public float hScale = 2f;
	public float vScale = 3f;
	
	private final long SEED;
	private final long SEED0;
	private final long SEED1;
	private final long SEED2;
	
	private final long NOISE_MAGIC_X;// 1619;
	private final long NOISE_MAGIC_Y;// 31337;
	private final long NOISE_MAGIC_Z;// 52597;
	private final long NOISE_MAGIC_SEED;// 1013;
	
	public Perlin(long S)
	{
		System.out.println("[PERLIN] Setting Seed:" + S);
		this.SEED = S;
		this.SEED0 = (long) Math.pow(S, 16);
		this.SEED1 = (int) ((this.SEED0 * 3.141592) / 2);
		this.SEED2 = (int) ((this.SEED0 / 3.141592) * 4);
		
		this.NOISE_MAGIC_SEED = this.SEED;
		this.NOISE_MAGIC_X = this.SEED0 * this.SEED2;
		this.NOISE_MAGIC_Y = this.SEED1 * this.SEED0;
		this.NOISE_MAGIC_Z = this.SEED2 * this.SEED1;
	}
	
	public long getSeed()
	{
		return this.SEED;
	}
	
	/**
	 * @deprecated Do not use. Will probably be removed.
	 **/
	public final float noise2D(int x)
	{
		x = (x << 13) ^ x;
		return (float) (1.0 - ((((x * ((x * x * 15731) + 789221)) + 1376312589) & 2147483647) / 1073741824.0));
	}
	
	/**
	 * @deprecated Do not use. Will probably be removed.
	 **/
	public final float interpolatedNoise2D(int x)
	{
		return (this.noise2D(x) / 2) + (this.noise2D(x - 1) / 4) + (this.noise2D(x + 1) / 4);
	}
	
	private final float noise(int x, int y)
	{
		int n = x + (y * 57);
		n = (n << 13) ^ n;
		return (1.0f - ((((n * ((n * n * this.SEED0) + this.SEED1)) + this.SEED2) & 0x7fffffff) / 1073741824f));
		
		//[NO SEEED] return (1.0f-((n*(n*n*15731+789221)+1376312589)&0x7fffffff)/1073741824f);
	}
	
	public final double noise3d(int x, int y, int z, long l)
	{
		int n = (int) (((this.NOISE_MAGIC_X * x) + (this.NOISE_MAGIC_Y * y) + (this.NOISE_MAGIC_Z * z) + (this.NOISE_MAGIC_SEED * l)) & 0x7fffffff);
		
		//n = (int) Math.pow(n<<13,n);
		n = (n << 13) ^ n;
		n = ((n * ((n * n * 15731) + 789221)) + 1376312589) & 0x7fffffff;
		
		return 1.0 - ((double) n / 1073741824);
	}
	
	private final double triLinearInterpolation(final double v000, final double v100, final double v010, final double v110, final double v001,
			final double v101, final double v011, final double v111, final double x, final double y, final double z)
	{
		/*double tx = easeCurve(x);
		double ty = easeCurve(y);
		double tz = easeCurve(z);*/
		final double tx = x;
		final double ty = y;
		final double tz = z;
		return ((v000 * (1 - tx) * (1 - ty) * (1 - tz)) + (v100 * tx * (1 - ty) * (1 - tz)) + (v010 * (1 - tx) * ty * (1 - tz))
				+ (v110 * tx * ty * (1 - tz)) + (v001 * (1 - tx) * (1 - ty) * tz) + (v101 * tx * (1 - ty) * tz) + (v011 * (1 - tx) * ty * tz) + (v111 * tx
				* ty * tz));
	}
	
	private final double noise3d_gradient(final double x, final double y, final double z, final long l)
	{
		// Calculate the integer coordinates
		final int x0 = (x > 0.0 ? (int) x : (int) x - 1);
		final int y0 = (y > 0.0 ? (int) y : (int) y - 1);
		final int z0 = (z > 0.0 ? (int) z : (int) z - 1);
		
		// Calculate the remaining part of the coordinates
		final double xl = x - x0;
		final double yl = y - y0;
		final double zl = z - z0;
		
		// Get values for corners of cube
		final double v000 = this.noise3d(x0, y0, z0, l);
		final double v100 = this.noise3d(x0 + 1, y0, z0, l);
		final double v010 = this.noise3d(x0, y0 + 1, z0, l);
		final double v110 = this.noise3d(x0 + 1, y0 + 1, z0, l);
		final double v001 = this.noise3d(x0, y0, z0 + 1, l);
		final double v101 = this.noise3d(x0 + 1, y0, z0 + 1, l);
		final double v011 = this.noise3d(x0, y0 + 1, z0 + 1, l);
		final double v111 = this.noise3d(x0 + 1, y0 + 1, z0 + 1, l);
		
		// Interpolate
		return this.triLinearInterpolation(v000, v100, v010, v110, v001, v101, v011, v111, xl, yl, zl);
	}
	
	/**
	 * @param x The X Position of the Noise Value.
	 * @param y The Y Position of the Noise Value.
	 * @param z The Z Position of the Noise Value.
	 * 
	 * WARNING: This Method is Extremely slow!<br>
	 * Use it only when you need high quality results.
	 * 
	 * @return A 3D Perlin-Noise Value.
	 **/
	public final double noise3d_perlin(final double x, final double y, final double z)
	{
		double a = 0;
		double f = 0.125D;
		double g = 0.25D;
		final double persistence = 0.75;
		final int numberOfOctaves = 4;
		
		for(int i = 0; i < numberOfOctaves; i++)
		{
			a += g * this.noise3d_gradient(x * f, y * f, z * f, this.SEED + i);
			f *= 2.0;
			g *= persistence;
		}
		
		return a;
	}
	
	/**
	 * WARNING: Very slow! You should use "simplex" if you need performance.
	 * 
	 * @return 3D Noise for the given input.
	 **/
	public final float noise3d_simplex4(double x, double y, double z)
	{
		float e = 0;
		e += this.noise3d_gradient(x / 40, y / 40, z / 40, this.SEED);
		e += this.noise3d_gradient(x / 50, y / 50, z / 50, this.SEED);
		e += this.noise3d_gradient(x / 60, y / 60, z / 60, this.SEED);
		e += this.noise3d_gradient(x / 70, y / 70, z / 70, this.SEED);
		e /= 4;
		return e;
	}
	
	/**
	 * WARNING: Very slow! You should use "simplex" if you need performance.
	 * 
	 * @return 3D Noise for the given input.
	 **/
	public final double noise3d_simplex5(double x, double y, double z)
	{
		double e = 0;
		e += this.noise3d_gradient(x / 30, y / 30, z / 30, this.SEED + 1);
		e += this.noise3d_gradient(x / 40, y / 40, z / 40, this.SEED + 2);
		e += this.noise3d_gradient(x / 50, y / 50, z / 50, this.SEED + 3);
		e += this.noise3d_gradient(x / 60, y / 60, z / 60, this.SEED + 4);
		e += this.noise3d_gradient(x / 70, y / 70, z / 70, this.SEED + 5);
		e /= 5;
		return e;
	}
	
	/**
	 * WARNING: Very slow! You should use "simplex" if you need performance.
	 * 
	 * @return 3D Noise for the given input.
	 **/
	public final float noise3d_simplex7(double x, double y, double z)
	{
		float e = 0;
		e += this.noise3d_gradient(x / 10, y / 10, z / 10, this.SEED);
		e += this.noise3d_gradient(x / 20, y / 20, z / 20, this.SEED);
		e += this.noise3d_gradient(x / 30, y / 30, z / 30, this.SEED);
		e += this.noise3d_gradient(x / 40, y / 40, z / 40, this.SEED);
		e += this.noise3d_gradient(x / 50, y / 50, z / 50, this.SEED);
		e += this.noise3d_gradient(x / 60, y / 60, z / 60, this.SEED);
		e += this.noise3d_gradient(x / 70, y / 70, z / 70, this.SEED);
		e /= 7;
		return e;
	}
	
	/**
	 * Calculates a random 2D Perlin-Noise Value between -1 and +1,<br>
	 * based on the interpolated value of random fractional corner values.<br>
	 * <br>
	 * Also, don't ask why all the variables are made final.<br>
	 * 
	 * @param x The X Position of the Noise
	 * @param y The Y Position of the Noise
	 * 
	 * @return A random 2D Perlin-Noise Value between -1 and +1 as float.
	 **/
	public float interpolatedNoise(final float x, final float y)
	{
		final int integer_X = (int) x;
		final float fractional_X = x - integer_X;
		
		final int integer_Y = (int) y;
		final float fractional_Y = y - integer_Y;
		
		final float v1 = this.noise(integer_X, integer_Y);
		final float v2 = this.noise(integer_X + 1, integer_Y);
		final float v3 = this.noise(integer_X, integer_Y + 1);
		final float v4 = this.noise(integer_X + 1, integer_Y + 1);
		
		final float i1 = this.interpolate(v1, v2, fractional_X);
		final float i2 = this.interpolate(v3, v4, fractional_X);
		
		return this.interpolate(i1, i2, fractional_Y);
	}
	
	/**
	 * Calculates a random 2D Perlin-Noise Value between -1 and +1,<br>
	 * based on the cosine-interpolated value of random fractional corner values.<br>
	 * <br>
	 * Also, don't ask why all the variables are made final.<br>
	 * 
	 * @param x The X Position of the Noise
	 * @param y The Y Position of the Noise
	 * 
	 * @return A random 2D Perlin-Noise Value between -1 and +1 as float.
	 **/
	public final float cosineInterpolatedNoise(final float x, final float y)
	{
		final int integer_X = (int) x;
		final float fractional_X = x - integer_X;
		
		final int integer_Y = (int) y;
		final float fractional_Y = y - integer_Y;
		
		final float v1 = this.noise(integer_X, integer_Y);
		final float v2 = this.noise(integer_X + 1, integer_Y);
		final float v3 = this.noise(integer_X, integer_Y + 1);
		final float v4 = this.noise(integer_X + 1, integer_Y + 1);
		
		final float i1 = this.cosineInterpolate(v1, v2, fractional_X);
		final float i2 = this.cosineInterpolate(v3, v4, fractional_X);
		
		return this.cosineInterpolate(i1, i2, fractional_Y);
	}
	
	private final float interpolate(float a, float b, float x)
	{
		//linear
		return (a * (1 - x)) + (b * x);
	}
	
	private final float cosineInterpolate(float a, float b, float x)
	{
		// cosine
		float ft = x * 3.1415927f;
		float f = (float) ((1 - Math.cos(ft)) * 0.5f);
		return (a * (1 - f)) + (b * f);
	}
	
	/**
	 * Calculates a random 2D Perlin-Noise Value between -1 and +1,<br>
	 * based on the interpolated value of random fractional corner values that are added on different scales.<br>
	 * <br>
	 * Also, don't ask why all the variables are made final.<br>
	 * 
	 * @param x The X Position of the Noise
	 * @param y The Y Position of the Noise
	 * @param steps The amount of steps the simplex function should multiply together.
	 * @param multiply The value that the simplex function should use to calculate the steps together.
	 * 
	 * @return A random 2D Perlin-Noise Value between -1 and +1 as float.
	 **/
	public final float simplex2D(int steps, float multiply, float x, float y)
	{
		// the base-noise we use for this call
		float ex = this.interpolatedNoise(x, y);
		
		// the amount of steps we should go up with each iteration
		int up = 1;
		while(up < steps)
		{
			final float m = up * multiply * (x * y);
			ex *= 1 - this.interpolatedNoise(x / m, y / m);
			up++;
		}
		
		return ex;
	}
	
	/**
	 * @deprecated Do not use if possible. Use the method "simplex" instead!
	 * @deprecated This method does not work as intended. It does not work with negative input values.
	 **/
	public final float noisy(float x, float z, boolean addOnly)
	{
		if((x < 0) || (z < 0))
			return 0;
		
		float noisy = this.interpolatedNoise(x / 10, z / 10);
		noisy += this.interpolatedNoise(x / 20, z / 20);
		noisy += this.interpolatedNoise(x / 30, z / 30);
		noisy += this.interpolatedNoise(x / 40, z / 40);
		noisy += this.interpolatedNoise(x / 50, z / 50);
		noisy += this.interpolatedNoise(x / 60, z / 60);
		
		if(!addOnly)
		{
			noisy /= 6;
		}
		
		return noisy;
	}
	
}