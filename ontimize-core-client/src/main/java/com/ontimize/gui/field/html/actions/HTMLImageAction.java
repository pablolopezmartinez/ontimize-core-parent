package com.ontimize.gui.field.html.actions;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTML;

import com.ontimize.gui.field.html.HTMLUtils;
import com.ontimize.gui.field.html.dialogs.ImageDialog;
import com.ontimize.gui.images.ImageManager;

/**
 * Action which desplays a dialog to insert an image
 *
 * @author Imatia S.L.
 *
 */
public class HTMLImageAction extends HTMLTextEditAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public HTMLImageAction() {
        super(HTMLTextEditAction.i18n.str("HTMLShef.image_"));
        this.putValue(Action.SMALL_ICON, ImageManager.getIcon(ImageManager.IMAGE));
        this.putValue(Action.SHORT_DESCRIPTION, this.getValue(Action.NAME));
    }

    @Override
    protected void editPerformed(ActionEvent e, JEditorPane editor) {
        ImageDialog d = this.createDialog(editor);
        d.setLocationRelativeTo(d.getParent());
        d.setVisible(true);
        if (d.hasUserCancelled()) {
            return;
        }

        String tagText = d.getHTML();
        if (editor.getCaretPosition() == editor.getDocument().getLength()) {
            tagText += "&nbsp;";
        }

        editor.replaceSelection("");
        HTML.Tag tag = HTML.Tag.IMG;
        if (tagText.startsWith("<a")) {
            tag = HTML.Tag.A;
        }

        HTMLUtils.insertHTML(tagText, tag, editor);
    }

    protected ImageDialog createDialog(JTextComponent ed) {
        Window w = SwingUtilities.getWindowAncestor(ed);
        ImageDialog d = null;
        if ((w != null) && (w instanceof Frame)) {
            d = new ImageDialog((Frame) w);
        } else if ((w != null) && (w instanceof Dialog)) {
            d = new ImageDialog((Dialog) w);
        }

        return d;
    }

}
