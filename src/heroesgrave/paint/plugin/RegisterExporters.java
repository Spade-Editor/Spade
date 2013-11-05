package heroesgrave.paint.plugin;

import heroesgrave.paint.main.ImageExporter;

public class RegisterExporters {
	
	public void register(ImageExporter exporter){
		ImageExporter.add(exporter);
	}
	
}
