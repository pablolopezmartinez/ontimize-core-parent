package com.ontimize.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.Form;
import com.ontimize.gui.button.Button;

public abstract class AbstractButtonAction implements ActionListener {

    public AbstractButtonAction() {
    }

    public Form getForm(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof Component) {
            Form f = ApplicationManager.getFormAncestor((Component) source);
            if (f != null) {
                return f;
            }
        }
        if (source instanceof Button) {
            return ((Button) source).getParentForm();
        } else {
            return null;
        }
    }

}
