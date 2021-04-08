package com.ontimize.gui.field.html.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.InlineView;
import javax.swing.text.html.StyleSheet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.html.HTMLUtils.Base64;

public class OImageView extends View {

    private static final Logger logger = LoggerFactory.getLogger(OImageView.class);

    /**
     * If true, when some of the bits are available a repaint is done.
     * <p>
     * This is set to false as swing does not offer a repaint that takes a delay. If this were true, a
     * bunch of immediate repaints would get generated that end up significantly delaying the loading of
     * the image (or anything else going on for that matter).
     */
    protected static boolean sIsInc = false;

    /**
     * Repaint delay when some of the bits are available.
     */
    protected static int sIncRate = 100;

    /**
     * Property name for pending image icon
     */
    protected static final String PENDING_IMAGE = "html.pendingImage";

    /**
     * Property name for missing image icon
     */
    protected static final String MISSING_IMAGE = "html.missingImage";

    /**
     * Document property for image cache.
     */
    protected static final String IMAGE_CACHE_PROPERTY = "imageCache";

    // Height/width to use before we know the real size, these should at least
    // the size of <code>sMissingImageIcon</code> and
    // <code>sPendingImageIcon</code>
    protected static final int DEFAULT_WIDTH = 38;

    protected static final int DEFAULT_HEIGHT = 38;

    /**
     * Default border to use if one is not specified.
     */
    protected static final int DEFAULT_BORDER = 2;

    // Bitmask values
    protected static final int LOADING_FLAG = 1;

    protected static final int LINK_FLAG = 2;

    protected static final int WIDTH_FLAG = 4;

    protected static final int HEIGHT_FLAG = 8;

    protected static final int RELOAD_FLAG = 16;

    protected static final int RELOAD_IMAGE_FLAG = 32;

    protected static final int SYNC_LOAD_FLAG = 64;

    protected AttributeSet attr;

    protected Image image;

    protected int width;

    protected int height;

    /**
     * Bitmask containing some of the above bitmask values. Because the image loading notification can
     * happen on another thread access to this is synchronized (at least for modifying it).
     */
    protected int state;

    protected Container container;

    protected Rectangle fBounds;

    protected Color borderColor;

    // Size of the border, the insets contains this valid. For example, if
    // the HSPACE attribute was 4 and BORDER 2, leftInset would be 6.
    protected short borderSize;

    // Insets, obtained from the painter.
    protected short leftInset;

    protected short rightInset;

    protected short topInset;

    protected short bottomInset;

    /**
     * We don't directly implement ImageObserver, instead we use an instance that calls back to us.
     */
    protected ImageObserver imageObserver;

    /**
     * Used for alt text. Will be non-null if the image couldn't be found, and there is valid alt text.
     */
    protected View altView;

    /** Alignment along the vertical (Y) axis. */
    protected float vAlign;

    /**
     * Creates a new view that represents an IMG element.
     * @param elem the element to create a view for
     */
    public OImageView(Element elem) {
        super(elem);
        this.fBounds = new Rectangle();
        this.imageObserver = new ImageHandler();
        this.state = OImageView.RELOAD_FLAG | OImageView.RELOAD_IMAGE_FLAG;
    }

    /**
     * Returns the text to display if the image can't be loaded. This is obtained from the Elements
     * attribute set with the attribute name <code>HTML.Attribute.ALT</code>.
     */
    public String getAltText() {
        return (String) this.getElement().getAttributes().getAttribute(HTML.Attribute.ALT);
    }

