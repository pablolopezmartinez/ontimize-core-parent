package com.ontimize.gui.field.html.actions;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JEditorPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.html.utils.ActionPerformedListener;
import com.ontimize.gui.field.html.utils.I18n;
import com.ontimize.gui.i18n.Internationalization;

/**
 * @author Imatia S.L.
 *
 */
public abstract class HTMLTextEditAction extends DefaultAction implements Internationalization {

    private static final Logger logger = LoggerFactory.getLogger(HTMLTextEditAction.class);

    public static final String EDITOR = "editor";

    public static final int DISABLED = -1;

    public static final int WYSIWYG = 0;

    static final I18n i18n = I18n.getInstance();

    protected Vector actionListeners = new Vector(2);

    protected ResourceBundle resourceBundle;

    public HTMLTextEditAction(String name) {
        super(name);
        this.updateEnabledState();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void execute(ActionEvent e) throws Exception {
        this.firePreviousActionPerformed(e);
        this.editPerformed(e, this.getCurrentEditor());
        this.firePostActionPerformed(e);
    }

    protected JEditorPane getCurrentEditor() {
        try {
            JEditorPane ep = (JEditorPane) this.getContextValue(HTMLTextEditAction.EDITOR);
            return ep;
        } catch (ClassCastException cce) {
            HTMLTextEditAction.logger.error(null, cce);
        }
        return null;
    }

    @Override
    protected void actionPerformedCatch(Throwable t) {
        HTMLTextEditAction.logger.debug(null, t);
    }

    @Override
    protected void contextChanged() {
        this.updateContextState(this.getCurrentEditor());
    }

    protected void updateContextState(JEditorPane editor) {

    }

    protected abstract void editPerformed(ActionEvent e, JEditorPane editor);

    public void addActionPerformedListener(ActionPerformedListener listener) {
        this.actionListeners.add(listener);
    }

    public void firePreviousActionPerformed(ActionEvent e) {
        for (int i = 0; i < this.actionListeners.size(); i++) {
            ((ActionPerformedListener) this.actionListeners.get(i)).previousActionPerformed(e);
        }
    }

    public void firePostActionPerformed(ActionEvent e) {
        for (int i = 0; i < this.actionListeners.size(); i++) {
            ((ActionPerformedListener) this.actionListeners.get(i)).postActionPerformed(e);
        }
    }

    public void removeActionPerformedListener(ActionPerformedListener listener) {
        this.actionListeners.remove(listener);
    }

    @Override
    public void setComponentLocale(Locale l) {

    }

    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public Vector getTextsToTranslate() {
        return null;
    }

}
