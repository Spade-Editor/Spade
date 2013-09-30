package l19.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class WorkingDirectory {
	
	/** INTERNAL-ENUM, IGNORE PLEASE **/
	private enum OS
	{
		WINDOWS, SOLARIS, LINUX, MACOS, UNKNOWN;
	}
	
	/** INTERNAL-METHOD, IGNORE PLEASE **/
	private static OS getPlatform()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		
		if (osName.contains("win")) return OS.WINDOWS;
		if (osName.contains("mac")) return OS.MACOS;
		if (osName.contains("linux")) return OS.LINUX;
		if (osName.contains("unix")) return OS.LINUX;
		
		return OS.UNKNOWN;
	}
	
	/**
	 * Returns the WorkingDirectory for the given Application.<br>
	 * The given Folder can then be used as root for all application data.<br>
	 **/
	public static File getWorkingDirectoryFor(final String APPLICATION_NAME) {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		
		switch (getPlatform().ordinal()) {
			case 0:
				String applicationData = System.getenv("APPDATA");
				String folder = applicationData != null ? applicationData : userHome;
				
				workingDirectory = new File(folder, "."+APPLICATION_NAME+"/");
			break;
			case 1:
			case 2:
				workingDirectory = new File(userHome, "."+APPLICATION_NAME+"/");
			break;
			case 3:
				workingDirectory = new File(userHome, "Library/Application Support/"+APPLICATION_NAME+"");
			break;
			default:
				workingDirectory = new File(userHome, "."+APPLICATION_NAME+"/");
		}
		
		if(workingDirectory.exists()){
			if(workingDirectory.isFile())
				System.err.println("[WARNING] WorkingDirectory is a File!");
			if(workingDirectory.isHidden())
				System.err.println("[WARNING] WorkingDirectory is hidden!");
		} else{
			// Create the WorkingDirectory
			workingDirectory.mkdirs();
			System.out.println("[INFO] Created WorkingDirectory for: " + APPLICATION_NAME);
		}
		
		return workingDirectory;
	}
	
	/**
	 * Get the installation directory of the application
	 * 
	 * @author stackoverflow.com
	 * 
	 * @param clazz The class that contains the main() method
	 * @return The installation directory of the application.
	 */
	public static File getExecutingClassParentFolder(Class<?> clazz){
		URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
		File file = null;
		
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			// Let's trust the JDK to get it rigth.
		}
		
		if(file == null)
			throw new Error("Unable to locate class file position.");
		
		if (file.isDirectory())
			// Application consists of loose class files
			return file;
		else
			// Application is packaged in a JAR file
			return file.getParentFile();
	}
	
	
}
