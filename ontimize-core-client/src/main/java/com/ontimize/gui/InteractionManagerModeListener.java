package com.ontimize.gui;

/**
 * Interface to listen changes in state of forms (i.e. form changes from QUERY-INSERT to INSERT).
 * This one should be implemented by components that want to perform an operation when state of form
 * changes.
 *
 * @author Imatia Innovation SL
 * @since 5.2000
 */
public interface InteractionManagerModeListener {

    /**
     * This method is called automatically when form changes the state in components that implement this
     * interface. Current state can be obtained from event parameter:
     * <code>e.getInteractionManagerMode()</code>. Value that returns this method can be compared with
     * variables: <br>
     * <ul>
     * <li>InteractionManager.QUERYINSERT
     * <li>InteractionManager.QUERY
     * <li>InteractionManager.UPDATE
     * <li>InteractionManager.INSERT
     * </ul>
     * @param e The event
     */
    public void interactionManagerModeChanged(InteractionManagerModeEvent e);

}
