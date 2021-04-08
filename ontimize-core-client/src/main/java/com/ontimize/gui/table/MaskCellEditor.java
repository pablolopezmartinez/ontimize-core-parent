package com.ontimize.gui.table;

import java.util.Hashtable;

import com.ontimize.gui.field.MaskDataField;

public class MaskCellEditor extends CellEditor {

    public MaskCellEditor(Hashtable parameters) {
        super(parameters.get(CellEditor.COLUMN_PARAMETER), new MaskDataField(parameters));
    }

}
