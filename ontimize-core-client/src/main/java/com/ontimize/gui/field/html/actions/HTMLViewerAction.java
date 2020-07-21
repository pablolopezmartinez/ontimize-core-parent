package com.ontimize.gui.field.html.actions;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

/**
 * This action shows a panel with the xml that represent the current value of the diagram component.
 * The default diagram class {@link DiagramGUI} registers the keys CTRL+ALT+SHIFT+X as shortcut to
 * this action.
 *
 * @see HTMLViewerAction
 */
public class HTMLViewerAction extends AbstractAction {

    private static final Logger logger = LoggerFactory.getLogger(HTMLViewerAction.class);

    protected JEditorPane editor;

    protected HtmlViewerPanel htmlViewerPanel;

    public HTMLViewerAction(JEditorPane editor) {
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.editor != null) {
            String value = null;
            value = this.getValueToShow();
            if (this.htmlViewerPanel == null) {
                this.createViewerPanel();
                ApplicationManager.center(this.htmlViewerPanel);
            }
            this.htmlViewerPanel.setValue(value);
            this.htmlViewerPanel.setVisible(true);
        }
    }

    protected String getValueToShow() {
        String value = null;
        if (this.editor != null) {
            value = this.editor.getText();
        }
        return value;
    }

    protected void createViewerPanel() {
        Window w = null;
        if (this.editor == null) {
            w = ApplicationManager.getApplication().getFrame();
        } else {
            w = SwingUtilities.getWindowAncestor(this.editor);
        }
        if (w == null) {
            return;
        }

        if (w instanceof Frame) {
            this.htmlViewerPanel = new HtmlViewerPanel((Frame) w);
        } else if (w instanceof Dialog) {
            this.htmlViewerPanel = new HtmlViewerPanel((Dialog) w);
        }
        if (ApplicationManager.getApplication() != null) {
            this.htmlViewerPanel.setTitle(ApplicationManager.getApplication().getFrame().getTitle());
        }
    }

    public static class HtmlViewerPanel extends JDialog {

        protected JTextArea textArea;

        public HtmlViewerPanel(Frame frame) {
            super(frame);
            this.init();
        }

        public HtmlViewerPanel(Dialog dialog) {
            super(dialog);
            this.init();

        }

        protected void init() {
            this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            try {
                this.textArea = new JTextArea();
                this.add(new JScrollPane(this.textArea));
                this.textArea.setEditable(false);
            } catch (Exception e) {
                HTMLViewerAction.logger.error(null, e);
            }

            this.setSize(800, 600);
            this.registerKeyBindings();

        }

        public void setValue(String xml) {
            this.textArea.setText(xml);
            this.textArea.setCaretPosition(0);
        }

        protected class HideAction extends AbstractAction {

            JDialog dialog;

            public HideAction(JDialog d) {
                this.dialog = d;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (this.dialog != null) {
                    this.dialog.setVisible(false);
                }
            }

        }

        protected void registerKeyBindings() {
            KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            Action action = new HideAction(this);

            try {
                InputMap inMap = this.textArea.getInputMap(JComponent.WHEN_FOCUSED);
                ActionMap actMap = this.textArea.getActionMap();
                inMap.put(ks, action);
                actMap.put("HIDE_XML", action);
            } catch (Exception e) {
                HTMLViewerAction.logger.error(this.getClass().toString() + ": Error registering keybindings", e);
            }
        }

    }

}
