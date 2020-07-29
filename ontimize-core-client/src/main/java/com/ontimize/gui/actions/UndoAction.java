package com.ontimize.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.UndoManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UndoAction extends AbstractAction {

    private static final Logger logger = LoggerFactory.getLogger(UndoAction.class);

    protected UndoManager undoManager;

    public UndoAction(UndoManager manager) {
        this.undoManager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            this.undoManager.undo();
        } catch (Exception cre) {
            UndoAction.logger.trace(null, cre);
        }
    }

}
