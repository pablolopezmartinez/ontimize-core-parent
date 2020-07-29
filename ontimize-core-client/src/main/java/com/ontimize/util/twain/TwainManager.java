package com.ontimize.util.twain;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.images.ImageManager;

public abstract class TwainManager {

    private static final Logger logger = LoggerFactory.getLogger(TwainManager.class);

    private static boolean DEBUG = true;

    private static JComponent compAux = new JComponent() {
    };

    public static int getTwainSourceCount() throws Exception {
        if (!TwainUtilities.isTwainEnabled()) {
            throw new Exception("Twain disabled -> there are not classes");
        }
        try {
            return SK.gnome.twain.TwainManager.listSources().length;
        } catch (Exception e) {
            TwainManager.logger.error(null, e);
            return -1;
        }
    }

    public static Object selectTwainSource() throws Exception {
        if (!TwainUtilities.isTwainEnabled()) {
            throw new Exception("Twain disabled -> there are not classes");
        }
        try {
            return SK.gnome.twain.TwainManager.selectSource(null);
        } catch (Exception e) {
            TwainManager.logger.error(null, e);
            return null;
        }
    }

    public static final int DEFAULT_PREVIEW_RES = 50;

    public static Image preview() throws Exception {
        return TwainManager.acquire(TwainManager.DEFAULT_PREVIEW_RES, TwainManager.DEFAULT_PREVIEW_RES, null);
    }

    public static Image preview(boolean showSourceGUI) throws Exception {
        return TwainManager.acquire(TwainManager.DEFAULT_PREVIEW_RES, TwainManager.DEFAULT_PREVIEW_RES, null,
                showSourceGUI);
    }

    public static Image showPreviewDialog(Component c) throws Exception {
        Image im = TwainManager.preview();
        PreviewDialog v = TwainManager.createPreviewDialog(c);
        v.setImage(im);
        v.pack();
        TwainManager.center(v);
        v.setVisible(true);
        return (Image) v.display.getSource();
    }

    public static Image acquire() throws Exception {
        return TwainManager.acquire(-1, -1, null);
    }

    public static Image acquire(int resX, int resY, Rectangle2D.Double rExt) throws Exception {
        return TwainManager.acquire(resX, resY, rExt, false);
    }

