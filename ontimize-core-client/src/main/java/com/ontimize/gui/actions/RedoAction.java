package com.ontimize.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.UndoManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedoAction extends AbstractAction {

	private static final Logger	logger	= LoggerFactory.getLogger(RedoAction.class);

	protected UndoManager undoManager;

	public RedoAction(UndoManager manager) {
		this.undoManager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.undoManager.redo();
		} catch (Exception cre) {
			RedoAction.logger.trace(null, cre);
		}
	}
}