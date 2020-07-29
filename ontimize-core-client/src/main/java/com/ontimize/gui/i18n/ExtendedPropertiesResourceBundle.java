package com.ontimize.gui.i18n;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExtendedPropertiesResourceBundle extends ResourceBundle implements Serializable {

    protected Hashtable values;

    protected Locale locale;

    public ExtendedPropertiesResourceBundle(Hashtable data, Locale l) {
        this.values = data;
        if (this.values == null) {
            this.values = new Hashtable();
        }
        this.locale = l;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public Enumeration getKeys() {
        return this.values.keys();
    }

    @Override
    protected Object handleGetObject(String key) {
        return this.values.get(key);
    }

    public Hashtable getValues() {
        return this.values;
    }

    public void updateValues(Hashtable values) {
        this.values = values;
    }

}
