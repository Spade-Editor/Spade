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

import java.util.Random;

/**
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 * Multi-Instance modifications by Longor1996.
 *
 * This could be speeded up even further, but it's useful as it is.
 *
 * Version 2012-03-09
 *
 * This code was placed in the public domain by its original author,
 * Stefan Gustavson. You may use it as you see fit, but
 * attribution is appreciated.
 *
 * @author Stefan Gustavson
 **/

public class SimplexNoise
{ // Simplex noise in 2D, 3D and 4D
	private final Grad grad3[] = {new Grad(1, 1, 0), new Grad(-1, 1, 0), new Grad(1, -1, 0), new Grad(-1, -1, 0), new Grad(1, 0, 1), new Grad(-1, 0, 1), new Grad(
			1, 0, -1), new Grad(-1, 0, -1), new Grad(0, 1, 1), new Grad(0, -1, 1), new Grad(0, 1, -1), new Grad(0, -1, -1)};
	
	private final Grad grad4[] = {new Grad(0, 1, 1, 1), new Grad(0, 1, 1, -1), new Grad(0, 1, -1, 1), new Grad(0, 1, -1, -1), new Grad(0, -1, 1, 1), new Grad(
			0, -1, 1, -1), new Grad(0, -1, -1, 1), new Grad(0, -1, -1, -1), new Grad(1, 0, 1, 1), new Grad(1, 0, 1, -1), new Grad(1, 0, -1, 1), new Grad(
			1, 0, -1, -1), new Grad(-1, 0, 1, 1), new Grad(-1, 0, 1, -1), new Grad(-1, 0, -1, 1), new Grad(-1, 0, -1, -1), new Grad(1, 1, 0, 1), new Grad(
			1, 1, 0, -1), new Grad(1, -1, 0, 1), new Grad(1, -1, 0, -1), new Grad(-1, 1, 0, 1), new Grad(-1, 1, 0, -1), new Grad(-1, -1, 0, 1), new Grad(
			-1, -1, 0, -1), new Grad(1, 1, 1, 0), new Grad(1, 1, -1, 0), new Grad(1, -1, 1, 0), new Grad(1, -1, -1, 0), new Grad(-1, 1, 1, 0), new Grad(
			-1, 1, -1, 0), new Grad(-1, -1, 1, 0), new Grad(-1, -1, -1, 0)};
	
	private final short p[] = {151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};
	
	private final double[] octaveDepthPositionPermutationTable;
	
	// To remove the need for index wrapping, double the permutation table length
	private final short perm[] = new short[512];
	private final short permMod12[] = new short[512];
	
	/**
	 * Create's a new Simplex-Noise instance, that can be used for pseudorandom number-generation.
	 * 
	 * @param seed The seed for this SimplexNoise generator. (Cannot be changed)
	 **/
	public SimplexNoise(long seed)
	{
		
		// Shuffle array by using random number generator made from the given seed.
		Random rnd = new Random(seed);
		for(int r = this.p.length; r > 1; r--)
		{
			swap(this.p, r - 1, rnd.nextInt(r));
		}
		
		// Copy Permutations
		for(int i = 0; i < 512; i++)
		{
			this.perm[i] = this.p[i & 255];
			this.permMod12[i] = (short) (this.perm[i] % 12);
		}
		
		octaveDepthPositionPermutationTable = new double[64];
		for(int i = 0; i < octaveDepthPositionPermutationTable.length; i++)
		{
			octaveDepthPositionPermutationTable[i] = rnd.nextFloat() * (int) (rnd.nextLong() & 0xFFFFFFFF);
		}
		
	}
	
