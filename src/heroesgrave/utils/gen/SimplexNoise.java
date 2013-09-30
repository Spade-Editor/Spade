package heroesgrave.utils.gen;

import heroesgrave.utils.math.MathUtils;

import java.util.Random;

public class SimplexNoise
{
	private static final double SQRT3 = Math.sqrt(3.0);

	private static final double F2 = 0.5 * (SQRT3 - 1.0);
	private static final double G2 = (3.0 - SQRT3) / 6.0;
	private static final double G22 = G2 * 2.0 - 1.0;

	private static final double F3 = 1 / 3.0;
	private static final double G3 = 1 / 6.0;

	private static final int grad[][] = new int[][]{new int[]{1, 1, 0}, new int[]{-1, 1, 0}, new int[]{1, -1, 0}, new int[]{-1, -1, 0}, new int[]{1, 0, 1}, new int[]{-1, 0, 1},
			new int[]{1, 0, -1}, new int[]{-1, 0, -1}, new int[]{0, 1, 1}, new int[]{0, -1, 1}, new int[]{0, 1, -1}, new int[]{0, -1, -1}};

	private short[] perm = new short[512];

	public SimplexNoise(long seed)
	{
		setSeed(seed);
	}

	public final void setSeed(long seed)
	{
		Random random = new Random(seed);
		for(int i = 0; i < 256; i++)
		{
			short j = (short) random.nextInt(256);
			perm[i] = j;
			perm[256 + i] = j;
		}
	}

	public static double dot(int[] grad, double x, double y)
	{
		return grad[0] * x + grad[1] * y;
	}

	public static double dot(int[] grad, double x, double y, double z)
	{
		return grad[0] * x + grad[1] * y + grad[2] * z;
	}

	public final double noise(double x, double y)
	{
		double n0, n1, n2;
		n0 = n1 = n2 = 0;

		double s = (x + y) * F2;
		int i = MathUtils.floor(x + s);
		int j = MathUtils.floor(y + s);

		double t = (i + j) * G2;
		double x0 = x - (i - t);
		double y0 = y - (j - t);

		int i1, j1;
		if(x0 > y0)
		{
			i1 = 1;
			j1 = 0;
		}
		else
		{
			i1 = 0;
			j1 = 1;
		}

		double x1 = x0 - i1 + G2;
		double y1 = y0 - j1 + G2;
		double x2 = x0 + G22;
		double y2 = y0 + G22;

		int ii = i & 0xFF;
		int jj = j & 0xFF;

		double t0 = 0.5 - x0 * x0 - y0 * y0;
		double t1 = 0.5 - x1 * x1 - y1 * y1;
		double t2 = 0.5 - x2 * x2 - y2 * y2;

		if(t0 > 0)
		{
			t0 *= t0;
			int gi0 = perm[ii + perm[jj]] % 12;
			n0 = t0 * t0 * dot(grad[gi0], x0, y0);
		}

		if(t1 > 0)
		{
			t1 *= t1;
			int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
			n1 = t1 * t1 * dot(grad[gi1], x1, y1);
		}

		if(t2 > 0)
		{
			t2 *= t2;
			int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
			n2 = t2 * t2 * dot(grad[gi2], x2, y2);
		}

		return 70.0 * (n0 + n1 + n2);
	}

	public final double noise(double x, double y, double z)
	{
		double n0, n1, n2, n3;
		n0 = n1 = n2 = n3 = 0;

		double s = (x + y + z) * F3;
		int i = MathUtils.floor(x + s);
		int j = MathUtils.floor(y + s);
		int k = MathUtils.floor(z + s);

		double t = (i + j + k) * G3;
		double x0 = x - (i - t);
		double y0 = y - (j - t);
		double z0 = z - (k - t);

		int i1, j1, k1;
		int i2, j2, k2;

		if(x0 >= y0)
		{
			if(y0 >= z0)
			{
				i1 = i2 = j2 = 1;
				j1 = k1 = k2 = 0;
			}
			else if(x0 >= z0)
			{
				i1 = i2 = k2 = 1;
				j1 = k1 = j2 = 0;
			}
			else
			{
				k1 = i2 = k2 = 1;
				i1 = j1 = j2 = 0;
			}
		}
		else
		{
			if(y0 < z0)
			{
				k1 = j2 = k2 = 1;
				i1 = j1 = i2 = 0;
			}
			else if(x0 < z0)
			{
				j1 = j2 = k2 = 1;
				i1 = k1 = i2 = 0;
			}
			else
			{
				j1 = i2 = j2 = 1;
				i1 = k1 = k2 = 0;
			}
		}

		double x1 = x0 - i1 + G3;
		double y1 = y0 - j1 + G3;
		double z1 = z0 - k1 + G3;
		double x2 = x0 - i2 + 2.0 * G3;
		double y2 = y0 - j2 + 2.0 * G3;
		double z2 = z0 - k2 + 2.0 * G3;
		double x3 = x0 - 1.0 + 3.0 * G3;
		double y3 = y0 - 1.0 + 3.0 * G3;
		double z3 = z0 - 1.0 + 3.0 * G3;

		int ii = i & 0xFF;
		int jj = j & 0xFF;
		int kk = k & 0xFF;

		int gi0 = perm[ii + perm[jj + perm[kk]]] % 12;
		int gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1]]] % 12;
		int gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2]]] % 12;
		int gi3 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1]]] % 12;

		double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
		double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
		double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
		double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;

		if(t0 >= 0)
		{
			t0 *= t0;
			n0 = t0 * t0 * dot(grad[gi0], x0, y0, z0);
		}

		if(t1 >= 0)
		{
			t1 *= t1;
			n1 = t1 * t1 * dot(grad[gi1], x1, y1, z1);
		}

		if(t2 >= 0)
		{
			t2 *= t2;
			n2 = t2 * t2 * dot(grad[gi2], x2, y2, z2);
		}

		if(t3 >= 0)
		{
			t3 *= t3;
			n3 = t3 * t3 * dot(grad[gi3], x3, y3, z3);
		}

		return 32.0 * (n0 + n1 + n2 + n3);
	}
}