    /**
     * @param resX
     * @param resY
     * @param rExt in millimeters
     * @return
     * @throws Exception
     */
    public static Image acquire(int resX, int resY, Rectangle2D.Double rExt, boolean showSourceGUI) throws Exception {
        if (!TwainUtilities.isTwainEnabled()) {
            throw new Exception("Twain disabled -> there are not classes");
        }
        long t = System.currentTimeMillis();
        try {

            SK.gnome.twain.TwainSource source = null;
            source = SK.gnome.twain.TwainManager.getDefaultSource();

            source.maskUnsupportedCapabilityException(false);
            source.maskBadValueException(false);
            source.setVisible(showSourceGUI);
            source.setBehaviorMask(0);
            source.setUnits(SK.gnome.twain.TwainConstants.TWUN_INCHES);

            if ((resX > 0) && (resY > 0)) {
                TwainManager.logger.debug("Current resolution established: " + resX + " , " + resY);
                source.setXResolution(resX);
                source.setYResolution(resY);
            }

            Rectangle2D.Double rTotal = TwainUtilities.convertMMtoInches(rExt);
            if (rTotal != null) {
                // explore zone in inches
                TwainManager.logger.debug("Explore zone in inches: " + rTotal);
                // left, top , right, bottom
                source.setFrame(rTotal.x, rTotal.y, rTotal.x + rTotal.width, rTotal.y + rTotal.height);
            } else {
                source.setFrame(0, 0, source.getPhysicalWidth(), source.getPhysicalHeight());
            }

            MediaTracker tracker = new MediaTracker(TwainManager.compAux);

            if (TwainManager.DEBUG) {
                TwainManager.logger.debug("Starting TWAIN capture: " + (System.currentTimeMillis() - t));
            }

            Image imagen = Toolkit.getDefaultToolkit().createImage(source);
            tracker.addImage(imagen, 0);
            tracker.waitForAll();
            if ((imagen.getWidth(null) <= 0) || (imagen.getHeight(null) <= 0)) {
                // Scanning error
                throw new Exception("M_SCANNING_ERROR");
            }

            if (TwainManager.DEBUG) {
                TwainManager.logger.debug("TWAIN capture finished: " + (System.currentTimeMillis() - t));
            }

            TwainManager.logger.debug("TwainUtilities -> Memory use = "
                    + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

            return imagen;
        } catch (SK.gnome.twain.TwainException e) {
            TwainManager.logger.error(null, e);
            throw e;
        } catch (Exception e) {
            TwainManager.logger.error(null, e);
            throw e;
        } catch (OutOfMemoryError error) {
            TwainManager.logger.error(null, error);
            throw error;
        } finally {
            try {
                SK.gnome.twain.TwainManager.close();
            } catch (Exception e) {
                TwainManager.logger.error(null, e);
            }
        }
    }

    protected static class JImageDisplay extends JPanel {

        protected RenderedImage source = null;

        protected int adjustSize = -1;

        protected double scale = 1.0;

        protected double zoom = 1;

        protected Rectangle selectionRectangle = null;

        protected Stroke selStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
                new float[] { 5f, 5.0f }, 0.0f);

        public JImageDisplay() {
            this.setLayout(null);
            this.setOpaque(false);
        }

        @Override
        public Dimension getMinimumSize() {
            return this.getPreferredSize();
        }

        public void setZoom(double zoom) {
            this.zoom = zoom;
            this.revalidate();
            this.repaint();
        }

        public double getZoom() {
            return this.zoom;
        }

        public void setSelectionRectangle(Rectangle r) {
            if (r == null) {
                this.selectionRectangle = r;
                this.repaint();
                return;
            }

            Rectangle old = this.selectionRectangle;
            this.selectionRectangle = r;
            int xMin = r.x - 1;
            int yMin = r.y - 1;
            int xMax = (r.x - 1) + r.width + 2;
            int yMax = (r.y - 1) + r.height + 2;

            if (old != null) {
                xMin = Math.min(xMin, old.x - 1);
                yMin = Math.min(yMin, old.y - 1);
                xMax = Math.max(xMax, (old.x - 1) + old.width + 2);
                yMax = Math.max(yMax, (old.y - 1) + old.height + 2);
            }
            this.repaint(new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin));
        }

        public Rectangle getSelectionRectangle() {
            return this.selectionRectangle;
        }

        @Override
        public Dimension getMaximumSize() {
            if (this.adjustSize > 0) {
                return this.getPreferredSize();
            } else {
                return super.getMaximumSize();
            }
        }

        public int getAdjustToSize() {
            return this.adjustSize;
        }

        public void setAdjustToSize(int s) {
            this.adjustSize = s;
            this.revalidate();
            this.repaint();
        }

        public JImageDisplay(RenderedImage renderedimage) {
            this.setLayout(null);
            if (renderedimage == null) {
                throw new IllegalArgumentException("image can´t be null");
            }
            this.source = renderedimage;
            Rectangle rect = this.source.getData().getBounds();

            int width = rect.width;
            int height = rect.height;
            Insets insets = this.getInsets();
            Dimension dimension = new Dimension(width + insets.left + insets.right,
                    height + insets.top + insets.bottom);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = this.calculatePreferredSize();
            return d;
        }

