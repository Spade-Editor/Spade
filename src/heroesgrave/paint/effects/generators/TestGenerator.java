package heroesgrave.paint.effects.generators;

import heroesgrave.paint.effects.Effect;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.edit.TestNoiseChange;

public class TestGenerator extends Effect
{
	public TestGenerator()
	{
		super("Test Generator");
	}
	
	public void perform(Layer layer)
	{
		layer.addChange(new TestNoiseChange());
	}
}
