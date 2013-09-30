package heroesgrave.utils.app;

import heroesgrave.utils.Disposable;

import java.util.ArrayList;

public class DisposableHandler
{
	private static ArrayList<Disposable> disposables = new ArrayList<Disposable>();
	
	private DisposableHandler()
	{
		
	}
	
	public static void add(Disposable d)
	{
		disposables.add(d);
	}
	
	public static void remove(Disposable d)
	{
		disposables.remove(d);
	}
	
	public static void dispose()
	{
		while(!disposables.isEmpty())
		{
			disposables.remove(0).dispose();
		}
	}
}
