package com.ontimize.gui;

import java.util.EventListener;

/**
 * Interface that determines the methods that controls the navigation among records of forms.
 * Notifies when a record is going to check to perform the necessary operations if the change is not
 * permitted and notifies when a change was performed.
 */
public interface DataNavigationListener extends EventListener {

    /**
     * Called when a form is going to change the status. In order to allow the change, this method must
     * return true.
     * @param event
     * @return true if the change can be done; false otherwise
     */
    public boolean dataWillChange(DataNavigationEvent event);

    /**
     * Notifies that the data in the form has changed.
     * @param event
     */
    public void dataChanged(DataNavigationEvent event);

}
