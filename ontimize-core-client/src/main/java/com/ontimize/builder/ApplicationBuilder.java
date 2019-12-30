package com.ontimize.builder;

import com.ontimize.gui.Application;

public interface ApplicationBuilder {

	/**
	 * Create an {@link Application} from the definition specified
	 *
	 * @param uriFile
	 *            URI to the definition file
	 * @return
	 */
	public Application buildApplication(String uriFile);
}