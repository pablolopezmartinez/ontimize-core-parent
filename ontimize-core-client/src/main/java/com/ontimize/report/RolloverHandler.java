package com.ontimize.report;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;

public class RolloverHandler extends MouseAdapter {

    private static RolloverHandler handler = new RolloverHandler();

    private RolloverHandler() {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        AbstractButton b = null;
        if (e.getSource() instanceof AbstractButton) {
            b = (AbstractButton) e.getSource();
        } else {
            return;
        }
        if (b.isEnabled()) {
            b.setBorderPainted(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        AbstractButton b = null;
        if (e.getSource() instanceof AbstractButton) {
            b = (AbstractButton) e.getSource();
        } else {
            return;
        }
        b.setBorderPainted(false);
    }

    public static RolloverHandler getInstance() {
        return RolloverHandler.handler;
    }

    public void add(AbstractButton b) {
        b.addMouseListener(this);
        b.setBorderPainted(false);
    }

}