        protected Dimension calculatePreferredSize() {
            if (this.source == null) {
                if (this.adjustSize > 0) {
                    return new Dimension(this.adjustSize, this.adjustSize);
                }
                Insets insets = this.getInsets();
                return new Dimension(200 + insets.left + insets.right, 200 + insets.top + insets.bottom);
            }
            try {
                if (this.adjustSize > 0) {
                    Rectangle rect = this.source.getData().getBounds();

                    int width = rect.width;
                    int height = rect.height;

                    int maximum = Math.max(width, height);
                    double scale = 1.0;
                    Insets insets = this.getInsets();
                    if (maximum == width) {
                        double currentZoomValue = 1.0;
                        if (this.zoom > 0) {
                            currentZoomValue = this.zoom;
                        }
                        scale = this.adjustSize / (double) width;
                        Dimension dimension = new Dimension(
                                (int) (this.adjustSize * currentZoomValue) + insets.left + insets.right,
                                (int) (height * scale * currentZoomValue) + insets.top + insets.bottom);
                        return dimension;
                    } else {
                        double actualZoom = 1.0;
                        if (this.zoom > 0) {
                            actualZoom = this.zoom;
                        }
                        scale = this.adjustSize / (double) height;
                        Dimension dimension = new Dimension(
                                (int) (width * scale * actualZoom) + insets.left + insets.right,
                                (int) (this.adjustSize * actualZoom) + insets.top + insets.bottom);
                        return dimension;
                    }
                } else if (this.zoom > 0) {
                    Rectangle rect = this.source.getData().getBounds();
                    int width = rect.width;
                    int height = rect.height;
                    Insets insets = this.getInsets();
                    Dimension dimension = new Dimension((int) (width * this.zoom) + insets.left + insets.right,
                            (int) (height * this.zoom) + insets.top + insets.bottom);
                    return dimension;
                } else {
                    Rectangle rect = this.source.getData().getBounds();
                    int width = rect.width;
                    int height = rect.height;
                    Insets insets = this.getInsets();
                    Dimension dimension = new Dimension(width + insets.left + insets.right,
                            height + insets.top + insets.bottom);
                    return dimension;
                }
            } catch (Exception e) {
                TwainManager.logger.trace(null, e);
                if (this.adjustSize > 0) {
                    return new Dimension(this.adjustSize, this.adjustSize);
                }
                Insets insets = this.getInsets();
                return new Dimension(200 + insets.left + insets.right, 200 + insets.top + insets.bottom);
            }
        }

        public void set(RenderedImage renderedimage) {
            this.disposeImage();
            if (renderedimage != null) {
                this.source = renderedimage;
            }
            this.revalidate();
            this.repaint();
        }

        public void set(RenderedImage renderedimage, Rectangle r) {
            this.disposeImage();
            if (renderedimage != null) {
                this.source = renderedimage;
            }
            this.revalidate();
            this.repaint(r);
        }

        protected void removeSource() {
            this.source = null;
            this.revalidate();
            this.repaint();
        }

        public RenderedImage getSource() {
            return this.source;
        }

        public double getCurrentScale() {
            return this.scale;
        }

        @Override
        protected synchronized void paintComponent(Graphics graphics) {
            try {
                Graphics2D graphics2d = (Graphics2D) graphics;
                if (this.source == null) {
                    if (this.isOpaque()) {
                        graphics2d.setColor(this.getBackground());
                        graphics2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                } else {
                    Rectangle rectangle = graphics2d.getClipBounds();
                    if ((rectangle.width == 0) || (rectangle.height == 0)) {
                        return;
                    }
                    if (this.isOpaque()) {
                        graphics2d.setColor(this.getBackground());
                        graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    }
                    Insets insets = this.getInsets();
                    int offsetX = insets.left;
                    int offsetY = insets.top;
                    Rectangle rect = this.source.getData().getBounds();

                    int width = rect.width;
                    int height = rect.height;
                    Dimension prefSize = this.getPreferredSize();
                    double scaleX = (prefSize.width - (insets.left + insets.right)) / (double) width;
                    double scaleY = (prefSize.height - (insets.top + insets.bottom)) / (double) height;
                    // Get the minimum scale
                    this.scale = Math.min(scaleX, scaleY);

                    try {
                        AffineTransform t = new AffineTransform(AffineTransform.getTranslateInstance(offsetX, offsetY));
                        t.concatenate(AffineTransform.getTranslateInstance(-rect.x * this.scale, -rect.y * this.scale));
                        t.concatenate(AffineTransform.getScaleInstance(this.scale, this.scale));
                        graphics2d.drawRenderedImage(this.source, t);
                    } catch (Exception e) {
                        TwainManager.logger.error(null, e);
                    } catch (OutOfMemoryError err) {
                        TwainManager.logger.error("OutOfMemoryError: ", err);
                    }
                    if (this.selectionRectangle != null) {
                        graphics2d.setColor(Color.red);
                        Stroke s = graphics2d.getStroke();
                        if (this.selStroke != null) {
                            graphics2d.setStroke(this.selStroke);
                        }
                        graphics2d.drawRect(this.selectionRectangle.x, this.selectionRectangle.y,
                                this.selectionRectangle.width, this.selectionRectangle.height);
                        graphics2d.setStroke(s);
                    }
                }
            } catch (Exception e) {
                TwainManager.logger.trace(null, e);
            }
        }

        public void disposeImage() {
            if (this.source != null) {
                if (this.source instanceof Image) {
                    ((Image) this.source).flush();
                    this.source = null;
                    this.revalidate();
                    this.repaint();
                } else {
                    this.removeSource();
                }
            }
        }

        public void setSelectionStroke(Stroke s) {
            this.selStroke = s;
        }

    }

