package com.ontimize.gui;

import java.util.EventObject;

/**
 * Class to manage the events that are fired when user changes the form manager in application. Used for {@link ApToolBarNavigator} component.
 *
 * @author Imatia Innovation SL
 * @since 5.2057EN-1.0
 *
 */
public class NavigationEvent extends EventObject {

	public static final int FORM_MANAGER_CHANGED = 0;

	protected int type = 0;
	protected String formManager;

	public NavigationEvent(Object source, int type, String formManager) {
		super(source);
		this.type = type;
		this.formManager = formManager;
	}

	public int getType() {
		return this.type;
	}

	public String getFormManager() {
		return this.formManager;
	}
}
