package fr.univnantes.termsuite.api;

import java.nio.file.Path;
import java.util.Optional;

/**
 * 
 * A Java-bean for the configuration of TermSuite resource access.
 * 
 * @author Damien Cram
 *
 */
public class ResourceOptions {

	private Optional<Path> resourceDirectory = Optional.empty(); 
	private Optional<Path> resourceJar = Optional.empty();
	
	public Optional<Path> getResourceDirectory() {
		return resourceDirectory;
	}

	public ResourceOptions setResourceDirectory(Path resourceDirectory) {
		this.resourceDirectory = Optional.of(resourceDirectory);
		return this;
	}
	
	public Optional<Path> getResourceJar() {
		return resourceJar;
	}
	
	public ResourceOptions setResourceJar(Path resourceJar) {
		this.resourceJar = Optional.of(resourceJar);
		return this;
	} 
	
}
