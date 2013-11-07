package heroesgrave.utils.misc;

public class StringUtil {
	
	/**
	 * Method to transform raw byte-count into a human-readable format.
	 * 
	 * @author aioobe
	 * (from <a href="http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java">Stackoverflow</a>})
	 **/
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		
		if (bytes < unit)
			return bytes + " B";
		
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
}
