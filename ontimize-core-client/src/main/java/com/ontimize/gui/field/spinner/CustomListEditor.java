package com.ontimize.gui.field.spinner;

import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.JSpinner.ListEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomListEditor extends ListEditor {

    private static final Logger logger = LoggerFactory.getLogger(CustomListEditor.class);

    public CustomListEditor(JSpinner spinner) {
        super(spinner);
    }

    @Override
    public void commitEdit() throws ParseException {
        try {
            super.commitEdit();
        } catch (Exception e) {
            CustomListEditor.logger.trace(null, e);
        }
    }

}
