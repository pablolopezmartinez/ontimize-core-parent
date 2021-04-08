package com.ontimize.gui.table;

import java.util.Hashtable;
import java.util.Vector;

import com.ontimize.gui.field.TextComboDataField;

public class ComboCellEditor extends CellEditor {

    public ComboCellEditor(Hashtable parameters) {
        super(parameters.get(CellEditor.COLUMN_PARAMETER), new TextComboDataField(parameters));
    }

    public void setValues(Vector values) {
        ((TextComboDataField) this.field).setValues(values);
    }

}
