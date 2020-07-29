package com.ontimize.gui;

public class ParameterException extends Exception {

    protected Object[] params = null;

    public ParameterException(String message, Object[] params) {
        super(message);
        this.params = params;
    }

    public Object[] getParams() {
        return this.params;
    }

}
