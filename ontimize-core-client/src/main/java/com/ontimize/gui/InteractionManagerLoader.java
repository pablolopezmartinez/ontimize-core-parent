package com.ontimize.gui;

public interface InteractionManagerLoader {

    /**
     * Gets the {@link InteractionManager} to use with the specified form
     * @param formName Form name
     * @return
     */
    public InteractionManager getInteractionManager(String formName);

}
