package com.ontimize.gui.table;

import java.awt.Component;
import java.awt.TextComponent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.i18n.Internationalization;

/**
 * Renderer used to show the translation of a text in a table.
 *
 */
public class BundleCellRenderer extends CellRenderer implements Internationalization {

    protected ResourceBundle bundle;

    public BundleCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
        String translation = null;
        if (value != null) {
            translation = ApplicationManager.getTranslation(value.toString(), this.bundle);
        }

        if (c instanceof TextComponent) {
            ((TextComponent) c).setText(translation);
        } else if (c instanceof JLabel) {
            ((JLabel) c).setText(translation);
        }
        return c;
    }

    @Override
    public void setResourceBundle(ResourceBundle res) {
        this.bundle = res;
    }

    @Override
    public void setComponentLocale(Locale l) {
    }

    @Override
    public Vector getTextsToTranslate() {
        return new Vector(0);
    }

}
