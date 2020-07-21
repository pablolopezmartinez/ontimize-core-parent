package com.ontimize.gui;

import javax.swing.JToolBar;

import com.ontimize.module.IModuleActionToolBarListener;

public interface ToolBarListener {

    /**
     * Set the JToolBar component to listen for
     */
    public void addToolBarToListenFor(JToolBar toolBar);

    /**
     * Set a reference to the client application component
     */
    public void setApplication(Application aplic);

    public void setInitialState();

    /**
     * Adds the listener.
     * @param listener the listener
     */
    public void addListener(IModuleActionToolBarListener listener);

}
