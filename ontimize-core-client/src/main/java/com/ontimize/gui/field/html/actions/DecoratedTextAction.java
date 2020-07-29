package com.ontimize.gui.field.html.actions;

import javax.swing.Action;
import javax.swing.text.TextAction;

/**
 * @author Imatia S.L.
 *
 */
public abstract class DecoratedTextAction extends TextAction {

    Action delegate;

    public DecoratedTextAction(String name, Action delegate) {
        super(name);
        this.delegate = delegate;
    }

    public Action getDelegate() {
        return this.delegate;
    }

}
