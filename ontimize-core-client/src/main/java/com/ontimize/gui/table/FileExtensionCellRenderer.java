package com.ontimize.gui.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

public class FileExtensionCellRenderer extends IconCellRenderer {

    private static final String URI_PROP_DEFECTO = "com/ontimize/gui/resources/fileextensionsicons.properties";

    private static final String UNKNOWN_EXTENSION_ICON = "com/ontimize/gui/resources/winprogramicons/unknown.gif";

    ;

    /**
     * Creates a cell renderer used by file names with the default properties file
     * @param showText
     * @throws Exception
     */
    public FileExtensionCellRenderer(boolean showText) throws Exception {
        super(FileExtensionCellRenderer.URI_PROP_DEFECTO, showText);
        this.setDefaultIcon(FileExtensionCellRenderer.UNKNOWN_EXTENSION_ICON);
    }

    public FileExtensionCellRenderer(String uriProp, boolean showText, boolean translate) throws Exception {
        super(uriProp, showText, translate);
        this.setDefaultIcon(FileExtensionCellRenderer.UNKNOWN_EXTENSION_ICON);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
            int row, int column) {
        Object oValue = value;
        if (value != null) {
            String vS = value.toString();
            int pointLastIndex = vS.lastIndexOf(".");
            oValue = vS.substring(pointLastIndex + 1);
        }
        Component comp = super.getTableCellRendererComponent(table, oValue, selected, hasFocus, row, column);
        if (this.showText && (value != null)) {
            ((JLabel) comp).setText(value.toString());
        }
        return comp;
    }

}
