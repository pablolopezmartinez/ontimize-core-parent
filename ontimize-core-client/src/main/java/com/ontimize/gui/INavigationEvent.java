package com.ontimize.gui;

import java.util.EventListener;

/**
 * Interface to define the events that are fired when user changes the form manager in application. Used for {@link ApToolBarNavigator} component.
 *
 * @author Imatia Innovation SL
 * @since 5.2057EN-1.0
 *
 */
public interface INavigationEvent extends EventListener {

	public void formManagerChanged(NavigationEvent e);

}
