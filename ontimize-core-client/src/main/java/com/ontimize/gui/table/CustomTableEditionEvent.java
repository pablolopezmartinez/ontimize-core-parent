package com.ontimize.gui.table;

/* NADA : NO SE USA. DEPRECATED DEL TODO */
public class CustomTableEditionEvent {

    protected Table table = null;

    protected Object newValue = null;

    protected int row = -1;

    protected int column = -1;

    public CustomTableEditionEvent(Table table, Object newValue, int row, int column) {
        this.table = table;
        this.newValue = newValue;
        this.row = row;
        this.column = column;
    }

    public Table getTable() {
        return this.table;
    }

    public Object getNewValue() {
        return this.newValue;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

}
