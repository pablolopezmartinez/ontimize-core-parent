package com.ontimize.gui.container;

import java.awt.LayoutManager;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ButtonGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.Freeable;
import com.ontimize.gui.field.FormComponent;

/**
 * This class creates a radio item group of buttons.
 *
 * <p>
 *
 * @author Imatia Innovation
 */
public class RadioItemGroup extends ButtonGroup implements FormComponent, Freeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadioItemGroup.class);

    /**
     * Class constructor. Empty.
     * <p>
     * @param parameters the <code>Hashtable</code> with parameters
     */
    public RadioItemGroup(Hashtable parameters) {
    }

    @Override
    public Object getConstraints(LayoutManager layout) {
        return null;
    }

    @Override
    public void init(Hashtable parameters) {
    }

    /**
     * Gets the attribute.
     * <p>
     * @return null
     */
    public Object getAttribute() {
        return null;
    }

    @Override
    public void setResourceBundle(ResourceBundle resources) {
    }

    @Override
    public Vector getTextsToTranslate() {
        Vector v = new Vector();
        return v;
    }

    @Override
    public void setComponentLocale(Locale l) {
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public void free() {
        if (com.ontimize.gui.ApplicationManager.DEBUG) {
            RadioItemGroup.LOGGER.debug(this.getClass().toString() + " Free");
        }
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
