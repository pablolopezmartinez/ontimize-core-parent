package com.ontimize.gui;

import java.util.EventObject;

public class InteractionManagerModeEvent extends EventObject {

    protected int mode = InteractionManager.QUERYINSERT;

    public InteractionManagerModeEvent(Object source, int mode) {
        super(source);
        this.mode = mode;
    }

    public int getInteractionManagerMode() {
        return this.mode;
    }

}
