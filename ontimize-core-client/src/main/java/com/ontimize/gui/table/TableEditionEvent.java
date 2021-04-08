package com.ontimize.gui.table;

import java.util.EventObject;

/**
 * Event object with info about cell has been edited:
 *
 * <ul>
 * <li>row
 * <li>column
 * <li>column name
 * <li>previous cell value
 * <li>new cell value
 * </ul>
 *
 * @author Imatia Innovation SL
 * @see TableEditorListener
 * @since 5.2000
 */
public class TableEditionEvent extends EventObject {

    private Object value = null;

    private int row = -1;

    private int column = -1;

    private String columnId = null;

    private Object oldValue = null;

    public TableEditionEvent(Table source, Object value, int row, int column, String columnId) {
        this(source, value, row, column, columnId, null);
    }

    public TableEditionEvent(Table source, Object value, int row, int column, String columnId, Object oldValue) {
        super(source);
        this.value = value;
        this.row = row;
        this.column = column;
        this.columnId = columnId;
        this.oldValue = oldValue;
    }

    public Object getValue() {
        return this.value;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public String getColumnId() {
        return this.columnId;
    }

    @Override
    public String toString() {
        return "TableEditionEvent: Value: " + this.value + " , row: " + this.row + " , column: " + this.column
                + " , Column name: " + this.columnId + " , Old value: " + this.oldValue;
    }

}
