package heroesgrave.paint.image.change.edit;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IGeneratorChange;
import heroesgrave.utils.misc.RandomUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class TestNoiseChange extends IGeneratorChange
{
	public long seed;
	
	public TestNoiseChange()
	{
		this.seed = RandomUtils.rLong();
	}
	
	public void write(DataOutputStream out) throws IOException
	{
	}
	
	public void read(DataInputStream in) throws IOException
	{
	}
	
	public RawImage generate(int width, int height)
	{
		RawImage image = new RawImage(width, height);
		Random random = new Random(seed);
		
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				int color = 0xff000000;
				color |= ((i * 256) / width) << 0;
				color |= ((j * 256) / height) << 8;
				color |= random.nextInt(0xff) << 16;
				image.setPixel(i, j, color);
			}
		}
		
		return image;
	}
}
