package com.ontimize.util.swing.popuplist;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import com.ontimize.gui.ApplicationManager;

public class LabelItem extends JLabel {

    protected boolean paintBorder = false;

    public LabelItem(Icon image) {
        super(image);
        if (!ApplicationManager.useOntimizePlaf) {
            this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        }
    }

    public LabelItem() {
        super();
        if (!ApplicationManager.useOntimizePlaf) {
            this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        }
    }

    public void setBorderPainted(boolean paint) {
        this.paintBorder = paint;
    }

    @Override
    public void paintBorder(Graphics g) {
        if (this.paintBorder) {
            super.paintBorder(g);
        }
    }

}
