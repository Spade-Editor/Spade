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
