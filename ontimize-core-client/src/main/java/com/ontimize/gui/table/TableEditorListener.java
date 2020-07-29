package com.ontimize.gui.table;

/**
 * Interface to manage edition events in table cells.
 *
 * @see Table#addTableEditorListener(TableEditorListener)
 * @author Imatia Innovation SL
 */
public interface TableEditorListener extends java.util.EventListener {

    /**
     * This method is called just before edition is finished.
     * @param e Object with <code>TableEditionEvent</code>
     * @throws EditingVetoException When occurs an <code>Exception</code> editing
     */
    public void editingWillStop(TableEditionEvent e) throws EditingVetoException;

    /**
     * This method is called when edition is finished.
     * @param e Object with <code>TableEditionEvent</code>
     */
    public void editingStopped(TableEditionEvent e);

    /**
     * This method is called when edition is cancelled.
     * @param e Object with <code>TableEditionEvent</code>
     */
    public void editingCanceled(TableEditionEvent e);

}
