package com.ontimize.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JWindow;

public class JTipWindow extends JWindow {

    public JTipWindow(Window parent) {
        super(parent);
    }

    public JTipWindow() {
        super();
    }

    public void show(Component c, int x, int y) {
        Point p = c.getLocationOnScreen();
        this.setLocation(p.x + x, p.y + y);
        if (!this.isVisible()) {
            this.setVisible(true);
        }
    }

}
