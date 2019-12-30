package com.ontimize.gui;

public interface SecureElement {

	public static String DESACTIVATE_COMPONENT_BY_PERMISSION_TIP = "secureelement.component_disabled_persmission_restrictions";

	public void initPermissions();

	public boolean isRestricted();
}