package com.ontimize.gui;

import java.util.EventObject;
import java.util.Vector;

public class RowTransferEvent extends EventObject {

    private Vector transferredRowKey = null;

    public RowTransferEvent(Object source, Vector transferredRows) {
        super(source);
        this.transferredRowKey = transferredRows;
    }

    public Vector getTransferredRowsKeys() {
        return this.transferredRowKey;
    }

}
