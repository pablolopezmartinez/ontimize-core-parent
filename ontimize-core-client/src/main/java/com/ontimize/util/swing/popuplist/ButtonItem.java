package com.ontimize.util.swing.popuplist;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class ButtonItem extends JButton {

    public ButtonItem(String text) {
        this.setText(text);
        this.setBorder(new LineBorder(Color.RED));
    }

}
