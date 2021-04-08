package com.ontimize.util.swing;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AJDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(AJDialog.class);

    protected static Action[] actions = new Action[0];

    protected static KeyStroke[] keyStrokes = new KeyStroke[0];

    protected static String[] keys = new String[0];

    static {
        class EAction extends AbstractAction {

            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (w instanceof JDialog) {
                    ((JDialog) w).setVisible(false);
                }
            }

        }
        AJDialog.setActionForKey(KeyEvent.VK_ESCAPE, 0, new EAction(), "Close window");
    }

    public AJDialog() throws HeadlessException {
        super();
        this.registerKeyBindings();
    }

    public AJDialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        this.registerKeyBindings();
    }

    public AJDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
        this.registerKeyBindings();
    }

    public AJDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        this.registerKeyBindings();
    }

    public AJDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
        this.registerKeyBindings();
    }

    public AJDialog(Dialog owner) throws HeadlessException {
        super(owner);
        this.registerKeyBindings();
    }

    public AJDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        this.registerKeyBindings();
    }

    public AJDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        this.registerKeyBindings();
    }

    public AJDialog(Frame owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        this.registerKeyBindings();
    }

    public AJDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
        this.registerKeyBindings();
    }

    public AJDialog(Frame owner) throws HeadlessException {
        super(owner);
        this.registerKeyBindings();
    }

    protected void registerKeyBindings() {
        try {
            InputMap inMap = ((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actMap = ((JComponent) this.getContentPane()).getActionMap();
            for (int i = 0; i < AJDialog.actions.length; i++) {
                inMap.put(AJDialog.keyStrokes[i], AJDialog.keys[i]);
                actMap.put(AJDialog.keys[i], AJDialog.actions[i]);
            }
        } catch (Exception e) {
            AJDialog.logger.error(this.getClass().toString() + ": Error registering keybindings", e);
        }
    }

    public static void setActionForKey(int keyCode, int modifiers, Action action, String key) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, modifiers, false);
        Action[] a = new Action[AJDialog.actions.length + 1];
        for (int i = 0; i < AJDialog.actions.length; i++) {
            a[i] = AJDialog.actions[i];
        }
        a[a.length - 1] = action;
        KeyStroke[] k = new KeyStroke[AJDialog.keyStrokes.length + 1];
        for (int i = 0; i < AJDialog.keyStrokes.length; i++) {
            k[i] = AJDialog.keyStrokes[i];
        }
        k[k.length - 1] = ks;

        String[] ke = new String[AJDialog.keys.length + 1];
        for (int i = 0; i < AJDialog.keys.length; i++) {
            ke[i] = AJDialog.keys[i];
        }
        ke[ke.length - 1] = key;

        AJDialog.keys = ke;
        AJDialog.actions = a;
        AJDialog.keyStrokes = k;
    }

    public void setAction(int keyCode, int modifiers, Action action, String key) {
        KeyStroke ks = KeyStroke.getKeyStroke(keyCode, modifiers, true);
        try {
            InputMap inMap = ((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actMap = ((JComponent) this.getContentPane()).getActionMap();
            inMap.put(ks, key);
            actMap.put(key, action);
        } catch (Exception e) {
            AJDialog.logger.error(this.getClass().toString() + ": Error registering keybindings", e);
        }
    }

}
