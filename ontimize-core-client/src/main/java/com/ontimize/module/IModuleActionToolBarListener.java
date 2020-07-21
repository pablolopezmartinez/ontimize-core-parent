package com.ontimize.module;

import java.awt.event.ActionEvent;

import com.ontimize.gui.Application;

public interface IModuleActionToolBarListener {

    /**
     * Action performed.
     * @param e the e
     * @return true, if action is accomplished
     */
    boolean actionModulePerformed(ActionEvent e);

    public void setApplication(Application application);

}
