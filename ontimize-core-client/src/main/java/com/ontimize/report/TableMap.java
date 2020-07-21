package com.ontimize.report;

/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved. Redistribution and use in source
 * and binary forms, with or without modification, are permitted provided that the following
 * conditions are met: -Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. -Redistribution in binary form must reproduct
 * the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. Neither the name of Sun
 * Microsystems, Inc. or the names of contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission. This software is provided
 * "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR RELATING TO USE, MODIFICATION OR
 * DISTRIBUTION OF THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL
 * OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE
 * USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGES. You acknowledge that Software is not designed, licensed or intended for use in the
 * design, construction, operation or maintenance of any nuclear facility.
 */

/*
 * @(#)TableMap.java 1.11 03/01/23
 */

/**
 * In a chain of data manipulators some behavior is common. TableMap provides most of this behavior
 * and can be subclassed by filters that only need to override a handful of specific methods.
 * TableMap implements TableModel by routing all requests to its model, and TableModelListener by
 * routing all events to its listeners. Inserting a TableMap which has not been subclassed into a
 * chain of table filters should have no effect.
 *
 * @version 1.11 01/23/03
 * @author Philip Milne
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

    // By default, Implement TableModel by forwarding all messages
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
