package com.ontimize.gui.table;

import java.util.Hashtable;

import com.ontimize.gui.field.CurrencyDataField;
import com.ontimize.gui.field.TextDataField;

public class CurrencyCellEditor extends CellEditor {

	public CurrencyCellEditor(Hashtable parameters) {
		super(parameters.get(CellEditor.COLUMN_PARAMETER), CurrencyCellEditor.initializeDataField(parameters));
	}

	protected static CurrencyDataField initializeDataField(Hashtable parameters) {
		CurrencyDataField cdf = new CurrencyDataField(parameters);
		if (cdf.getDataField() instanceof TextDataField.EJTextField) {
			((TextDataField.EJTextField) cdf.getDataField()).setCaretPositionOnFocusLost(false);
		}
		return cdf;
	}

}