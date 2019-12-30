package com.ontimize.gui.images;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * The Interface IImageManager.
 */
public interface IImageManager {

	/**
	 * Gets the icon.
	 *
	 * @param icon
	 *            the icon
	 * @return the icon
	 */
	ImageIcon getIcon(String icon);

	/**
	 * Reset image cache.
	 */
	void resetImageCache();

	/**
	 * Gets the icon url.
	 *
	 * @param icon
	 *            the icon
	 * @return the icon url
	 */
	URL getIconURL(String icon);

	/**
	 * Adds the base image path.
	 *
	 * @param path
	 *            the path
	 */
	void addBaseImagePath(String path);

	/**
	 * Gets the base image paths.
	 *
	 * @return the base image paths
	 */
	List<String> getBaseImagePaths();

	/**
	 * Removes the base image path.
	 *
	 * @param path
	 *            the path
	 */
	void removeBaseImagePath(String path);

	/**
	 * Transparent.
	 *
	 * @param icon
	 *            the icon
	 * @param trans
	 *            the trans
	 * @return the image icon
	 */
	ImageIcon transparent(ImageIcon icon, float trans);

	/**
	 * Brighter.
	 *
	 * @param icon
	 *            the icon
	 * @return the image icon
	 */
	ImageIcon brighter(ImageIcon icon);

	/**
	 * Darker.
	 *
	 * @param icon
	 *            the icon
	 * @return the image icon
	 */
	ImageIcon darker(ImageIcon icon);

	/**
	 * Gets the blur image.
	 *
	 * @param image
	 *            the image
	 * @param radius
	 *            the radius
	 * @return the blur image
	 */
	BufferedImage getBlurImage(BufferedImage image, int radius);
}