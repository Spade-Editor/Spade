// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.image.change;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A change that has no data and therefore can be contained in a single instance
 * 
 * @author HeroesGrave
 *
 */
public abstract class SingleChange extends SerialisedChange
{
	public abstract SingleChange getInstance();
	
	@Override
	public final void write(DataOutputStream out) throws IOException
	{
	}
	
	@Override
	public final void read(DataInputStream in) throws IOException
	{
	}
}
