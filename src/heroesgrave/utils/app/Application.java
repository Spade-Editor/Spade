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

package heroesgrave.utils.app;

public abstract class Application
{
	public int fps;
	public boolean terminate;
	
	public final void start()
	{
		init();
		loop();
	}
	
	private final void loop()
	{
		final double GAME_HERTZ = 60.0;
		final double TARGET_FPS = 60.0;
		
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		final double TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
		
		final int MAX_UPDATES_BEFORE_RENDER = 5;
		
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		int frameCount = 0;
		int lastSecond = (int) (lastUpdateTime / 1000000000);
		
		while(!terminate)
		{
			double now = System.nanoTime();
			int updateCount = 0;
			
			while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER)
			{
				update();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount++;
			}
			
			if(now - lastUpdateTime > TIME_BETWEEN_UPDATES)
			{
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}
			
			render();
			
			lastRenderTime = now;
			frameCount++;
			
			int thisSecond = (int) (lastUpdateTime / 1000000000);
			if(thisSecond > lastSecond)
			{
				fps = frameCount;
				frameCount = 0;
				lastSecond = thisSecond;
			}
			
			while(now - lastRenderTime < TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
			{
				Thread.yield();
				
				try
				{
					Thread.sleep(1);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				now = System.nanoTime();
			}
		}
		
		close(0);
	}
	
	public final void close(int arg0)
	{
		dispose();
		System.exit(arg0);
	}
	
	public abstract void init();
	
	public abstract void update();
	
	public abstract void render();
	
	public abstract void dispose();
	
	public static void launch(Application app)
	{
		app.start();
	}
}