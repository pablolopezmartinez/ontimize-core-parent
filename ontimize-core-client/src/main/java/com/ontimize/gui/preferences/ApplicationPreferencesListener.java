package com.ontimize.gui.preferences;

/**
 * The listener interface for receiving preference events. The class that is interested in
 * processing a preference event implements this interface, and the object created with that class
 * is registered with a component, using the component's addApplicationPreferencesListener method.
 * When the preference change event occurs, that object's preferenceChanged method is invoked.
 *
 * @version 1.0
 */
public interface ApplicationPreferencesListener extends java.util.EventListener {

    /**
     * Invoked when a preference change event occurs. The event object contains the associated
     * information
     * @param e
     */
    public void preferenceChanged(PreferenceEvent e);

}
