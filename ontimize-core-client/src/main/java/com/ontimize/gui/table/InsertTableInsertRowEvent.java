package com.ontimize.gui.table;

import java.util.EventObject;
import java.util.Hashtable;

public class InsertTableInsertRowEvent extends EventObject {

    Hashtable rowData = null;

    public InsertTableInsertRowEvent(InsertTableInsertRowChange source, Hashtable rowData) {
        super(source);

        this.rowData = rowData;
    }

    public Hashtable getRowData() {
        return this.rowData;
    }

}
