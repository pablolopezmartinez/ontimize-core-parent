package com.ontimize.util.swing.icon;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class LinkingIcon implements Icon {

    private Icon first = null;

    private Icon second = null;

    public LinkingIcon(Icon first, Icon second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getIconHeight() {
        return Math.max(this.first.getIconHeight(), this.second.getIconHeight());
    }

    @Override
    public int getIconWidth() {
        int primero = this.first.getIconWidth();
        int segundo = this.second.getIconWidth();
        return primero + segundo;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.first.paintIcon(c, g, x, y);
        this.second.paintIcon(c, g, x + this.first.getIconWidth(), y);
    }

}