    protected static class PreviewDialog extends JDialog {

        protected JImageDisplay display = null;

        protected Rectangle scanArea = null;

        protected JButton scan = new JButton("Scan selected area");

        protected JLabel labelInfo = new JLabel("Select scanning area");

        protected JLabel labelResolucion = new JLabel("Resolution");

        protected JTextField tfResolution = new JTextField();

        protected JButton capture = new JButton("Capture");

        protected class SelectionListener extends MouseAdapter implements MouseMotionListener {

            private int x = -1;

            private int y = -1;

            private JScrollPane scroll = null;

            private JImageDisplay disp = null;

            public SelectionListener(JImageDisplay c) {
                this.disp = c;
                Container cont = c.getParent();
                if (cont.getParent() instanceof JScrollPane) {
                    this.scroll = (JScrollPane) cont.getParent();
                    c.addMouseListener(this);
                    c.addMouseMotionListener(this);
                } else if (cont.getParent().getParent() instanceof JScrollPane) {
                    this.scroll = (JScrollPane) cont.getParent().getParent();
                    c.addMouseListener(this);
                    c.addMouseMotionListener(this);
                } else {
                    TwainManager.logger.debug("Scroll not found");
                    c.addMouseListener(this);
                    c.addMouseMotionListener(this);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.x = e.getX();
                this.y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if ((this.x != -1) && (this.y != -1)) {
                    // crop.
                    Rectangle r = this.disp.getSelectionRectangle();
                    // Scale.
                    double dScale = this.disp.getCurrentScale();
                    dScale = 1 / dScale;
                }
                this.x = -1;
                this.y = -1;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Original point - current point
                if ((this.x != -1) && (this.y != -1)) {

                    int dx = e.getX() - this.x;
                    int dy = e.getY() - this.y;
                    if ((dx > 0) && (dy > 0)) {
                        this.disp.setSelectionRectangle(new Rectangle(this.x, this.y, dx, dy));
                        if (this.scroll != null) {
                            this.disp.scrollRectToVisible(new Rectangle(e.getX(), e.getY(), 2, 2));
                        }
                    } else {
                        int xini = Math.min(this.x, e.getX());
                        int yini = Math.min(this.y, e.getY());
                        int width = Math.abs(dx);
                        int height = Math.abs(dy);
                        this.disp.setSelectionRectangle(new Rectangle(xini, yini, width, height));
                        if (this.scroll != null) {
                            this.disp.scrollRectToVisible(new Rectangle(e.getX(), e.getY(), 2, 2));
                        }
                    }
                }
            }

        }

        public PreviewDialog(Frame f, String title) {
            super(f, title, true);
            this.init();
        }

        public PreviewDialog(Dialog f, String title) {
            super(f, title, true);
            this.init();
        }

