package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

public class FloodPathChange extends PathChange
{

	public FloodPathChange(Point p, int colour)
	{
		super(p, colour);
		
	}
	
	@Override
	public void apply(RawImage image)
	{
		for (Point p : points)
		{
			floodFill(image, p.x, p.y, colour);
		}
	}
	
	// XXX: move to RawImage instance method
	private static void floodFill(RawImage image, int x, int y, int color)
	{
		if (x < 0 | y < 0 || x >= image.width || y >= image.height)
			return;
		
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		int targetColor = buffer[x + y * image.width];
		
		if (targetColor == color || (mask != null && !mask[x + y * image.width]))
			return;
		
		Queue<Integer> queue = new ArrayDeque<>();
		
		queue.add(x + y * image.width);
		
		while(!queue.isEmpty())
		{
			int n = queue.poll();
			if(buffer[n] == targetColor && (mask == null || mask[n]))
			{
				int ny = n / image.height;
				int nyo = ny * image.height;
				int nx = n % image.width;
				int w = nx, e = nx;
				while(w >= 0 && buffer[w + nyo] == targetColor && (mask == null || mask[w + nyo])) --w;
				while(e < image.width && buffer[e + nyo] == targetColor && (mask == null || mask[e + nyo])) ++e;
				w += 1 + nyo;
				e += nyo;
				for(int i = w; i < e; ++i)
				{
					buffer[i] = color;
					if (ny > 0 && buffer[i - image.height] == targetColor && (mask == null || mask[i - image.height]))
						queue.add(i - image.height);
					if (ny < image.height - 1 && buffer[i + image.height] == targetColor && (mask == null || mask[i + image.height]))
						queue.add(i + image.height);
				}
			}
		}
	}
}
