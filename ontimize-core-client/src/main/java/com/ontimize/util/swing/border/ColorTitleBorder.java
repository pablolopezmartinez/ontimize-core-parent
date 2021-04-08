package com.ontimize.util.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ColorTitleBorder extends TitledBorder {

    protected EmptyBorder separator = new EmptyBorder(2, 2, 2, 2) {

        @Override
        public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
            Color parentColor = c.getParent().getBackground();
            g.setColor(parentColor);
            // save original clip
            Rectangle saveClip = g.getClipBounds();

            // Top
            Rectangle r = new Rectangle(saveClip);
            if (ColorTitleBorder.computeIntersection(r, 0, 0, width, 2)) {
                g.setClip(r);
                g.fillRect(x, y, width, height);
            }

            // Left
            r = new Rectangle(saveClip);
            if (ColorTitleBorder.computeIntersection(r, 0, 0, 2, height)) {
                g.setClip(r);
                g.fillRect(x, y, width, height);
            }

            // Right
            r = new Rectangle(saveClip);
            if (ColorTitleBorder.computeIntersection(r, (x + width) - 2, 0, 2, height)) {
                g.setClip(r);
                g.fillRect(x, y, width, height);
            }

            // Bottom
            r = new Rectangle(saveClip);
            if (ColorTitleBorder.computeIntersection(r, 0, (y + height) - 2, width, 2)) {
                g.setClip(r);
                g.fillRect(x, y, width, height);
            }

            g.setColor(c.getBackground());
            g.setClip(saveClip);
        };
    };

    public ColorTitleBorder(String title) {
        super(new EmptyBorder(new Insets(2, 2, 2, 2)), title);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        this.separator.paintBorder(c, g, x, y, width, height);
        super.paintBorder(c, g, x + 2, y + 2, width - 4, height - 4);
    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    @Override
    public Insets getBorderInsets(Component c) {
        Insets insets = super.getBorderInsets(c);
        insets.top = insets.top + 2;
        insets.right = insets.right + 2;
        insets.bottom = insets.bottom + 2;
        insets.left = insets.left + 2;
        return insets;
    }

    protected static boolean computeIntersection(Rectangle dest, int rx, int ry, int rw, int rh) {
        int x1 = Math.max(rx, dest.x);
        int x2 = Math.min(rx + rw, dest.x + dest.width);
        int y1 = Math.max(ry, dest.y);
        int y2 = Math.min(ry + rh, dest.y + dest.height);
        dest.x = x1;
        dest.y = y1;
        dest.width = x2 - x1;
        dest.height = y2 - y1;

        if ((dest.width <= 0) || (dest.height <= 0)) {
            return false;
        }
        return true;
    }

}
