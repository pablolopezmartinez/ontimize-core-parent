package com.ontimize.gui.table;

public interface InsertTableInsertRowChange {

    public void addInsertTableInsertRowListener(InsertTableInsertRowListener l);

    public void removeInsertTableInsertRowListener(InsertTableInsertRowListener l);

    public void fireInsertTableInsertRowChange(InsertTableInsertRowEvent insertTableInsertRowEvent);

}
