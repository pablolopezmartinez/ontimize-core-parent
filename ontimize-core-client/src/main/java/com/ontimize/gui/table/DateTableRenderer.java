package com.ontimize.gui.table;

import java.util.Hashtable;

import javax.swing.table.DefaultTableCellRenderer;

import com.ontimize.gui.field.DateDataField;

/**
 * @deprecated (Never used)
 */
@Deprecated
public class DateTableRenderer extends DefaultTableCellRenderer {

    DateDataField dateDataField = new DateDataField(new Hashtable());

    public DateTableRenderer() {
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof java.sql.Timestamp) {
            this.dateDataField.setValue(value);
            String sFieldValue = this.dateDataField.getText();
            super.setValue(sFieldValue);
        } else {
            super.setValue(value);
        }
    }

}
