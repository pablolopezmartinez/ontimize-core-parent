package com.ontimize.util.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * A label, that can be rotate periodically. Thus the reading direction is either horizontal (the
 * default), up or down.
 *
 */

public class RotatedLabel extends JLabel {

    public static int defaultTimerPeriod = 50; // ms

    public static int defaultRotationIncrement = 20; // degree

    protected Timer timer;

    protected double currentRotation = 1;

    /**
     * Initialises a new instance of the {@link RotatedLabel} class using default values.
     */
    public RotatedLabel() {
        super();
    }

    /**
     * Initialises a new instance of the {@link RotatedLabel} class using the specified icon and
     * horizontal alignment.
     * @param image The icon to use for this label.
     * @param horizontalAlignment The horizontal alignment of the text.
     */
    public RotatedLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        if (image != null) {
            this.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        }
        this.init();
    }

    /**
     * Initialises a new instance of the {@link RotatedLabel} class using the specified icon.
     * @param image The icon to use for this label.
     */
    public RotatedLabel(Icon image) {
        super(image);
        if (image != null) {
            this.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        }
        this.init();
    }

    public void init() {
        this.timer = new Timer(RotatedLabel.defaultTimerPeriod, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RotatedLabel.this.currentRotation = (RotatedLabel.this.currentRotation
                        + RotatedLabel.defaultRotationIncrement) % 360;
                RotatedLabel.this.repaint();
            }
        });
        this.timer.start();
    }

    public Timer getTimer() {
        return this.timer;
    }

    public void restart() {
        if (!this.timer.isRunning()) {
            this.timer.restart();
        }
    }

    public void stop() {
        if (this.timer.isRunning()) {
            this.timer.stop();
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int cx = this.getSize().width / 2;
        int cy = this.getSize().height / 2;
        double radian = (this.currentRotation * Math.PI) / 180;
        double x = this.getPreferredSize().width / 2;
        double y = this.getPreferredSize().height / 2;
        g2.translate(cx, cy);
        g2.rotate(radian);
        g2.translate(-x, -y);
        g2.drawImage(((ImageIcon) this.getIcon()).getImage(), 0, 0, this);
    }

}
