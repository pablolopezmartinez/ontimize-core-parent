package com.ontimize.gui.table;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class SumRowTableModel implements TableModel {

	protected OTableModel tableSorter;
 
	public SumRowTableModel(OTableModel tableSorter) {
		this.tableSorter = tableSorter;
	}

	@Override
	public int getRowCount() {
		if (this.tableSorter.getRowCount() == 0) {
			return 0;
		}
		if (this.tableSorter.isSum()) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getColumnCount() {
		return this.tableSorter.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.tableSorter.getColumnName(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.tableSorter.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == 0) {
			return this.tableSorter.getColumnOperation(columnIndex);
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.tableSorter.addTableModelListener(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.tableSorter.removeTableModelListener(l);
	}

	public TableCellRenderer getSumCellRenderer(boolean currency) {
		return this.tableSorter.getSumCellRenderer(currency);
	}

	public int convertRowIndexToFilteredModel(int viewRowIndex) {
		return this.tableSorter.convertRowIndexToFilteredModel(viewRowIndex);
	}

	public Number getSelectedColumnOperation(Object columnName, int[] rowIndex) {
		return this.tableSorter.getSelectedColumnOperation(columnName, rowIndex);
	}
}
