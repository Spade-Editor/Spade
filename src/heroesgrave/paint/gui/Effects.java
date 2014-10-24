package heroesgrave.paint.gui;

import heroesgrave.paint.gui.ToolMenu.EffectMenuItem;
import heroesgrave.paint.tools.effects.Effect;
import heroesgrave.paint.tools.effects.Invert;

import com.alee.laf.menu.WebMenu;

public class Effects
{
	public WebMenu effects, operations;
	
	public void addEffect(Effect effect, String shortcut)
	{
		effects.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void addOperation(Effect effect, String shortcut)
	{
		operations.add(new EffectMenuItem(effect.name, effect, shortcut));
	}
	
	public void registerEffects()
	{
		addEffect(new Invert("Invert Colour"), "I");
		//PluginManager.instance.registerEffects(this);
	}
}
