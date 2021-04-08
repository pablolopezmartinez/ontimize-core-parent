package com.ontimize.gui.table;

public class EditingVetoException extends Exception {

    protected boolean showMessage = true;

    public EditingVetoException(String descript) {
        this(descript, true);
    }

    public EditingVetoException(String descript, boolean showMessage) {
        super(descript);
        this.showMessage = showMessage;
    }

    public boolean isShowMessage() {
        return this.showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

}
