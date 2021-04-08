package com.ontimize.util.swing.selectablelist;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectableItemMouseListener extends MouseAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SelectableItemMouseListener.class);

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getModifiers() == InputEvent.META_MASK) {
            return;
        }

        if (e.getX() > 30) {
            return;
        }

        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int index = ((JList) e.getComponent()).locationToIndex(e.getPoint());
            if (index < 0) {
                return;
            }
            SelectableItem it = (SelectableItem) ((JList) e.getComponent()).getModel().getElementAt(index);
            boolean wasSelected = !it.isSelected();
            it.setSelected(wasSelected);

            ((JList) e.getComponent()).repaint();
            if (((JList) e.getComponent()).getModel() instanceof DefaultListModel) {
                ((DefaultListModel) ((JList) e.getComponent()).getModel()).setElementAt(it, index);
            }
            // ((DefaultListModel) ((JList)
            // e.getComponent()).getModel()).setElementAt(it, index);

        } catch (Exception ex) {
            SelectableItemMouseListener.logger.error(null, ex);
        } finally {
            ((JList) e.getComponent()).setCursor(Cursor.getDefaultCursor());

        }
    }

}
