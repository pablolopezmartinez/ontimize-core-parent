package com.ontimize.gui.table;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.swing.CollapsiblePopupPanel;

public class GroupTableButton extends TableButton {

    protected CollapsiblePopupPanel panel = new CollapsiblePopupPanel();

    public GroupTableButton() {
        super(ImageManager.getIcon(ImageManager.GROUP));
        this.setTransferHandler(new ButtonTransferHandler());
    }

    @Override
    protected void init() {
        this.setMargin(new Insets(0, 0, 0, 0));
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GroupTableButton.this.panel.show((JButton) e.getSource(), 0, ((JButton) e.getSource()).getHeight());
            }
        });
    }

    protected void hideCollapsible() {
        this.panel.setVisible(false);
    }

    @Override
    public Component add(Component comp) {
        return this.panel.add(comp);
    }

    public Component add(Component comp, boolean fireProperty) {
        Component c = this.add(comp);
        this.firePropertyChange(ControlPanel.CHANGE_BUTTON_PROPERTY, false, true);
        return c;
    }

    public Component[] getInnerComponents() {
        return this.panel.getInnerComponents();
    }

}
