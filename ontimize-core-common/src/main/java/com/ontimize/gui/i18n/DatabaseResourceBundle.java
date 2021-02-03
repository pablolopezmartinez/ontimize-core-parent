package com.ontimize.gui.i18n;

import java.io.Serializable;
import java.util.*;

public class DatabaseResourceBundle extends ResourceBundle implements Serializable {

    protected Map values;

    protected Locale locale;

    public DatabaseResourceBundle(Map data, Locale l) {
        this.values = data;
        if (this.values == null) {
            this.values = new HashMap();
        }
        this.locale = l;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public Enumeration getKeys() {
        return Collections.enumeration(this.values.keySet());
    }

    @Override
    protected Object handleGetObject(String key) {
        return this.values.get(key);
    }

    public Map getValues() {
        return this.values;
    }

    public void updateValues(Map values) {
        this.values = values;
    }

}
