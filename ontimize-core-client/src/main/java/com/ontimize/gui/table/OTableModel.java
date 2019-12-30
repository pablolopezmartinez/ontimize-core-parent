package com.ontimize.gui.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public interface OTableModel extends TableModel {

	public boolean isSum();
	
	public Number getColumnOperation(int columnIndex);
	
	public TableCellRenderer getSumCellRenderer(boolean currency);
	
	public int convertRowIndexToFilteredModel(int viewRowIndex);
	
	public Number getSelectedColumnOperation(Object columnName, int[] rowIndex);
}
