package com.ontimize.gui.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class DefaultOrgTreeRenderer extends JLabel implements JOrgTreeCellRenderer {

    protected Border lineBorder = null;

    protected Color backgroundColor = null;

    protected Color selectedBackgroundColor = null;

    public DefaultOrgTreeRenderer() {
        this.lineBorder = BorderFactory.createLineBorder(Color.black);
        this.backgroundColor = Color.orange;
        this.selectedBackgroundColor = Color.yellow;
        ((JLabel) this).setHorizontalAlignment(0);
    }

    @Override
    public Component getJOrgTreeCellRendererComponent(JOrgTree orgTree, Object value, boolean selec, int row,
            boolean focus) {
        ((JComponent) this).setFont(((Component) orgTree).getFont());
        ((JComponent) this).setOpaque(true);
        ((JComponent) this).setBorder(this.lineBorder);
        if (selec) {
            ((JComponent) this).setBackground(this.selectedBackgroundColor);
        } else {
            ((JComponent) this).setBackground(this.backgroundColor);
        }
        if (value != null) {
            ((JLabel) this).setText(value.toString());
        } else {
            ((JLabel) this).setText("");
        }
        return this;
    }

}
