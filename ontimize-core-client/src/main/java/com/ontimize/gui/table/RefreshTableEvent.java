package com.ontimize.gui.table;

import java.util.EventObject;

public class RefreshTableEvent extends EventObject {

    public static final int OK = 0;

    public static final int ERROR = 1;

    public static final int CANCEL = 2;

    protected int type = 0;

    public RefreshTableEvent(IRefreshable source, int type) {
        super(source);
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
