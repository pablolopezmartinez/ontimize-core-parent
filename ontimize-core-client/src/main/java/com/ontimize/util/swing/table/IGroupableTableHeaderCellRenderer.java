package com.ontimize.util.swing.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public interface IGroupableTableHeaderCellRenderer extends TableCellRenderer {

    public Component getTableHeaderCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
            int row, int column);

    public Component getTableHeaderBackgroundCellRendererComponent(JTable table, Object value, boolean selected,
            boolean hasFocus, int row, int column);

}
