// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.plugin;

import heroesgrave.paint.editing.Effect;
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.gui.Effects;
import heroesgrave.paint.gui.Tools;
import heroesgrave.paint.image.blend.BlendMode;
import heroesgrave.paint.io.HistoryIO;
import heroesgrave.paint.io.ImageExporter;
import heroesgrave.paint.io.ImageImporter;
import heroesgrave.paint.io.Serialised;
import heroesgrave.paint.main.Paint;
import heroesgrave.utils.misc.Pair;

import java.util.ArrayList;

public class Registrar
{
	private ArrayList<Pair<Tool, Character>> tools = new ArrayList<Pair<Tool, Character>>();
	private ArrayList<Pair<Effect, Character>> effects = new ArrayList<Pair<Effect, Character>>();
	private ArrayList<Pair<Effect, Character>> ops = new ArrayList<Pair<Effect, Character>>();
	private ArrayList<Pair<Effect, Character>> generators = new ArrayList<Pair<Effect, Character>>();
	private ArrayList<Class<? extends Serialised>> serialisers = new ArrayList<Class<? extends Serialised>>();
	private ArrayList<BlendMode> blendmodes = new ArrayList<BlendMode>();
	private ArrayList<ImageExporter> exporters = new ArrayList<ImageExporter>();
	private ArrayList<ImageImporter> importers = new ArrayList<ImageImporter>();
	
	public void registerTool(Tool tool, Character key)
	{
		tools.add(new Pair<Tool, Character>(tool, key));
	}
	
	public void registerEffect(Effect effect, Character key)
	{
		effects.add(new Pair<Effect, Character>(effect, key));
	}
	
	public void registerOperation(Effect op, Character key)
	{
		ops.add(new Pair<Effect, Character>(op, key));
	}
	
	public void registerGenerator(Effect gen, Character key)
	{
		generators.add(new Pair<Effect, Character>(gen, key));
	}
	
	public void registerSerialiser(Class<? extends Serialised> c)
	{
		serialisers.add(c);
	}
	
	public void registerBlendMode(BlendMode mode)
	{
		blendmodes.add(mode);
	}
	
	public void registerImporter(ImageImporter importer)
	{
		importers.add(importer);
	}
	
	public void registerExporter(ImageExporter exporter)
	{
		exporters.add(exporter);
	}
	
	protected void completeRegistration(Tools tools, Effects effects)
	{
		for(Pair<Tool, Character> pair : this.tools)
		{
			tools.addTool(pair.t, pair.u);
		}
		for(Pair<Effect, Character> pair : this.effects)
		{
			effects.addEffect(pair.t, pair.u);
		}
		for(Pair<Effect, Character> pair : this.ops)
		{
			effects.addOperation(pair.t, pair.u);
		}
		for(Pair<Effect, Character> pair : this.generators)
		{
			effects.addGenerator(pair.t, pair.u);
		}
		for(Class<? extends Serialised> c : this.serialisers)
		{
			HistoryIO.registerClass(c);
		}
		for(BlendMode mode : blendmodes)
		{
			BlendMode.addBlendMode(mode);
		}
		Paint.main.gui.layers.updateBlendModes();
		for(ImageExporter exporter : exporters)
		{
			ImageExporter.add(exporter);
		}
		for(ImageImporter importer : importers)
		{
			ImageImporter.add(importer);
		}
	}
}
