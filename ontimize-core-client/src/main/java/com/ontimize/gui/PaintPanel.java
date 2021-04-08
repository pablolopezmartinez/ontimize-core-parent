package com.ontimize.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaintPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PaintPanel.class);

    protected Paint bgPaint;

    public PaintPanel(LayoutManager layout) {
        super(layout);
    }

    public PaintPanel(LayoutManager layout, Paint bgPaint) {
        super(layout);
        this.bgPaint = bgPaint;
        if (this.bgPaint != null) {
            this.setOpaque(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (this.bgPaint != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(this.bgPaint);
                Insets insets = this.getInsets();
                g2d.fillRect(insets.left, insets.top, this.getWidth() - insets.right - insets.left,
                        this.getHeight() - insets.bottom - insets.top);
            }
        } catch (Exception e) {
            PaintPanel.logger.error(null, e);
        }
        super.paintComponent(g);
    }

    public Paint getBgPaint() {
        return this.bgPaint;
    }

    public void setBgPaint(Paint bgPaint) {
        this.bgPaint = bgPaint;
        if (this.bgPaint != null) {
            this.setOpaque(false);
        }
    }

}
