package com.ontimize.gui;

import java.util.EventObject;

public class ValueEvent extends EventObject {

    public static int USER_CHANGE = 0;

    public static int PROGRAMMATIC_CHANGE = 1;

    protected Object oldValue = null;

    protected Object newValue = null;

    protected int type = 1;

    /**
     * Create a new event to use when a component value changes
     * @param source Event source component
     * @param newValue New component value
     * @param oldValue Old component value
     * @param type Event type. Valid values are:<br>
     *        {@link ValueEvent#PROGRAMMATIC_CHANGE} when the value change was automatically or <br>
     *        {@link ValueEvent#USER_CHANGE} when an user did the change
     */
    public ValueEvent(Object source, Object newValue, Object oldValue, int type) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public Object getNewValue() {
        return this.newValue;
    }

    public int getType() {
        return this.type;
    }

}
