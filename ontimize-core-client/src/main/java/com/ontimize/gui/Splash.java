package com.ontimize.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.util.AWTUtilities;

public class Splash extends JWindow implements ISplash {

    private static final Logger logger = LoggerFactory.getLogger(Splash.class);

    protected JLabel centerLabel = new JLabel();

    protected List<ImageIcon> images;

    protected boolean show = true;

    public Splash(Frame owner, ResourceBundle res, ImageIcon image, final int frameNumber, final int refreshTime) {
        AWTUtilities.setWindowOpaque(this, false);
        this.getRootPane().setBackground(new Color(0, 0, 0, 1));
        AWTUtilities.setAlwaysOnTop(this, true);
        this.images = this.getImages(image, frameNumber);
        this.centerLabel.setIcon(this.images.get(0));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.centerLabel);
        Thread t = new Thread("Splash") {

            @Override
            public void run() {
                int index = 0;
                while (Splash.this.show) {
                    try {
                        Thread.sleep(refreshTime);
                        Splash.this.centerLabel.setIcon(Splash.this.images.get(index));
                        index++;
                        index = index % frameNumber;
                    } catch (InterruptedException e) {
                        Splash.logger.error(null, e);
                    }

                }
            };
        };
        this.pack();
        ApplicationManager.center(this);
        t.start();
    }

    protected List<ImageIcon> getImages(ImageIcon panel, int number) {
        List<ImageIcon> images = new ArrayList<ImageIcon>();

        int width = panel.getIconWidth() / number;
        int height = panel.getIconHeight();
        for (int i = 0; i < number; i++) {
            BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = buffer.createGraphics();
            g.drawImage(panel.getImage(), 0, 0, width, height, width * i, 0, width * (i + 1), height, null);
            ImageIcon current = new ImageIcon(buffer);
            images.add(current);
        }
        return images;
    }

    @Override
    public void show(boolean b) {
        this.show();
    }

    @Override
    public void show() {
        if (SwingUtilities.isEventDispatchThread()) {
            super.show();
            this.update();
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    Splash.this.setVisible(true);
                    Splash.this.update();
                }
            });
        }
    }

    @Override
    public void hide() {
        this.show = false;
        super.hide();

    }

    /**
     * Repaint the window
     */
    public void update() {
        Splash.this.getRootPane()
            .paintImmediately(0, 0, Splash.this.getRootPane().getWidth(), Splash.this.getRootPane().getHeight());
    }

    @Override
    public void setRepaintTime(int repaintTime) {

    }

    @Override
    public void updateText(String text) {

    }

}
