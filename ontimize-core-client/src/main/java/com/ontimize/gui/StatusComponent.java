package com.ontimize.gui;

/**
 * Interface that must be implemented by those components that can be monitored in the Application's
 * status bar.
 */
public interface StatusComponent {

    /**
     * The text to be placed in the status bar relative to this component. Typically this method will be
     * call when the mouse is over the element.
     * @return the text to be shown in the status bar relative to the element.
     */
    public String getStatusText();

}
