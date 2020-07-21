package com.ontimize.gui.table;

/**
 * In a chain of data manipulators some behavior is common. TableMap provides most of this behavior
 * and can be subclassed by filters that only need to override a handful of specific methods.
 * TableMap implements TableModel by routing all requests to its model, and TableModelListener by
 * routing all events to its listeners. Inserting a TableMap which has not been subclassed into a
 * chain of table filters should have no effect.
 */

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TableMap extends AbstractTableModel implements TableModelListener {

    protected TableModel model;

    public TableModel getModel() {
        return this.model;
    }

    public void setModel(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }

    // By default, implement TableModel by forwarding all messages
    // to the model.

    @Override
    public Object getValueAt(int aRow, int aColumn) {
        return this.model.getValueAt(aRow, aColumn);
    }

    @Override
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        this.model.setValueAt(aValue, aRow, aColumn);
    }

    @Override
    public int getRowCount() {
        return this.model == null ? 0 : this.model.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return this.model == null ? 0 : this.model.getColumnCount();
    }

    @Override
    public String getColumnName(int aColumn) {
        return this.model.getColumnName(aColumn);
    }

    @Override
    public Class getColumnClass(int aColumn) {
        return this.model.getColumnClass(aColumn);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return this.model.isCellEditable(row, column);
    }

    //
    // Implementation of the TableModelListener interface,
    //
    // By default forward all events to all the listeners.
    @Override
    public void tableChanged(TableModelEvent e) {
        this.fireTableChanged(e);
    }

}
