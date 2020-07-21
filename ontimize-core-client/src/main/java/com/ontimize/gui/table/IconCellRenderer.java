package com.ontimize.gui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.ParseUtils;

public class IconCellRenderer extends CellRenderer implements Internationalization {

    private static final Logger logger = LoggerFactory.getLogger(IconCellRenderer.class);

    public static final String ICON = "icon";

    public static final String URL = "url";

    public static final String SHOW_TEXT = "showtext";

    public static final String TRANSLATE = "translate";

    protected Hashtable icons = new Hashtable();

    protected boolean showText = false;

    protected boolean translate = true;

    protected JTable[] tables = new JTable[0];

    protected ResourceBundle resourceFile = null;

    private String defaultIcon = null;

    protected class ImgObserver implements java.awt.image.ImageObserver {

        @Override
        public boolean imageUpdate(java.awt.Image img, int flags, int x, int y, int w, int h) {
            if ((flags & (ImageObserver.FRAMEBITS | ImageObserver.ALLBITS)) != 0) {
                for (int i = 0; i < IconCellRenderer.this.tables.length; i++) {
                    if (IconCellRenderer.this.tables[i].isShowing()) {
                        IconCellRenderer.this.tables[i].repaint(IconCellRenderer.this.tables[i].getVisibleRect());
                    }
                }
            }
            return (flags & (ImageObserver.ALLBITS | ImageObserver.ABORT)) == 0;
        }

    }

    protected ImgObserver observer = null;

    public IconCellRenderer(String uriProperties) throws Exception {
        this(uriProperties, false);
        this.setHorizontalTextPosition(SwingConstants.LEFT);
    }

    public IconCellRenderer(String uriProperties, boolean showText) throws Exception {
        this.configureIcon(uriProperties);
        this.showText = showText;
    }

    public IconCellRenderer(Hashtable parameters) throws Exception {
        Object uriProperties = parameters.get(IconCellRenderer.URL);
        if (uriProperties == null) {
            throw new Exception("Not found 'url' parameter");
        }
        this.configureIcon(uriProperties.toString());
        this.showText = ParseUtils.getBoolean((String) parameters.get(IconCellRenderer.SHOW_TEXT), false);
        this.translate = ParseUtils.getBoolean((String) parameters.get(IconCellRenderer.TRANSLATE), true);
    }

    protected void configureIcon(String uriProperties) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(uriProperties);
        Properties props = new Properties();
        props.load(url.openStream());
        this.observer = new ImgObserver();
        // Create a cache with the icons
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration enumKeys = props.propertyNames();
        while (enumKeys.hasMoreElements()) {
            String sKey = (String) enumKeys.nextElement();
            String uri = props.getProperty(sKey);
            url = ImageManager.getIconURL(uri);
            if (url == null) {
                url = cl.getResource(uri);
            }
            if (url == null) {
                IconCellRenderer.logger.error("{}: Not found {}", this.getClass().toString(), uri);
            } else {
                ImageIcon icon = new ImageIcon(url);
                icon.setImageObserver(this.observer);
                this.icons.put(sKey, icon);
            }
        }
    }

    public IconCellRenderer(String uriProperties, boolean showText, boolean translate) throws Exception {
        URL url = this.getClass().getClassLoader().getResource(uriProperties);
        if (url == null) {
            throw new Exception("Not found " + uriProperties);
        }
        Properties props = new Properties();
        props.load(url.openStream());
        this.observer = new ImgObserver();
        // Create a cache with the icons
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration enumKeys = props.propertyNames();
        while (enumKeys.hasMoreElements()) {
            String sKey = (String) enumKeys.nextElement();
            String uri = props.getProperty(sKey);
            url = ImageManager.getIconURL(uri);
            if (url == null) {
                url = cl.getResource(uri);
            }
            if (url == null) {
                IconCellRenderer.logger.error("{}: Not found {}", this.getClass().toString(), uri);
            } else {
                ImageIcon icon = new ImageIcon(url);
                icon.setImageObserver(this.observer);
                this.icons.put(sKey, icon);
            }
        }
        this.showText = showText;
        this.translate = translate;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
            int row, int column) {
        if (table != null) {
            synchronized (this) {
                boolean exist = false;
                for (int i = 0; i < this.tables.length; i++) {
                    if (table == this.tables[i]) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    JTable[] jAuxTable = new JTable[this.tables.length + 1];
                    System.arraycopy(this.tables, 0, jAuxTable, 0, this.tables.length);
                    jAuxTable[this.tables.length] = table;
                    this.tables = jAuxTable;
                }
            }
        }
        String sTextValue = null;
        if (this.showText && (value != null)) {
            String aux = value.toString();
            sTextValue = ApplicationManager.getTranslation(aux, this.resourceFile);
        } else {
            sTextValue = "";
        }

        Component c = super.getTableCellRendererComponent(table, sTextValue, selected, hasFocus, row, column);
        // Compare the string value
        if (value == null) {
            if (this.defaultIcon != null) {
                if (this.icons.containsKey(this.defaultIcon)) {
                    ((JLabel) c).setIcon((Icon) this.icons.get(this.defaultIcon));
                } else {
                    URL url = this.getClass().getClassLoader().getResource(this.defaultIcon);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        icon.setImageObserver(this.observer);
                        this.icons.put(this.defaultIcon, icon);
                    } else {
                        ((JLabel) c).setIcon(null);
                    }
                }
            } else {
                ((JLabel) c).setIcon(null);
            }
            return c;
        }
        String v = value.toString();
        if (this.icons.containsKey(v)) {
            ((JLabel) c).setIcon((Icon) this.icons.get(v));
        } else {
            if (this.defaultIcon != null) {
                if (this.icons.containsKey(this.defaultIcon)) {
                    ((JLabel) c).setIcon((Icon) this.icons.get(this.defaultIcon));
                } else {
                    URL url = this.getClass().getClassLoader().getResource(this.defaultIcon);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        icon.setImageObserver(this.observer);
                        this.icons.put(this.defaultIcon, icon);
                    } else {
                        ((JLabel) c).setIcon(null);
                    }
                }
            } else {
                ((JLabel) c).setIcon(null);
            }
        }
        if (this.showText && (value != null)) {
            String aux = value.toString();
            if (this.translate) {
                sTextValue = ApplicationManager.getTranslation(aux, this.resourceFile);
            }
            ((JLabel) c).setText(sTextValue);
        } else {
            ((JLabel) c).setText("");
        }
        return c;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return d;
    }

    @Override
    public void setResourceBundle(ResourceBundle res) {
        this.resourceFile = res;
    }

    @Override
    public void setComponentLocale(Locale l) {
    }

    @Override
    public Vector getTextsToTranslate() {
        return new Vector();
    }

    public void setDefaultIcon(String s) {
        this.defaultIcon = s;
    }

    public String getDefaultIcon() {
        return this.defaultIcon;
    }

}
