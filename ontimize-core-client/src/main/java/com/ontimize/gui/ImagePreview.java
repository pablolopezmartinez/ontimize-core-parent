package com.ontimize.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreview extends JComponent implements PropertyChangeListener {

    ImageIcon thumbnail = null;

    File file = null;

    public ImagePreview(JFileChooser fc) {
        this.setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
    }

    public void loadImage() {
        if (this.file == null) {
            return;
        }

        ImageIcon tmpIcon = new ImageIcon(this.file.getPath());
        if (tmpIcon.getIconWidth() > 90) {
            this.thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, java.awt.Image.SCALE_DEFAULT));
        } else {
            this.thumbnail = tmpIcon;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            this.file = (File) e.getNewValue();
            if (this.isShowing()) {
                this.loadImage();
                this.repaint();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.thumbnail == null) {
            this.loadImage();
        }
        if (this.thumbnail != null) {
            int x = (this.getWidth() / 2) - (this.thumbnail.getIconWidth() / 2);
            int y = (this.getHeight() / 2) - (this.thumbnail.getIconHeight() / 2);

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            this.thumbnail.paintIcon(this, g, x, y);
        }
    }

}
