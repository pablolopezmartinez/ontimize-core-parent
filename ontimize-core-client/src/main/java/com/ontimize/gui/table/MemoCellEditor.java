package com.ontimize.gui.table;

import java.util.Hashtable;

import com.ontimize.gui.field.MemoDataField;

public class MemoCellEditor extends CellEditor {

	public MemoCellEditor(Hashtable parameters) {
		super(parameters.get(CellEditor.COLUMN_PARAMETER), new MemoDataField(parameters));
	}
}