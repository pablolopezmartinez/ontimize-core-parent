package com.ontimize.gui;

import javax.swing.JMenuBar;

import com.ontimize.module.IModuleActionMenuListener;

/**
 * The listener interface for receiving menu events. The class that is interested in processing a menu event implements this interface, and the object created with that class is
 * established as application menu listener using the application's setMenuListener method.
 */
public interface MenuListener {

	/**
	 * Indicates to the element which is the menu bar to listen for
	 *
	 * @param menubar
	 */
	public void addMenuToListenFor(JMenuBar menubar);

	/**
	 * Sets a reference to the application
	 *
	 * @param application
	 */
	public void setApplication(Application application);

	public void setInitialState();

	/**
	 * Adds the listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addListener(IModuleActionMenuListener listener);
}