    /**
     * Return a URL for the image source, or null if it could not be determined.
     */
    public URL getImageURL() {
        String src = (String) this.getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
        if (src == null) {
            return null;
        }

        URL reference = ((HTMLDocument) this.getDocument()).getBase();
        try {
            URL u = new URL(reference, src);
            return u;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public Image getEmbedImage() {
        String src = (String) this.getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
        if (src == null) {
            return null;
        } else if (!src.startsWith("data:image/png;base64,")) {
            return null;
        }

        int index = src.indexOf("base64,");
        String encodedImg = src.substring(index + 7);
        byte[] bImg = Base64.decode(encodedImg);

        InputStream input = new ByteArrayInputStream(bImg);

        try {
            BufferedImage img = ImageIO.read(input);
            return img;
        } catch (IOException e) {
            OImageView.logger.trace(null, e);
        }
        return null;
    }

    /**
     * Returns the icon to use if the image couldn't be found.
     */
    public Icon getNoImageIcon() {
        return (Icon) UIManager.getLookAndFeelDefaults().get(OImageView.MISSING_IMAGE);
    }

    /**
     * Returns the icon to use while in the process of loading the image.
     */
    public Icon getLoadingImageIcon() {
        return (Icon) UIManager.getLookAndFeelDefaults().get(OImageView.PENDING_IMAGE);
    }

    /**
     * Returns the image to render.
     */
    public Image getImage() {
        this.sync();
        return this.image;
    }

    /**
     * Sets how the image is loaded. If <code>newValue</code> is true, the image we be loaded when first
     * asked for, otherwise it will be loaded asynchronously. The default is to not load synchronously,
     * that is to load the image asynchronously.
     */
    public void setLoadsSynchronously(boolean newValue) {
        synchronized (this) {
            if (newValue) {
                this.state |= OImageView.SYNC_LOAD_FLAG;
            } else {
                this.state = (this.state | OImageView.SYNC_LOAD_FLAG) ^ OImageView.SYNC_LOAD_FLAG;
            }
        }
    }

    /**
     * Returns true if the image should be loaded when first asked for.
     */
    public boolean getLoadsSynchronously() {
        return (this.state & OImageView.SYNC_LOAD_FLAG) != 0;
    }

    /**
     * Convenience method to get the StyleSheet.
     */
    protected StyleSheet getStyleSheet() {
        HTMLDocument doc = (HTMLDocument) this.getDocument();
        return doc.getStyleSheet();
    }

    /**
     * Fetches the attributes to use when rendering. This is implemented to multiplex the attributes
     * specified in the model with a StyleSheet.
     */
    @Override
    public AttributeSet getAttributes() {
        this.sync();
        return this.attr;
    }

    /**
     * For images the tooltip text comes from text specified with the <code>ALT</code> attribute. This
     * is overriden to return <code>getAltText</code>.
     *
     * @see JTextComponent#getToolTipText
     */
    @Override
    public String getToolTipText(float x, float y, Shape allocation) {
        return this.getAltText();
    }

    /**
     * Update any cached values that come from attributes.
     */
    protected void setPropertiesFromAttributes() {
        StyleSheet sheet = this.getStyleSheet();
        this.attr = sheet.getViewAttributes(this);

        // Gutters
        this.borderSize = (short) this.getIntAttr(HTML.Attribute.BORDER, this.isLink() ? OImageView.DEFAULT_BORDER : 0);

        this.leftInset = this.rightInset = (short) (this.getIntAttr(HTML.Attribute.HSPACE, 0) + this.borderSize);
        this.topInset = this.bottomInset = (short) (this.getIntAttr(HTML.Attribute.VSPACE, 0) + this.borderSize);

        this.borderColor = ((StyledDocument) this.getDocument()).getForeground(this.getAttributes());

        AttributeSet attr = this.getElement().getAttributes();

        // Alignment.
        // PENDING: This needs to be changed to support the CSS versions
        // when conversion from ALIGN to VERTICAL_ALIGN is complete.
        Object alignment = attr.getAttribute(HTML.Attribute.ALIGN);

        this.vAlign = 1.0f;
        if (alignment != null) {
            alignment = alignment.toString();
            if ("top".equals(alignment)) {
                this.vAlign = 0f;
            } else if ("middle".equals(alignment)) {
                this.vAlign = .5f;
            }
        }

        AttributeSet anchorAttr = (AttributeSet) attr.getAttribute(HTML.Tag.A);
        if ((anchorAttr != null) && anchorAttr.isDefined(HTML.Attribute.HREF)) {
            synchronized (this) {
                this.state |= OImageView.LINK_FLAG;
            }
        } else {
            synchronized (this) {
                this.state = (this.state | OImageView.LINK_FLAG) ^ OImageView.LINK_FLAG;
            }
        }
    }

    /**
     * Establishes the parent view for this view. Seize this moment to cache the AWT Container I'm in.
     */
    @Override
    public void setParent(View parent) {
        View oldParent = this.getParent();
        super.setParent(parent);
        this.container = parent != null ? this.getContainer() : null;
        if (oldParent != parent) {
            synchronized (this) {
                this.state |= OImageView.RELOAD_FLAG;
            }
        }
    }

    /**
     * Invoked when the Elements attributes have changed. Recreates the image.
     */
    @Override
    public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        super.changedUpdate(e, a, f);

        synchronized (this) {
            this.state |= OImageView.RELOAD_FLAG | OImageView.RELOAD_IMAGE_FLAG;
        }

        // Assume the worst.
        this.preferenceChanged(null, true, true);
    }

    /**
     * Paints the View.
     * @param g the rendering surface to use
     * @param a the allocated region to render into
     * @see View#paint
     */
    @Override
    public void paint(Graphics g, Shape a) {
        this.sync();

        Rectangle rect = a instanceof Rectangle ? (Rectangle) a : a.getBounds();

        Image image = this.getImage();
        Rectangle clip = g.getClipBounds();

        this.fBounds.setBounds(rect);
        this.paintHighlights(g, a);
        this.paintBorder(g, rect);
        if (clip != null) {
            g.clipRect(rect.x + this.leftInset, rect.y + this.topInset, rect.width - this.leftInset - this.rightInset,
                    rect.height - this.topInset - this.bottomInset);
        }
        if (image != null) {
            if (!this.hasPixels(image)) {
                // No pixels yet, use the default
                Icon icon = image == null ? this.getNoImageIcon() : this.getLoadingImageIcon();

                if (icon != null) {
                    icon.paintIcon(this.getContainer(), g, rect.x + this.leftInset, rect.y + this.topInset);
                }
            } else {
                // Draw the image
                g.drawImage(image, rect.x + this.leftInset, rect.y + this.topInset, this.width, this.height,
                        this.imageObserver);
            }
        } else {
            Icon icon = this.getNoImageIcon();

            if (icon != null) {
                icon.paintIcon(this.getContainer(), g, rect.x + this.leftInset, rect.y + this.topInset);
            }
            View view = this.getAltView();
            // Paint the view representing the alt text, if its non-null
            if ((view != null)
                    && (((this.state & OImageView.WIDTH_FLAG) == 0) || (this.width > OImageView.DEFAULT_WIDTH))) {
                // Assume layout along the y direction
                Rectangle altRect = new Rectangle(rect.x + this.leftInset + OImageView.DEFAULT_WIDTH,
                        rect.y + this.topInset,
                        rect.width - this.leftInset - this.rightInset - OImageView.DEFAULT_WIDTH,
                        rect.height - this.topInset - this.bottomInset);

                view.paint(g, altRect);
            }
        }
        if (clip != null) {
            // Reset clip.
            g.setClip(clip.x, clip.y, clip.width, clip.height);
        }
    }

    protected void paintHighlights(Graphics g, Shape shape) {
        if (this.container instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) this.container;
            Highlighter h = tc.getHighlighter();
            if (h instanceof LayeredHighlighter) {
                ((LayeredHighlighter) h).paintLayeredHighlights(g, this.getStartOffset(), this.getEndOffset(), shape,
                        tc, this);
            }
        }
    }

