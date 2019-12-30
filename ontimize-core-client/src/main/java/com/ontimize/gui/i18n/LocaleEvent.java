package com.ontimize.gui.i18n;

import java.util.EventObject;
import java.util.Locale;

public class LocaleEvent extends EventObject {

	protected Locale locale = null;

	protected String resourceBundle = null;

	public LocaleEvent(Object source, Locale l, String resourceBundle) {
		super(source);
		this.locale = l;
		this.resourceBundle = resourceBundle;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public String getResourceBundle() {
		return this.resourceBundle;
	}
}