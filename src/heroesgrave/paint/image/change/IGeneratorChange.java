package heroesgrave.paint.image.change;

import heroesgrave.paint.image.RawImage;

public abstract class IGeneratorChange extends IImageChange
{
	public abstract RawImage generate(int width, int height);
	
	public RawImage apply(RawImage image)
	{
		image.copyRegion(this.generate(image.width, image.height));
		return image;
	}
}
