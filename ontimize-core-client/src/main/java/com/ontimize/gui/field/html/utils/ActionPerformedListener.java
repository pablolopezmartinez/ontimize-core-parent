package com.ontimize.gui.field.html.utils;

import java.awt.event.ActionEvent;
import java.util.EventListener;

public interface ActionPerformedListener extends EventListener {

    /**
     * Method called before action is performed
     * @param e Event
     */
    public void previousActionPerformed(ActionEvent e);

    /**
     * Method called after action is performed
     * @param e Event
     */
    public void postActionPerformed(ActionEvent e);

}