    protected void paintBorder(Graphics g, Rectangle rect) {
        Color color = this.borderColor;

        if (((this.borderSize > 0) || (this.image == null)) && (color != null)) {
            int xOffset = this.leftInset - this.borderSize;
            int yOffset = this.topInset - this.borderSize;
            g.setColor(color);
            int n = this.image == null ? 1 : this.borderSize;
            for (int counter = 0; counter < n; counter++) {
                g.drawRect(rect.x + xOffset + counter, rect.y + yOffset + counter,
                        rect.width - counter - counter - xOffset - xOffset - 1,
                        rect.height - counter - counter - yOffset - yOffset - 1);
            }
        }
    }

    /**
     * Determines the preferred span for this view along an axis.
     * @param axis may be either X_AXIS or Y_AXIS
     * @return the span the view would like to be rendered into; typically the view is told to render
     *         into the span that is returned, although there is no guarantee; the parent may choose to
     *         resize or break the view
     */
    @Override
    public float getPreferredSpan(int axis) {
        this.sync();

        // If the attributes specified a width/height, always use it!
        if ((axis == View.X_AXIS) && ((this.state & OImageView.WIDTH_FLAG) == OImageView.WIDTH_FLAG)) {
            this.getPreferredSpanFromAltView(axis);
            return this.width + this.leftInset + this.rightInset;
        }
        if ((axis == View.Y_AXIS) && ((this.state & OImageView.HEIGHT_FLAG) == OImageView.HEIGHT_FLAG)) {
            this.getPreferredSpanFromAltView(axis);
            return this.height + this.topInset + this.bottomInset;
        }

        Image image = this.getImage();

        if (image != null) {
            switch (axis) {
                case View.X_AXIS:
                    return this.width + this.leftInset + this.rightInset;
                case View.Y_AXIS:
                    return this.height + this.topInset + this.bottomInset;
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        } else {
            View view = this.getAltView();
            float retValue = 0f;

            if (view != null) {
                retValue = view.getPreferredSpan(axis);
            }
            switch (axis) {
                case View.X_AXIS:
                    return retValue + (this.width + this.leftInset + this.rightInset);
                case View.Y_AXIS:
                    return retValue + (this.height + this.topInset + this.bottomInset);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }

    /**
     * Determines the desired alignment for this view along an axis. This is implemented to give the
     * alignment to the bottom of the icon along the y axis, and the default along the x axis.
     * @param axis may be either X_AXIS or Y_AXIS
     * @return the desired alignment; this should be a value between 0.0 and 1.0 where 0 indicates
     *         alignment at the origin and 1.0 indicates alignment to the full span away from the
     *         origin; an alignment of 0.5 would be the center of the view
     */
    @Override
    public float getAlignment(int axis) {
        switch (axis) {
            case View.Y_AXIS:
                return this.vAlign;
            default:
                return super.getAlignment(axis);
        }
    }

    /**
     * Provides a mapping from the document model coordinate space to the coordinate space of the view
     * mapped to it.
     * @param pos the position to convert
     * @param a the allocated region to render into
     * @return the bounding box of the given position
     * @exception BadLocationException if the given position does not represent a valid location in the
     *            associated document
     * @see View#modelToView
     */
    @Override
    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        int p0 = this.getStartOffset();
        int p1 = this.getEndOffset();
        if ((pos >= p0) && (pos <= p1)) {
            Rectangle r = a.getBounds();
            if (pos == p1) {
                r.x += r.width;
            }
            r.width = 0;
            return r;
        }
        return null;
    }

    /**
     * Provides a mapping from the view coordinate space to the logical coordinate space of the model.
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param a the allocated region to render into
     * @return the location within the model that best represents the given point of view
     * @see View#viewToModel
     */
    @Override
    public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
        Rectangle alloc = (Rectangle) a;
        if (x < (alloc.x + alloc.width)) {
            bias[0] = Position.Bias.Forward;
            return this.getStartOffset();
        }
        bias[0] = Position.Bias.Backward;
        return this.getEndOffset();
    }

    /**
     * Sets the size of the view. This should cause layout of the view if it has any layout duties.
     * @param width the width >= 0
     * @param height the height >= 0
     */
    @Override
    public void setSize(float width, float height) {
        this.sync();

        if (this.getImage() == null) {
            View view = this.getAltView();

            if (view != null) {
                view.setSize(Math.max(0f, width - (OImageView.DEFAULT_WIDTH + this.leftInset + this.rightInset)),
                        Math.max(0f, height - (this.topInset + this.bottomInset)));
            }
        }
    }

    /**
     * Returns true if this image within a link?
     */
    protected boolean isLink() {
        return (this.state & OImageView.LINK_FLAG) == OImageView.LINK_FLAG;
    }

    /**
     * Returns true if the passed in image has a non-zero width and height.
     */
    protected boolean hasPixels(Image image) {
        return (image != null) && (image.getHeight(this.imageObserver) > 0) && (image.getWidth(this.imageObserver) > 0);
    }

    /**
     * Returns the preferred span of the View used to display the alt text, or 0 if the view does not
     * exist.
     */
    protected float getPreferredSpanFromAltView(int axis) {
        if (this.getImage() == null) {
            View view = this.getAltView();

            if (view != null) {
                return view.getPreferredSpan(axis);
            }
        }
        return 0f;
    }

    /**
     * Request that this view be repainted. Assumes the view is still at its last-drawn location.
     */
    protected void repaint(long delay) {
        if ((this.container != null) && (this.fBounds != null)) {
            this.container.repaint(delay, this.fBounds.x, this.fBounds.y, this.fBounds.width, this.fBounds.height);
        }
    }

    /**
     * Convenience method for getting an integer attribute from the elements AttributeSet.
     */
    protected int getIntAttr(HTML.Attribute name, int deflt) {
        AttributeSet attr = this.getElement().getAttributes();
        if (attr.isDefined(name)) { // does not check parents!
            int i;
            String val = (String) attr.getAttribute(name);
            if (val == null) {
                i = deflt;
            } else {
                try {
                    i = Math.max(0, Integer.parseInt(val));
                } catch (NumberFormatException x) {
                    i = deflt;
                }
            }
            return i;
        } else {
            return deflt;
        }
    }

    /**
     * Makes sure the necessary properties and image is loaded.
     */
    protected void sync() {
        int s = this.state;
        if ((s & OImageView.RELOAD_IMAGE_FLAG) != 0) {
            this.refreshImage();
        }
        s = this.state;
        if ((s & OImageView.RELOAD_FLAG) != 0) {
            synchronized (this) {
                this.state = (this.state | OImageView.RELOAD_FLAG) ^ OImageView.RELOAD_FLAG;
            }
            this.setPropertiesFromAttributes();
        }
    }

    /**
     * Loads the image and updates the size accordingly. This should be invoked instead of invoking
     * <code>loadImage</code> or <code>updateImageSize</code> directly.
     */
    protected void refreshImage() {
        synchronized (this) {
            // clear out width/height/realoadimage flag and set loading flag
            this.state = (this.state | OImageView.LOADING_FLAG | OImageView.RELOAD_IMAGE_FLAG | OImageView.WIDTH_FLAG
                    | OImageView.HEIGHT_FLAG)
                    ^ (OImageView.WIDTH_FLAG | OImageView.HEIGHT_FLAG | OImageView.RELOAD_IMAGE_FLAG);
            this.image = null;
            this.width = this.height = 0;
        }

        try {
            // Load the image
            this.loadImage();

            // And update the size params
            this.updateImageSize();
        } finally {
            synchronized (this) {
                // Clear out state in case someone threw an exception.
                this.state = (this.state | OImageView.LOADING_FLAG) ^ OImageView.LOADING_FLAG;
            }
        }
    }

    /**
     * Loads the image from the URL <code>getImageURL</code>. This should only be invoked from
     * <code>refreshImage</code>.
     */
    protected void loadImage() {
        URL src = this.getImageURL();
        Image newImage = null;
        if (src != null) {
            Dictionary cache = (Dictionary) this.getDocument().getProperty(OImageView.IMAGE_CACHE_PROPERTY);
            if (cache != null) {
                newImage = (Image) cache.get(src);
            } else {
                newImage = Toolkit.getDefaultToolkit().createImage(src);
                if ((newImage != null) && this.getLoadsSynchronously()) {
                    // Force the image to be loaded by using an ImageIcon.
                    ImageIcon ii = new ImageIcon();
                    ii.setImage(newImage);
                }
            }
        }

        if (src == null) {
            newImage = this.getEmbedImage();
        }

        this.image = newImage;
    }

    /**
     * Recreates and reloads the image. This should only be invoked from <code>refreshImage</code>.
     */
    protected void updateImageSize() {
        int newWidth = 0;
        int newHeight = 0;
        int newState = 0;
        Image newImage = this.getImage();

        if (newImage != null) {
            Element elem = this.getElement();
            AttributeSet attr = elem.getAttributes();

            // Get the width/height and set the state ivar before calling
            // anything that might cause the image to be loaded, and thus the
            // ImageHandler to be called.
            newWidth = this.getIntAttr(HTML.Attribute.WIDTH, -1);
            if (newWidth > 0) {
                newState |= OImageView.WIDTH_FLAG;
            }
            newHeight = this.getIntAttr(HTML.Attribute.HEIGHT, -1);
            if (newHeight > 0) {
                newState |= OImageView.HEIGHT_FLAG;
            }

            if (newWidth <= 0) {
                newWidth = newImage.getWidth(this.imageObserver);
                if (newWidth <= 0) {
                    newWidth = OImageView.DEFAULT_WIDTH;
                }
            }

            if (newHeight <= 0) {
                newHeight = newImage.getHeight(this.imageObserver);
                if (newHeight <= 0) {
                    newHeight = OImageView.DEFAULT_HEIGHT;
                }
            }

            // Make sure the image starts loading:
            if ((newState & (OImageView.WIDTH_FLAG | OImageView.HEIGHT_FLAG)) != 0) {
                Toolkit.getDefaultToolkit().prepareImage(newImage, newWidth, newHeight, this.imageObserver);
            } else {
                Toolkit.getDefaultToolkit().prepareImage(newImage, -1, -1, this.imageObserver);
            }

            boolean createText = false;
            synchronized (this) {
                // If imageloading failed, other thread may have called
                // ImageLoader which will null out image, hence we check
                // for it.
                if (this.image != null) {
                    if (((newState & OImageView.WIDTH_FLAG) == OImageView.WIDTH_FLAG) || (this.width == 0)) {
                        this.width = newWidth;
                    }
                    if (((newState & OImageView.HEIGHT_FLAG) == OImageView.HEIGHT_FLAG) || (this.height == 0)) {
                        this.height = newHeight;
                    }
                } else {
                    createText = true;
                    if ((newState & OImageView.WIDTH_FLAG) == OImageView.WIDTH_FLAG) {
                        this.width = newWidth;
                    }
                    if ((newState & OImageView.HEIGHT_FLAG) == OImageView.HEIGHT_FLAG) {
                        this.height = newHeight;
                    }
                }
                this.state = this.state | newState;
                this.state = (this.state | OImageView.LOADING_FLAG) ^ OImageView.LOADING_FLAG;
            }
            if (createText) {
                // Only reset if this thread determined image is null
                this.updateAltTextView();
            }
        } else {
            this.width = this.height = OImageView.DEFAULT_HEIGHT;
            this.updateAltTextView();
        }
    }

    /**
     * Updates the view representing the alt text.
     */
    protected void updateAltTextView() {
        String text = this.getAltText();

        if (text != null) {
            ImageLabelView newView;

            newView = new ImageLabelView(this.getElement(), text);
            synchronized (this) {
                this.altView = newView;
            }
        }
    }

    /**
     * Returns the view to use for alternate text. This may be null.
     */
    protected View getAltView() {
        View view;

        synchronized (this) {
            view = this.altView;
        }
        if ((view != null) && (view.getParent() == null)) {
            view.setParent(this.getParent());
        }
        return view;
    }

    /**
     * Invokes <code>preferenceChanged</code> on the event displatching thread.
     */
    protected void safePreferenceChanged() {
        if (SwingUtilities.isEventDispatchThread()) {
            Document doc = this.getDocument();
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).readLock();
            }
            this.preferenceChanged(null, true, true);
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).readUnlock();
            }
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    OImageView.this.safePreferenceChanged();
                }
            });
        }
    }

    /**
     * ImageHandler implements the ImageObserver to correctly update the display as new parts of the
     * image become available.
     */
    protected class ImageHandler implements ImageObserver {

        // This can come on any thread. If we are in the process of reloading
        // the image and determining our state (loading) we don't fire
        // preference changed, or repaint, we just reset the fWidth/fHeight as
        // necessary and return. This is ok as we know when loading finishes
        // it will pick up the new height/width, if necessary.
        @Override
        public boolean imageUpdate(Image img, int flags, int x, int y, int newWidth, int newHeight) {
            if ((OImageView.this.image == null) || (OImageView.this.image != img)
                    || (OImageView.this.getParent() == null)) {
                return false;
            }

            // Bail out if there was an error:
            if ((flags & (ImageObserver.ABORT | ImageObserver.ERROR)) != 0) {
                OImageView.this.repaint(0);
                synchronized (OImageView.this) {
                    if (OImageView.this.image == img) {
                        // Be sure image hasn't changed since we don't
                        // initialy synchronize
                        OImageView.this.image = null;
                        if ((OImageView.this.state & OImageView.WIDTH_FLAG) != OImageView.WIDTH_FLAG) {
                            OImageView.this.width = OImageView.DEFAULT_WIDTH;
                        }
                        if ((OImageView.this.state & OImageView.HEIGHT_FLAG) != OImageView.HEIGHT_FLAG) {
                            OImageView.this.height = OImageView.DEFAULT_HEIGHT;
                        }
                    }
                    if ((OImageView.this.state & OImageView.LOADING_FLAG) == OImageView.LOADING_FLAG) {
                        // No need to resize or repaint, still in the process
                        // of loading.
                        return false;
                    }
                }
                OImageView.this.updateAltTextView();
                OImageView.this.safePreferenceChanged();
                return false;
            }

            // Resize image if necessary:
            short changed = 0;
            if (((flags & ImageObserver.HEIGHT) != 0)
                    && !OImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT)) {
                changed |= 1;
            }
            if (((flags & ImageObserver.WIDTH) != 0)
                    && !OImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.WIDTH)) {
                changed |= 2;
            }

            synchronized (OImageView.this) {
                if (OImageView.this.image != img) {
                    return false;
                }
                if (((changed & 1) == 1) && ((OImageView.this.state & OImageView.WIDTH_FLAG) == 0)) {
                    OImageView.this.width = newWidth;
                }
                if (((changed & 2) == 2) && ((OImageView.this.state & OImageView.HEIGHT_FLAG) == 0)) {
                    OImageView.this.height = newHeight;
                }
                if ((OImageView.this.state & OImageView.LOADING_FLAG) == OImageView.LOADING_FLAG) {
                    // No need to resize or repaint, still in the process of
                    // loading.
                    return true;
                }
            }
            if (changed != 0) {
                // May need to resize myself, asynchronously:
                OImageView.this.safePreferenceChanged();
                return true;
            }

            // Repaint when done or when new pixels arrive:
            if ((flags & (ImageObserver.FRAMEBITS | ImageObserver.ALLBITS)) != 0) {
                OImageView.this.repaint(0);
            } else if (((flags & ImageObserver.SOMEBITS) != 0) && OImageView.sIsInc) {
                OImageView.this.repaint(OImageView.sIncRate);
            }
            return (flags & ImageObserver.ALLBITS) == 0;
        }

    }

    /**
     * ImageLabelView is used if the image can't be loaded, and the attribute specified an alt
     * attribute. It overriden a handle of methods as the text is hardcoded and does not come from the
     * document.
     */
    protected class ImageLabelView extends InlineView {

        protected Segment segment;

        protected Color fg;

        ImageLabelView(Element e, String text) {
            super(e);
            this.reset(text);
        }

        public void reset(String text) {
            this.segment = new Segment(text.toCharArray(), 0, text.length());
        }

        @Override
        public void paint(Graphics g, Shape a) {
            // Don't use supers paint, otherwise selection will be wrong
            // as our start/end offsets are fake.
            GlyphPainter painter = this.getGlyphPainter();

            if (painter != null) {
                g.setColor(this.getForeground());
                painter.paint(this, g, a, this.getStartOffset(), this.getEndOffset());
            }
        }

        @Override
        public Segment getText(int p0, int p1) {
            if ((p0 < 0) || (p1 > this.segment.array.length)) {
                throw new RuntimeException("ImageLabelView: Stale view");
            }
            this.segment.offset = p0;
            this.segment.count = p1 - p0;
            return this.segment;
        }

        @Override
        public int getStartOffset() {
            return 0;
        }

        @Override
        public int getEndOffset() {
            return this.segment.array.length;
        }

        @Override
        public View breakView(int axis, int p0, float pos, float len) {
            // Don't allow a break
            return this;
        }

        @Override
        public Color getForeground() {
            View parent;
            if ((this.fg == null) && ((parent = this.getParent()) != null)) {
                Document doc = this.getDocument();
                AttributeSet attr = parent.getAttributes();

                if ((attr != null) && (doc instanceof StyledDocument)) {
                    this.fg = ((StyledDocument) doc).getForeground(attr);
                }
            }
            return this.fg;
        }

    }

}
