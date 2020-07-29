package com.ontimize.gui.field.spinner;

import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;

public class CustomDateEditor extends DateEditor {

    public CustomDateEditor(JSpinner spinner, String dateFormatPattern) {
        super(spinner, dateFormatPattern);
    }

    public CustomDateEditor(JSpinner spinner) {
        super(spinner);
    }

}
