package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Same as a PathChange (a series of points), but flood-fills the image starting at each point.
 * 
 * @author BurntPizza
 *
 */
public class FloodPathChange extends PathChange
{

	public FloodPathChange(Point p, int colour)
	{
		super(p, colour);
		
	}
	
	public FloodPathChange(short x, short y, int colour)
	{
		super(x, y, colour);
		
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
	// scan line flood fill as described in Wikipedia
	private static void floodFill(RawImage image, int x, int y, int color)
	{
		if (x < 0 | y < 0 || x >= image.width || y >= image.height)
			return;
		
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		int targetColor = buffer[x + y * image.width];
		
		if (targetColor == color || (mask != null && !mask[x + y * image.width]))
			return;
		
		// if more speed is necessary, implement an IntQueue
		Queue<Integer> queue = new ArrayDeque<>();
		
		queue.add(x + y * image.width);

		while(!queue.isEmpty())
		{
			int n = queue.poll();
			if(buffer[n] == targetColor) // mask predicate is guaranteed here
			{
				int ny = n / image.height;
				int nyo = ny * image.height;
				int nxl = nyo + image.width;
				int w = n, e = n;
				// scan out on each side of current pixel
				while(w >= nyo && buffer[w] == targetColor && (mask == null || mask[w])) --w;
				while(e < nxl && buffer[e] == targetColor && (mask == null || mask[e])) ++e;
				// fill in between
				for(int i = w + 1; i < e; ++i)
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
