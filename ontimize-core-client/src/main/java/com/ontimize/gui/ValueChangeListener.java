package com.ontimize.gui;

import java.util.EventListener;

public interface ValueChangeListener extends EventListener {

    /**
     * Method called when the value change
     * @param e Event with the previous value and the new one
     */
    public void valueChanged(ValueEvent e);

}
