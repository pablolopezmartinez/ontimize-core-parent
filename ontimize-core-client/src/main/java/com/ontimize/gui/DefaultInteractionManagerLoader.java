package com.ontimize.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInteractionManagerLoader implements InteractionManagerLoader {

	private static final Logger	logger						= LoggerFactory.getLogger(DefaultInteractionManagerLoader.class);

	/**
	 * Default interaction manager class name
	 */
	public static String DEFAULT_INTERACTION_MANAGER = "com.ontimize.gui.BasicInteractionManager";

	protected static Class defaultInteraccionManager = null;

	@Override
	public InteractionManager getInteractionManager(String formName) {
		try {
			if (DefaultInteractionManagerLoader.defaultInteraccionManager == null) {
				DefaultInteractionManagerLoader.defaultInteraccionManager = Class.forName(DefaultInteractionManagerLoader.DEFAULT_INTERACTION_MANAGER);
			}
			Object o = DefaultInteractionManagerLoader.defaultInteraccionManager.newInstance();
			if (o instanceof InteractionManager) {
				return (InteractionManager) o;
			}
		} catch (Exception e) {
			DefaultInteractionManagerLoader.logger.error(null, e);
		}
		return null;
	}

}
