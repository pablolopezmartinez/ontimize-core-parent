package com.ontimize.gui;

/**
 * The listener interface for receiving application events. The class that is interested in
 * processing an application event either implements this interface (and all the methods it
 * contains). The listener object created from that class is then registered with the application
 * using the MainApplication's ddApplicationListener method. When the Application's status changes
 * by virtue of being closed the relevant method in the listener object is invoked, and the
 * ApplicationEvent is passed to it.
 *
 * @version 1.0
 */
public interface ApplicationListener extends java.util.EventListener {

    /**
     * Invoked when the user attempts to close the window
     * @param e
     * @return
     */
    public boolean applicationClosing(ApplicationEvent e);

}
