package com.ontimize.gui;

import java.util.Enumeration;
import java.util.Hashtable;

public class MultipleValue implements java.io.Serializable {

    private final Hashtable data = new Hashtable();

    public MultipleValue(Hashtable data) {
        if (data != null) {
            this.data.putAll(data);
        }
    }

    public Object get(Object attribute) {
        return this.data.get(attribute);
    }

    public void put(Object attribute, Object value) {
        this.data.put(attribute, value);
    }

    public void clear() {
        this.data.clear();
    }

    public Enumeration keys() {
        return this.data.keys();
    }

}
