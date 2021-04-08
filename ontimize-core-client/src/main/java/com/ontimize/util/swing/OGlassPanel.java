package com.ontimize.util.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import com.ontimize.gui.images.ImageManager;

public class OGlassPanel extends JPanel {

    public static Color backgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.30f);

    public static boolean disable = false;

    public OGlassPanel() {
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // paintContent(g);
        this.paintBlackLayer(g);
    }

    private BufferedImage contentBuffer = null;

    private Graphics contentGraphics = null;

    protected void paintBlackLayer(Graphics g) {
        Rectangle clip = g.getClipBounds();
        g.setColor(OGlassPanel.backgroundColor);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

    private void paintContent(Graphics g) {
        Container contentPane = ((RootPaneContainer) SwingUtilities.getAncestorOfClass(RootPaneContainer.class, this))
            .getContentPane();
        if ((this.contentBuffer == null) || (this.contentBuffer.getWidth() != contentPane.getWidth())
                || (this.contentBuffer.getHeight() != contentPane.getHeight())) {
            if (this.contentBuffer != null) {
                this.contentBuffer.flush();
                this.contentGraphics.dispose();
            }
            this.contentBuffer = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            this.contentGraphics = this.contentBuffer.createGraphics();
        }

        Graphics2D g2 = (Graphics2D) this.contentGraphics;
        g2.clipRect(contentPane.getX(), contentPane.getY(), contentPane.getWidth(), contentPane.getHeight());
        // because the content buffer is reused, the image
        // must be cleared
        g2.setComposite(AlphaComposite.Clear);
        Rectangle clip = g.getClipBounds();
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(g.getColor());
        g2.setFont(g.getFont());
        contentPane.paint(g2);
        this.contentBuffer = ImageManager.getBlurImage(this.contentBuffer, 2);
        g.drawImage(this.contentBuffer, 0, 0, null);
    }

    public static BufferedImage darker(BufferedImage buffImage) {
        // we use an image with an alpha channel
        // therefore, we need 4 components (RGBA)
        float[] factors = new float[] { 0.75f, 0.75f, 0.75f, 1f };
        float[] offsets = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        RescaleOp op = new RescaleOp(factors, offsets, null);
        BufferedImage brighter = op.filter(buffImage, null);
        return brighter;
    }

}
