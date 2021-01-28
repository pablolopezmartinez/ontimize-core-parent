package com.ontimize.gui.table.blocked;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.table.EJTable;
import com.ontimize.gui.table.ExtendedTableModel;
import com.ontimize.gui.table.TableSorter;

public class BlockedTable extends JTable {

    private static final Logger logger = LoggerFactory.getLogger(BlockedTable.class);

    protected EJTable dataTable;

    protected int blockedColumnIndex = 0;

    public BlockedTable(TableModel model, EJTable table) {
        super(model);
        this.dataTable = table;
        // this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    public EJTable getJTable() {
        return this.dataTable;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
    }

    public int getBlockedColumnIndex() {
        return this.blockedColumnIndex;
    }

    public void setBlockedColumnIndex(int blockedColumnIndex) {
        this.blockedColumnIndex = blockedColumnIndex;
        ((BlockedTableModel) this.getModel()).setBlockedColumnIndex(blockedColumnIndex);
        ((BlockedTableModel) this.getModel()).fireTableStructureChanged();
        ((TableSorter) this.dataTable.getModel()).fireTableChanged(new TableModelEvent(this.dataTable.getModel()));

    }

    @Override
    protected TableColumnModel createDefaultColumnModel() {
        TableColumnModel tableColumnModel = new DefaultTableColumnModel() {
            @Override
            public void moveColumn(int columnIndex, int newIndex) {
                if (newIndex == 0)
                    return;
                super.moveColumn(columnIndex, newIndex);
                BlockedTable.this.dataTable.getColumnModel().moveColumn(columnIndex, newIndex);
            }
        };

        TableColumn rowCountTableColumn = new TableColumn();
        rowCountTableColumn.setHeaderValue(ExtendedTableModel.ROW_NUMBERS_COLUMN);
        rowCountTableColumn.setResizable(false);


        tableColumnModel.addColumn(rowCountTableColumn);
        return tableColumnModel;
    }


    @Override
    public void createDefaultColumnsFromModel() {

        BlockedTableModel tableModel = (BlockedTableModel) getModel();

        if (tableModel != null && this.dataTable != null) {
            TableColumnModel originColumnModel = this.dataTable.getColumnModel();
            // Remove any current columns
            TableColumnModel cm = getColumnModel();

            // int index=1;
            while (cm.getColumnCount() > 1) {
                TableColumn returnedColumn = cm.getColumn(1);
                cm.removeColumn(returnedColumn);

                int originIndex = originColumnModel.getColumnIndex(returnedColumn.getIdentifier());
                TableColumn originColumn = originColumnModel.getColumn(originIndex);

                originColumn.setMaxWidth(returnedColumn.getMaxWidth());
                originColumn.setMinWidth(returnedColumn.getMinWidth());
                originColumn.setPreferredWidth(returnedColumn.getPreferredWidth());
                originColumn.setWidth(returnedColumn.getWidth());
                originColumn.setResizable(returnedColumn.getResizable());
            }

            int blockedColumnIndex = this.getBlockedColumnIndex();

            if (blockedColumnIndex > 0) {
                // Create new columns from the data model info
                for (int i = 1; i <= blockedColumnIndex; i++) {
                    TableColumn currentColumn = originColumnModel.getColumn(i);

                    TableColumn newColumn = new TableColumn();
                    newColumn.setHeaderValue(currentColumn.getHeaderValue());
                    newColumn.setCellEditor(currentColumn.getCellEditor());
                    newColumn.setCellRenderer(currentColumn.getCellRenderer());
                    newColumn.setIdentifier(currentColumn.getIdentifier());
                    newColumn.setMaxWidth(currentColumn.getMaxWidth());
                    newColumn.setMinWidth(currentColumn.getMinWidth());
                    newColumn.setPreferredWidth(currentColumn.getPreferredWidth());
                    newColumn.setWidth(currentColumn.getWidth());
                    newColumn.setResizable(currentColumn.getResizable());
                    newColumn.setHeaderRenderer(currentColumn.getHeaderRenderer());
                    newColumn.setModelIndex(currentColumn.getModelIndex());

                    currentColumn.setWidth(0);
                    currentColumn.setPreferredWidth(0);
                    currentColumn.setMaxWidth(0);
                    currentColumn.setMinWidth(0);
                    currentColumn.setResizable(false);

                    addColumn(newColumn);
                }
            }
        }
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return dataTable.getCellRenderer(row, column);
    }

    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        if (dataTable != null) {
            return dataTable.getDefaultRenderer(columnClass);
        }
        return null;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return dataTable.getCellEditor(row, column);
    }

    @Override
    public TableCellEditor getDefaultEditor(Class<?> columnClass) {
        return super.getDefaultEditor(columnClass);
    }

    @Override
    public int getRowHeight() {
        return dataTable.getRowHeight();
    }

    @Override
    public int getRowHeight(int row) {
        return dataTable.getRowHeight(row);
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    @Override
    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        Rectangle r = super.getCellRect(row, column, includeSpacing);
        if (dataTable!=null && dataTable.isFitRowsHeight()){
            Rectangle dataTableRectangle = dataTable.getCellRect(row, column, includeSpacing);
            if (r.y!=dataTableRectangle.y){
                r.y = dataTableRectangle.y;
            }

            if (r.height!=dataTableRectangle.height){
                r.height = dataTableRectangle.height;
            }
        }
        return r;
    }

}
