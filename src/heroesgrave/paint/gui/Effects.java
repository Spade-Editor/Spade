package heroesgrave.paint.gui;

import heroesgrave.paint.effects.Effect;
import heroesgrave.paint.effects.Invert;
import heroesgrave.paint.effects.generators.TestGenerator;
import heroesgrave.paint.gui.ToolMenu.EffectMenuItem;

import com.alee.laf.menu.WebMenu;

public class Effects
{
	public WebMenu effects, generators, operations;
	
	public void addEffect(Effect effect, String shortcut)
	{
		effects.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addGenerator(Effect effect, String shortcut)
	{
		generators.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addOperation(Effect effect, String shortcut)
	{
		operations.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void registerEffects()
	{
		addEffect(new Invert("Invert Colour"), "I");
		addGenerator(new TestGenerator(), null);
		//PluginManager.instance.registerEffects(this);
	}
}
