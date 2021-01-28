package com.ontimize.util.incidences;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.ontimize.gui.Application;
import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.MainApplication;
import com.ontimize.gui.button.FormHeaderButton;
import com.ontimize.gui.images.ImageManager;

public class IncidenceHelper {

    public static void addIncidenceServiceButton(Application application) {
        MainApplication app = (MainApplication) application;

        InputMap inMap = ((JComponent) app.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actMap = ((JComponent) app.getContentPane()).getActionMap();
        final FormHeaderButton button = IncidenceHelper.createIncidenceButton(app.getOwner());
        button.setIcon(ImageManager.getIcon(ImageManager.INCIDENCE_BUTTON));
        button.setToolTipText(ApplicationManager.getTranslation("M_CREATE_INCIDENCE_BUTTON"));

        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, true);
        AbstractAction act = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        };

        app.setKeyBinding("createIncidence", ks, act, inMap, actMap, true);
        if (app.getStatusBar() != null) {
        	app.getStatusBar().add(button);
        }
    }

    public static FormHeaderButton createIncidenceButton(Window owner) {
        final Window ownerWindow = owner;
        Hashtable h = new Hashtable();
        h.put("key", new String("createIncidence"));
        // h.put("icon", imagemanager.incidence_button);

        FormHeaderButton fhb = new FormHeaderButton(h);

        fhb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FormCreateIncidences incidences = new FormCreateIncidences(e.getSource());
                ApplicationManager.center(incidences);
                incidences.setVisible(true);
            }
        });
        return fhb;
    }

}
