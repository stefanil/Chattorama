package de.saxsys.jfx.chattorama.resource;


/**
 * 
 * @author stefan.illgen
 * 
 */
public enum Images {

	// ### Image Configuration ###

	RHINO("rhino.png"), 
	BULL("bull.png"), 
	EAGLE("eagle.png"),
	SEND_POST("sendPost.png"), 
	CRY("cry.png"), 
	LAUGH("laugh.png"), 
	SMILE("smile.png");

	/**
	 * The base directory which contains all images (eventually using sub
	 * folders).
	 */
	public static final String IMAGE_BASE_DIR = "/de/saxsys/jfx/chattorama/img";

	// ### public API ###

	public String getFullPath() {
		return fullpath;
	}

	// ### private API ###

	private String fullpath;

	private Images(String path) {
		fullpath = IMAGE_BASE_DIR + "/" + path;
	}

}
