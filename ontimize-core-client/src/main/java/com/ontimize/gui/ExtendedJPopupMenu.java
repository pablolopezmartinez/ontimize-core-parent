package com.ontimize.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedJPopupMenu extends JPopupMenu {

    private static final Logger logger = LoggerFactory.getLogger(ExtendedJPopupMenu.class);

    public ExtendedJPopupMenu() {
        super();
    }

    public ExtendedJPopupMenu(String label) {
        super(label);
    }

    @Override
    public void show(Component c, int x, int y) {
        Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
        // Avoid that the menu disappears of the window
        try {

            Point p = c.getLocationOnScreen();
            if ((p.x + x + this.getWidth()) > dScreen.width) {
                x = Math.max(x - this.getWidth(), -p.x);
            }
            if ((p.y + y + this.getHeight()) > dScreen.height) {
                y = Math.max(y - this.getHeight(), -p.y);
            }
        } catch (Exception e) {
            ExtendedJPopupMenu.logger.error(this.getClass().toString() + ": " + e.getMessage(), e);
        }
        super.show(c, x, y);
    }

}
