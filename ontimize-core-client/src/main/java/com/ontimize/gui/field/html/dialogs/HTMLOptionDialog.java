package com.ontimize.gui.field.html.dialogs;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.Icon;

/**
 * An abstract OptionDialog for HTML editor dialog boxes.
 *
 * Subclasses should implement dialogs for inserting HTML elements such as tables, links, images,
 * etc.
 *
 * @author Imatia S.L.
 *
 */
public abstract class HTMLOptionDialog extends OptionDialog {

    public HTMLOptionDialog(Frame parent, String title, String desc, Icon ico) {
        super(parent, title, desc, ico);
    }

    public HTMLOptionDialog(Dialog parent, String title, String desc, Icon ico) {
        super(parent, title, desc, ico);
    }

    /**
     * Gets the generated HTML from the dialog
     * @return the HTML
     */
    public abstract String getHTML();

}
