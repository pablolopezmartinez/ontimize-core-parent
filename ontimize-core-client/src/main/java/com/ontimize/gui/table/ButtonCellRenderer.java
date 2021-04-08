package com.ontimize.gui.table;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;

public class ButtonCellRenderer extends CellRenderer {

    protected JButton button = new JButton();

    public ButtonCellRenderer(Icon i) {
        super();
        this.button.setIcon(i);
        this.configureRenderer = false;
        this.button.setFocusPainted(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
            int row, int column) {

        this.setJComponent(this.button);
        Component c = super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);

        this.setTipWhenNeeded(table, value, column);
        return c;
    }

    public JButton getButton() {
        return this.button;
    }

}
