/**
 * 
 */
package de.saxsys.jfx.chattorama.resource;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author stefan.illgen
 * 
 */
public class ImageRegistry {

	private static ImageRegistry INSTANCE;

	/**
	 * 
	 * @return
	 */
	public static ImageRegistry instance() {
		return (INSTANCE == null) ? INSTANCE = new ImageRegistry() : INSTANCE;
	}

	private Map<Integer, Image> allImages = new HashMap<Integer, Image>();

	private ImageRegistry() {
		// nothing
	}

	/**
	 * 
	 * @param key
	 * @param fullPath
	 * @return
	 */
	public static synchronized Image get(final Integer key,
			final String fullPath) {

		if (key == null) {
			throw new NullPointerException("Parameter key must not be null!");
		}

		Image image = instance().allImages.get(key);
		if (image == null) {
			URL imageURL = instance().resolveURL(fullPath);
			image = new Image(imageURL.toExternalForm());
		}
		// register
		instance().allImages.put(key, image);

		return image;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static synchronized Image get(final Images key) {
		return get(key.ordinal(), key.getFullPath());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static synchronized ImageView getImageView(
			final Images key) {
		return new ImageView(get(key));
	}

	private URL resolveURL(String fullpath) {
		URL imageURL = this.getClass().getResource(fullpath);
		if (imageURL == null) {
			throw new IllegalArgumentException(
					"No icon found for the image constant with path '"
							+ fullpath + "'");
		}
		return imageURL;
	}

	/**
	 * 
	 */
	public void dispose() {
		Platform.runLater(new Runnable() {
			public void run() {
				allImages.clear();
				allImages = null;
			}
		});
	}

}
