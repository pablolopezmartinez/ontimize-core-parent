package com.ontimize.gui.field.html.actions;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author Imatia S.L.
 *
 */
public abstract class BasicEditAction extends HTMLTextEditAction {

    /**
     * @param name
     */
    public BasicEditAction(String name) {
        super(name);
    }

    public void ensureEditor(ActionEvent e, JEditorPane editor) {
        if (editor == null) {
            JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
            editor = (JEditorPane) popupMenu.getInvoker();
        }
    }

    @Override
    protected final void editPerformed(ActionEvent e, JEditorPane editor) {
        this.ensureEditor(e, editor);
        this.doEdit(e, editor);
    }

    protected abstract void doEdit(ActionEvent e, JEditorPane editor);

    @Override
    protected void updateContextState(JEditorPane editor) {
    }

}
