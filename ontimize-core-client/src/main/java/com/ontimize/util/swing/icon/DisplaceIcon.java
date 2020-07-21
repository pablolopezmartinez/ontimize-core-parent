package com.ontimize.util.swing.icon;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class DisplaceIcon implements Icon {

    private Icon first = null;

    private int displace = 0;

    public DisplaceIcon(Icon first) {
        this.first = first;
    }

    public void setDisplace(int displace) {
        this.displace = displace;
    }

    @Override
    public int getIconHeight() {
        return this.first.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        int primero = this.first.getIconWidth();
        return primero + this.displace;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.first.paintIcon(c, g, x + this.displace, y);
    }

}
