package com.ontimize.util.swing.border;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.EtchedBorder;

public class SideEtchedBorder extends EtchedBorder {

    public static final int TOP = 0;

    public static final int LEFT = 1;

    public static final int RIGHT = 2;

    public static final int BOTTOM = 3;

    protected int location = SideEtchedBorder.TOP;

    public SideEtchedBorder(int type, int location) {
        super(type);
        if ((location < 0) || (location > 3)) {
            this.location = 0;
        } else {
            this.location = location;
        }
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int w = width;
        int h = height;
        g.translate(x, y);

        switch (this.location) {
            case TOP:
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getShadowColor(c) : this.getHighlightColor(c));
                g.drawLine(0, 0, w - 2, 0);
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getHighlightColor(c) : this.getShadowColor(c));
                g.drawLine(1, 1, w - 3, 1);
                break;
            case LEFT:
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getShadowColor(c) : this.getHighlightColor(c));
                g.drawLine(0, 0, 0, h - 2);
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getHighlightColor(c) : this.getShadowColor(c));
                g.drawLine(1, h - 3, 1, 1);
                break;
            case RIGHT:
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getShadowColor(c) : this.getHighlightColor(c));
                g.drawLine(w - 2, 0, w - 2, h - 2);
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getHighlightColor(c) : this.getShadowColor(c));
                g.drawLine(w - 1, h - 1, w - 1, 0);
                break;
            case BOTTOM:
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getShadowColor(c) : this.getHighlightColor(c));
                g.drawLine(0, h - 2, w - 2, h - 2);
                g.setColor(this.etchType == EtchedBorder.LOWERED ? this.getHighlightColor(c) : this.getShadowColor(c));
                g.drawLine(0, h - 1, w - 1, h - 1);
                break;
        }

        g.translate(-x, -y);
    }

}
