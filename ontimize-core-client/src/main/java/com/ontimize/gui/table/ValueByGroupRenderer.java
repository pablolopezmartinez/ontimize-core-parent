package com.ontimize.gui.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.ontimize.gui.table.TableSorter.ValueByGroup;

/**
 * Renders the representation of a cell that is an instance
 */
public class ValueByGroupRenderer implements TableCellRenderer {

    protected EJTable t = null;

    public ValueByGroupRenderer(EJTable t) {
        this.t = t;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        TableCellRenderer render = this.t.getNoGroupedCellRenderer(row, column);
        Component c = null;
        if (value instanceof ValueByGroup) {
            c = render.getTableCellRendererComponent(table, ((ValueByGroup) value).getValue(), isSelected, hasFocus,
                    row, column);
            if (c instanceof JLabel) {
                Object oPrevious = ((JLabel) c).getText();
                StringBuilder buffer = null;
                if (oPrevious != null) {
                    buffer = new StringBuilder(oPrevious.toString());
                } else {
                    buffer = new StringBuilder();
                }
                buffer.append(" (");
                buffer.append(((ValueByGroup) value).getElementCount());
                buffer.append(")");
                ((JLabel) c).setText(buffer.toString());
            }
        } else {
            c = render.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        return c;
    }

}
