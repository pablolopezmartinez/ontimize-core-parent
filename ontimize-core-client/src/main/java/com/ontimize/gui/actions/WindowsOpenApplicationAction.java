package com.ontimize.gui.actions;

import java.awt.event.ActionEvent;

public class WindowsOpenApplicationAction extends AbstractButtonAction {

    protected String appName = null;

    private final boolean error = false;

    public WindowsOpenApplicationAction(String appName) {
        this.appName = appName;
    }

    public boolean wasError() {
        return this.error;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
         * try { WindowsUtils.runApplication_Script(appName); error = false; } catch(Exception ex) { error =
         * true; }
         */
    }

}
