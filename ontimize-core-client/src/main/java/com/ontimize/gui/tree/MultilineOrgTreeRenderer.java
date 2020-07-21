package com.ontimize.gui.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

public class MultilineOrgTreeRenderer extends JTextArea implements JOrgTreeCellRenderer {

    protected Border lineBorder = null;

    protected Color backgroundColor = null;

    protected Color selectedBackgroundColor = null;

    public MultilineOrgTreeRenderer() {
        this.lineBorder = BorderFactory.createLineBorder(Color.black);
        this.backgroundColor = Color.orange;
        this.selectedBackgroundColor = Color.yellow;
        ((JComponent) this).setOpaque(true);
        ((JTextArea) this).setLineWrap(true);
        ((JTextArea) this).setWrapStyleWord(true);
    }

    @Override
    public Component getJOrgTreeCellRendererComponent(JOrgTree orgTree, Object value, boolean selec, int row,
            boolean focus) {
        ((JTextArea) this).setFont(((Component) orgTree).getFont());
        ((JComponent) this).setOpaque(true);
        ((JComponent) this).setBorder(this.lineBorder);
        if (selec) {
            ((JComponent) this).setBackground(this.selectedBackgroundColor);
        } else {
            ((JComponent) this).setBackground(this.backgroundColor);
        }
        if (value != null) {
            ((JTextComponent) this).setText(value.toString());
        } else {
            ((JTextComponent) this).setText("");
        }
        return this;
    }

}