	/**
	 * Swaps the two specified elements in the specified array.
	 */
	private static void swap(short[] arr, int i, int j)
	{
		short tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	
	// Skewing and unskewing factors for 2, 3, and 4 dimensions
	private static final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
	private static final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
	private static final double F3 = 1.0 / 3.0;
	private static final double G3 = 1.0 / 6.0;
	private static final double F4 = (Math.sqrt(5.0) - 1.0) / 4.0;
	private static final double G4 = (5.0 - Math.sqrt(5.0)) / 20.0;
	
	// This method is a *lot* faster than using (int)Math.floor(x)
	private final int fastfloor(double x)
	{
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}
	
	private final double dot(Grad g, double x, double y)
	{
		return (g.x * x) + (g.y * y);
	}
	
	private final double dot(Grad g, double x, double y, double z)
	{
		return (g.x * x) + (g.y * y) + (g.z * z);
	}
	
	private final double dot(Grad g, double x, double y, double z, double w)
	{
		return (g.x * x) + (g.y * y) + (g.z * z) + (g.w * w);
	}
	
	/**
	 * Perlin's Simplex-Noise Function in 2D.
	 * Octaves: Best around 4 to 16. (This number is used in a FOR-loop! Don't put it too high!)
	 * Amplitude: Best around 0.125 to 1.5
	 * Lacunarity: Best around 2
	 * Persistence: Best around 0.2 to 0.6
	 **/
	public final double noiseO2(double xin, double yin, int octaves, double amp, double lacunarity, double persistence)
	{
		double val = 0;
		double amplitude = amp; // 1
		
		for(int n = 0; n < octaves; n++)
		{
			val += noise(xin, yin) * amplitude;
			
			xin *= lacunarity;
			yin *= lacunarity;
			
			amplitude *= persistence;
		}
		
		return val;
	}
	
	// 2D simplex noise
	public final double noise(double xin, double yin)
	{
		// Noise contributions from the three corners
		double n0, n1, n2;
		
		// Skew the input space to determine which simplex cell we're in
		double s = (xin + yin) * F2; // Hairy factor for 2D
		
		int i = this.fastfloor(xin + s);
		int j = this.fastfloor(yin + s);
		
		double t = (i + j) * G2;
		
		// Unskew the cell origin back to (x,y) space
		double X0 = i - t;
		double Y0 = j - t;
		
		// The x,y distances from the cell origin
		double x0 = xin - X0;
		double y0 = yin - Y0;
		
		// For the 2D case, the simplex shape is an equilateral triangle.
		// Determine which simplex we are in.
		
		// Offsets for second (middle) corner of simplex in (i,j) coords
		int i1, j1;
		
		if(x0 > y0)
		{
			// lower triangle, XY order: (0,0)->(1,0)->(1,1)
			i1 = 1;
			j1 = 0;
		}
		else
		{
			// upper triangle, YX order: (0,0)->(0,1)->(1,1)
			i1 = 0;
			j1 = 1;
		}
		
		// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
		// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
		// c = (3-sqrt(3))/6
		
		// Offsets for middle corner in (x,y) unskewed coords
		double x1 = (x0 - i1) + G2;
		double y1 = (y0 - j1) + G2;
		
		// Offsets for last corner in (x,y) unskewed coords
		double x2 = (x0 - 1.0) + (2.0 * G2);
		double y2 = (y0 - 1.0) + (2.0 * G2);
		
		// Work out the hashed gradient indices of the three simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int gi0 = this.permMod12[ii + this.perm[jj]];
		int gi1 = this.permMod12[ii + i1 + this.perm[jj + j1]];
		int gi2 = this.permMod12[ii + 1 + this.perm[jj + 1]];
		
		// Calculate the contribution from the three corners
		double t0 = 0.5 - (x0 * x0) - (y0 * y0);
		
		if(t0 < 0)
		{
			n0 = 0.0;
			;
		}
		else
		{
			// (x,y) of grad3 used for 2D gradient
			t0 *= t0;
			n0 = t0 * t0 * this.dot(this.grad3[gi0], x0, y0);
		}
		
		double t1 = 0.5 - (x1 * x1) - (y1 * y1);
		
		if(t1 < 0)
		{
			n1 = 0.0;
			;
		}
		else
		{
			t1 *= t1;
			n1 = t1 * t1 * this.dot(this.grad3[gi1], x1, y1);
		}
		
		double t2 = 0.5 - (x2 * x2) - (y2 * y2);
		
		if(t2 < 0)
		{
			n2 = 0.0;
		}
		else
		{
			t2 *= t2;
			n2 = t2 * t2 * this.dot(this.grad3[gi2], x2, y2);
		}
		
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to return values in the interval [-1,1].
		return 70.0 * (n0 + n1 + n2);
	}
	
	// 3D simplex noise
	public final double noise(double xin, double yin, double zin)
	{
		// Noise contributions from the four corners
		double n0, n1, n2, n3;
		
		// Skew the input space to determine which simplex cell we're in
		// Very nice and simple skew factor for 3D
		double s = (xin + yin + zin) * F3;
		
		int i = this.fastfloor(xin + s);
		int j = this.fastfloor(yin + s);
		int k = this.fastfloor(zin + s);
		double t = (i + j + k) * G3;
		
		// Unskew the cell origin back to (x,y,z) space
		double X0 = i - t;
		double Y0 = j - t;
		double Z0 = k - t;
		
		// The x,y,z distances from the cell origin
		double x0 = xin - X0;
		double y0 = yin - Y0;
		double z0 = zin - Z0;
		
		// For the 3D case, the simplex shape is a slightly irregular tetrahedron.
		// Determine which simplex we are in.
		
		// Offsets for second corner of simplex in (i,j,k) coords
		int i1, j1, k1;
		
		// Offsets for third corner of simplex in (i,j,k) coords
		int i2, j2, k2;
		
		if(x0 >= y0)
		{
			if(y0 >= z0)
			{
				// X Y Z order
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			}
			else if(x0 >= z0)
			{
				// X Z Y order
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			}
			else
			{
				// Z X Y order
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			}
		}
		else if(y0 < z0)
		{
			// Z Y X order
			i1 = 0;
			j1 = 0;
			k1 = 1;
			i2 = 0;
			j2 = 1;
			k2 = 1;
		}
		else if(x0 < z0)
		{
			// Y Z X order
			i1 = 0;
			j1 = 1;
			k1 = 0;
			i2 = 0;
			j2 = 1;
			k2 = 1;
		}
		else
		{
			// Y X Z order
			i1 = 0;
			j1 = 1;
			k1 = 0;
			i2 = 1;
			j2 = 1;
			k2 = 0;
		}
		
		// A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
		// a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z),
		// and  a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z),
		// where: c = 1/6.
		
		// Offsets for second corner in (x,y,z) coords
		double x1 = (x0 - i1) + G3;
		double y1 = (y0 - j1) + G3;
		double z1 = (z0 - k1) + G3;
		
		// Offsets for third corner in (x,y,z) coords
		double x2 = (x0 - i2) + (2.0 * G3);
		double y2 = (y0 - j2) + (2.0 * G3);
		double z2 = (z0 - k2) + (2.0 * G3);
		
		// Offsets for last corner in (x,y,z) coords
		double x3 = (x0 - 1.0) + (3.0 * G3);
		double y3 = (y0 - 1.0) + (3.0 * G3);
		double z3 = (z0 - 1.0) + (3.0 * G3);
		
		// Work out the hashed gradient indices of the four simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		
		int gi0 = this.permMod12[ii + this.perm[jj + this.perm[kk]]];
		int gi1 = this.permMod12[ii + i1 + this.perm[jj + j1 + this.perm[kk + k1]]];
		int gi2 = this.permMod12[ii + i2 + this.perm[jj + j2 + this.perm[kk + k2]]];
		int gi3 = this.permMod12[ii + 1 + this.perm[jj + 1 + this.perm[kk + 1]]];
		
		// Calculate the contribution from the four corners
		double t0 = 0.6 - (x0 * x0) - (y0 * y0) - (z0 * z0);
		if(t0 < 0)
		{
			n0 = 0.0;
		}
		else
		{
			t0 *= t0;
			n0 = t0 * t0 * this.dot(this.grad3[gi0], x0, y0, z0);
		}
		double t1 = 0.6 - (x1 * x1) - (y1 * y1) - (z1 * z1);
		if(t1 < 0)
		{
			n1 = 0.0;
		}
		else
		{
			t1 *= t1;
			n1 = t1 * t1 * this.dot(this.grad3[gi1], x1, y1, z1);
		}
		double t2 = 0.6 - (x2 * x2) - (y2 * y2) - (z2 * z2);
		if(t2 < 0)
		{
			n2 = 0.0;
		}
		else
		{
			t2 *= t2;
			n2 = t2 * t2 * this.dot(this.grad3[gi2], x2, y2, z2);
		}
		double t3 = 0.6 - (x3 * x3) - (y3 * y3) - (z3 * z3);
		if(t3 < 0)
		{
			n3 = 0.0;
		}
		else
		{
			t3 *= t3;
			n3 = t3 * t3 * this.dot(this.grad3[gi3], x3, y3, z3);
		}
		
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to stay just inside [-1,1]
		return 32.0 * (n0 + n1 + n2 + n3);
	}
	
	// 4D simplex noise, better simplex rank ordering method 2012-03-09
	public final double noise(double x, double y, double z, double w)
	{
		// Noise contributions from the five corners
		double n0, n1, n2, n3, n4;
		
		// Skew the (x,y,z,w) space to determine which cell of 24 simplices we're in
		// Factor for 4D skewing
		double s = (x + y + z + w) * F4;
		int i = this.fastfloor(x + s);
		int j = this.fastfloor(y + s);
		int k = this.fastfloor(z + s);
		int l = this.fastfloor(w + s);
		
		// Factor for 4D unskewing
		double t = (i + j + k + l) * G4;
		
		// Unskew the cell origin back to (x,y,z,w) space
		double X0 = i - t;
		double Y0 = j - t;
		double Z0 = k - t;
		double W0 = l - t;
		
		// The x,y,z,w distances from the cell origin
		double x0 = x - X0;
		double y0 = y - Y0;
		double z0 = z - Z0;
		double w0 = w - W0;
		
		// For the 4D case, the simplex is a 4D shape I won't even try to describe.
		// To find out which of the 24 possible simplices we're in, we need to
		// determine the magnitude ordering of x0, y0, z0 and w0.
		
		// Six pair-wise comparisons are performed between each possible pair
		// of the four coordinates, and the results are used to rank the numbers.
		int rankx = 0;
		int ranky = 0;
		int rankz = 0;
		int rankw = 0;
		if(x0 > y0)
		{
			rankx++;
		}
		else
		{
			ranky++;
		}
		if(x0 > z0)
		{
			rankx++;
		}
		else
		{
			rankz++;
		}
		if(x0 > w0)
		{
			rankx++;
		}
		else
		{
			rankw++;
		}
		if(y0 > z0)
		{
			ranky++;
		}
		else
		{
			rankz++;
		}
		if(y0 > w0)
		{
			ranky++;
		}
		else
		{
			rankw++;
		}
		if(z0 > w0)
		{
			rankz++;
		}
		else
		{
			rankw++;
		}
		
		// The integer offsets for the second simplex corner
		int i1, j1, k1, l1;
		
		// The integer offsets for the third simplex corner
		int i2, j2, k2, l2;
		
		// The integer offsets for the fourth simplex corner
		int i3, j3, k3, l3;
		
		// simplex[c] is a 4-vector with the numbers 0, 1, 2 and 3 in some order.
		// Many values of c will never occur, since e.g. x>y>z>w makes x<z, y<w and x<w are
		// impossible. Only the 24 indices which have non-zero entries make any sense.
		// We use a thresholding to set the coordinates in turn from the largest magnitude.
		
		// Rank 3 denotes the largest coordinate.
		i1 = rankx >= 3 ? 1 : 0;
		j1 = ranky >= 3 ? 1 : 0;
		k1 = rankz >= 3 ? 1 : 0;
		l1 = rankw >= 3 ? 1 : 0;
		
		// Rank 2 denotes the second largest coordinate.
		i2 = rankx >= 2 ? 1 : 0;
		j2 = ranky >= 2 ? 1 : 0;
		k2 = rankz >= 2 ? 1 : 0;
		l2 = rankw >= 2 ? 1 : 0;
		
		// Rank 1 denotes the second smallest coordinate.
		i3 = rankx >= 1 ? 1 : 0;
		j3 = ranky >= 1 ? 1 : 0;
		k3 = rankz >= 1 ? 1 : 0;
		l3 = rankw >= 1 ? 1 : 0;
		
		// The fifth corner has all coordinate offsets = 1, so no need to
		// compute that.
		
		// Offsets for second corner in (x,y,z,w) coords
		double x1 = (x0 - i1) + G4;
		double y1 = (y0 - j1) + G4;
		double z1 = (z0 - k1) + G4;
		double w1 = (w0 - l1) + G4;
		
		// Offsets for third corner in (x,y,z,w) coords
		double x2 = (x0 - i2) + (2.0 * G4);
		double y2 = (y0 - j2) + (2.0 * G4);
		double z2 = (z0 - k2) + (2.0 * G4);
		double w2 = (w0 - l2) + (2.0 * G4);
		
		// Offsets for fourth corner in (x,y,z,w) coords
		double x3 = (x0 - i3) + (3.0 * G4);
		double y3 = (y0 - j3) + (3.0 * G4);
		double z3 = (z0 - k3) + (3.0 * G4);
		double w3 = (w0 - l3) + (3.0 * G4);
		
		// Offsets for last corner in (x,y,z,w) coords
		double x4 = (x0 - 1.0) + (4.0 * G4);
		double y4 = (y0 - 1.0) + (4.0 * G4);
		double z4 = (z0 - 1.0) + (4.0 * G4);
		double w4 = (w0 - 1.0) + (4.0 * G4);
		
		// Work out the hashed gradient indices of the five simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		int ll = l & 255;
		
		// hashing
		int gi0 = this.perm[ii + this.perm[jj + this.perm[kk + this.perm[ll]]]] % 32;
		int gi1 = this.perm[ii + i1 + this.perm[jj + j1 + this.perm[kk + k1 + this.perm[ll + l1]]]] % 32;
		int gi2 = this.perm[ii + i2 + this.perm[jj + j2 + this.perm[kk + k2 + this.perm[ll + l2]]]] % 32;
		int gi3 = this.perm[ii + i3 + this.perm[jj + j3 + this.perm[kk + k3 + this.perm[ll + l3]]]] % 32;
		int gi4 = this.perm[ii + 1 + this.perm[jj + 1 + this.perm[kk + 1 + this.perm[ll + 1]]]] % 32;
		
		// Calculate the contribution from the five corners
		
		// Contribution A
		double t0 = 0.6 - (x0 * x0) - (y0 * y0) - (z0 * z0) - (w0 * w0);
		if(t0 < 0)
		{
			n0 = 0.0;
		}
		else
		{
			t0 *= t0;
			n0 = t0 * t0 * this.dot(this.grad4[gi0], x0, y0, z0, w0);
		}
		
		// Contribution B
		double t1 = 0.6 - (x1 * x1) - (y1 * y1) - (z1 * z1) - (w1 * w1);
		if(t1 < 0)
		{
			n1 = 0.0;
		}
		else
		{
			t1 *= t1;
			n1 = t1 * t1 * this.dot(this.grad4[gi1], x1, y1, z1, w1);
		}
		
		// Contribution C
		double t2 = 0.6 - (x2 * x2) - (y2 * y2) - (z2 * z2) - (w2 * w2);
		if(t2 < 0)
		{
			n2 = 0.0;
		}
		else
		{
			t2 *= t2;
			n2 = t2 * t2 * this.dot(this.grad4[gi2], x2, y2, z2, w2);
		}
		
		// Contribution D
		double t3 = 0.6 - (x3 * x3) - (y3 * y3) - (z3 * z3) - (w3 * w3);
		if(t3 < 0)
		{
			n3 = 0.0;
		}
		else
		{
			t3 *= t3;
			n3 = t3 * t3 * this.dot(this.grad4[gi3], x3, y3, z3, w3);
		}
		
		// Contribution E
		double t4 = 0.6 - (x4 * x4) - (y4 * y4) - (z4 * z4) - (w4 * w4);
		if(t4 < 0)
		{
			n4 = 0.0;
		}
		else
		{
			t4 *= t4;
			n4 = t4 * t4 * this.dot(this.grad4[gi4], x4, y4, z4, w4);
		}
		
		// Sum up and scale the result to cover the range [-1,1]
		return 27.0 * (n0 + n1 + n2 + n3 + n4);
	}
	
	// Inner class to speed up gradient computations
	// (array access is a lot slower than member access)
	private final class Grad
	{
		double x, y, z, w;
		
		Grad(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		Grad(double x, double y, double z, double w)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
	}
}