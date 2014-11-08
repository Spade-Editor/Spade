package heroesgrave.utils.misc;

public class Version
{
	private int major, minor;
	private char revision;
	private String type;
	
	public Version(int major, int minor)
	{
		this(major, minor, null);
	}
	
	public Version(int major, int minor, String type)
	{
		this(major, minor, (char) 0, type);
	}
	
	public Version(int major, int minor, char revision)
	{
		this(major, minor, revision, null);
	}
	
	public Version(int major, int minor, char revision, String type)
	{
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.type = type;
	}
	
	public Version(String string)
	{
		this.major = -1;
		this.minor = -1;
		this.revision = 0;
		this.type = string;
	}
	
	public boolean isGreater(Version other)
	{
		if(this.major < other.major)
			return false;
		else if(this.major > other.major)
			return true;
		else
		{
			if(this.minor < other.minor)
				return false;
			else if(this.minor > other.minor)
				return true;
			else
			{
				if(this.revision < other.revision)
					return false;
				else if(this.revision > other.revision)
					return true;
				else
				{
					if(this.type() <= other.type())
						return false;
					else
						return true;
				}
			}
		}
	}
	
	private int type()
	{
		if(this.type == null)
			return 16;
		if(this.type.startsWith("RC."))
		{
			return 3 + Integer.parseInt(this.type.substring(3));
		}
		switch(this.type)
		{
			case "Dev":
				return 1;
			case "Alpha":
				return 2;
			case "Beta":
				return 3;
			default:
				return 15;
		}
	}
	
	public static Version parse(String version)
	{
		if(version.startsWith("\"") && version.endsWith("\""))
		{
			return new Version(version.substring(1, version.length() - 1));
		}
		try
		{
			String[] dot = version.split("\\.");
			String[] dash = dot[1].split("-");
			
			String major = dot[0];
			String minor = dash[0];
			String type = null;
			
			if(dash.length == 2)
			{
				type = dash[1];
			}
			
			char maybeRevision = minor.toLowerCase().charAt(minor.length() - 1);
			if(maybeRevision >= 'a' && maybeRevision <= 'z')
			{
				return new Version(Integer.parseInt(major), Integer.parseInt(minor.substring(0, minor.length() - 1)), maybeRevision, type);
			}
			else
			{
				return new Version(Integer.parseInt(major), Integer.parseInt(minor), type);
			}
		}
		catch(Exception e)
		{
			System.err.println("Badly formatted version: " + version);
			return null;
		}
	}
	
	public String toString()
	{
		if(major < 0 || minor < 0)
		{
			return type;
		}
		String v = major + "." + minor;
		if(revision != 0)
		{
			v += revision;
		}
		if(type != null)
		{
			v += "-" + type;
		}
		return v;
	}
}
