package com.ontimize.gui.table;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class ExtendedTableModelEvent extends TableModelEvent {

    public static final int HIDDEN_ROW = -2;

    public ExtendedTableModelEvent(TableModel source) {
        super(source);
    }

    public ExtendedTableModelEvent(TableModel source, int firstRow, int lastRow, int column, int type) {
        super(source, firstRow, lastRow, column, type);
    }

    public ExtendedTableModelEvent(TableModel source, int firstRow, int lastRow, int column) {
        super(source, firstRow, lastRow, column);
    }

    public ExtendedTableModelEvent(TableModel source, int firstRow, int lastRow) {
        super(source, firstRow, lastRow);
    }

    public ExtendedTableModelEvent(TableModel source, int row) {
        super(source, row);
    }

}
