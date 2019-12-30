package com.ontimize.gui.table;

import java.awt.Component;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import com.ontimize.gui.field.IntegerDataField;

public class IntegerTableCellEditor implements TableCellEditor {

	protected IntegerDataField integerDataField = new IntegerDataField(new Hashtable());

	protected DefaultCellEditor defaultCellEditor = null;

	public IntegerTableCellEditor() {
		this.defaultCellEditor = new DefaultCellEditor((JTextField) this.integerDataField.getDataField());
	}

	@Override
	public Object getCellEditorValue() {
		return this.integerDataField.getValue();
	}

	@Override
	public void removeCellEditorListener(CellEditorListener listener) {
		this.defaultCellEditor.removeCellEditorListener(listener);
	}

	@Override
	public void addCellEditorListener(CellEditorListener listener) {
		this.defaultCellEditor.addCellEditorListener(listener);
	}

	@Override
	public void cancelCellEditing() {
		this.defaultCellEditor.cancelCellEditing();
	}

	@Override
	public boolean stopCellEditing() {
		return this.defaultCellEditor.stopCellEditing();
	}

	@Override
	public boolean shouldSelectCell(EventObject event) {
		return this.defaultCellEditor.shouldSelectCell(event);
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		return this.defaultCellEditor.isCellEditable(event);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
		return this.integerDataField.getDataField();

	}
}