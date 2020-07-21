package com.ontimize.gui;

import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;

public class MenuRadioButtonGroup extends Menu {

    protected ButtonGroup buttonGroup = new ButtonGroup();

    public MenuRadioButtonGroup(Hashtable parameters) {
        super(parameters);
    }

    @Override
    public JMenuItem add(JMenuItem itemMenu) {
        if (itemMenu instanceof RadioMenuItem) {
            this.buttonGroup.add(itemMenu);
        }
        return super.add(itemMenu);
    }

}