        protected void init() {
            JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelNorte.add(this.labelInfo);
            panelNorte.add(this.labelResolucion);
            panelNorte.add(this.tfResolution);
            panelNorte.add(this.capture);

            this.labelInfo.setFont(this.labelInfo.getFont().deriveFont(16));
            this.labelInfo.setFont(this.labelInfo.getFont().deriveFont(Font.BOLD));
            this.labelInfo.setForeground(Color.blue);
            this.tfResolution.setText("150");
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonsPanel.add(this.scan);
            this.getContentPane().add(panelNorte, BorderLayout.NORTH);
            this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
            this.display = new JImageDisplay();
            this.display.setAdjustToSize(400);
            this.display.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

            this.capture.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Image im = TwainManager.preview();
                        PreviewDialog.this.setImage(im);
                    } catch (Exception ex) {
                        TwainManager.logger.error(null, ex);
                    }
                }
            });

            this.capture.setMargin(new Insets(2, 4, 2, 4));
            ImageIcon icon = ImageManager.getIcon(ImageManager.SCANNER_PREVIEW);
            if (icon != null) {
                this.capture.setIcon(icon);
            } else {
                this.capture.setText("Capture");
            }

            this.scan.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Rectangle rSelection = PreviewDialog.this.display.getSelectionRectangle();
                        double dScale = PreviewDialog.this.display.getCurrentScale();
                        dScale = 1 / dScale;
                        rSelection = new Rectangle((int) (rSelection.x * dScale), (int) (rSelection.y * dScale),
                                (int) (rSelection.width * dScale),
                                (int) (rSelection.height * dScale));
                        Rectangle2D.Double rExp = null;
                        if (rSelection != null) {
                            rExp = TwainUtilities.convertPixelsToMM(TwainManager.DEFAULT_PREVIEW_RES,
                                    TwainManager.DEFAULT_PREVIEW_RES,
                                    new Rectangle.Double(rSelection.x, rSelection.y, rSelection.width,
                                            rSelection.height));

                        }
                        int res = 150;
                        try {
                            res = Integer.parseInt(PreviewDialog.this.tfResolution.getText());
                        } catch (Exception ex) {
                            TwainManager.logger.trace(null, ex);
                            JOptionPane.showMessageDialog(PreviewDialog.this.scan,
                                    PreviewDialog.this.tfResolution + " It is not a valid resolution", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Image im = TwainManager.acquire(res, res, rExp);
                        PreviewDialog.this.display.set(TwainUtilities.toBufferedImage(im));
                        PreviewDialog.this.display.setSelectionRectangle(null);
                        PreviewDialog.this.setVisible(false);
                    } catch (Exception ex) {
                        TwainManager.logger.error(null, ex);
                    }
                }
            });
            this.getContentPane().add(new JScrollPane(this.display));
            SelectionListener listener = new SelectionListener(this.display);
            TwainManager.logger.trace("Selection listener register : {}", listener);
        }

        public void setImage(Image im) {
            if (im == null) {
                this.display.set(null);
            } else {
                this.display.set(TwainUtilities.toBufferedImage(im));
            }
        }

    };

    protected static PreviewDialog previewDialog = null;

    protected static PreviewDialog createPreviewDialog(Component c) {
        Window w = SwingUtilities.getWindowAncestor(c);
        if (TwainManager.previewDialog == null) {
            if (w instanceof Dialog) {
                TwainManager.previewDialog = new PreviewDialog((Dialog) w, "Preview");
            } else {
                TwainManager.previewDialog = new PreviewDialog((Frame) w, "Preview");
            }
        } else {
            if (TwainManager.previewDialog.getOwner() != w) {
                TwainManager.previewDialog.dispose();
            }
            if (w instanceof Dialog) {
                TwainManager.previewDialog = new PreviewDialog((Dialog) w, "Preview");
            } else {
                TwainManager.previewDialog = new PreviewDialog((Frame) w, "Preview");
            }
        }
        return TwainManager.previewDialog;
    }

    public static void center(Window f) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width / 2) - (f.getWidth() / 2);
        int y = (d.height / 2) - (f.getHeight() / 2);
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > d.width) {
            x = 0;
        }
        if (y > d.height) {
            y = 0;
        }
        f.setLocation(x, y);
    }

}
