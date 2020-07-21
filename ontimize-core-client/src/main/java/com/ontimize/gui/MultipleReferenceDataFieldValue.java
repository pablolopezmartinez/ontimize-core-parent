package com.ontimize.gui;

import java.io.Serializable;
import java.util.Hashtable;

public class MultipleReferenceDataFieldValue implements Serializable {

    private final Hashtable data = new Hashtable();

    private Hashtable cache = null;

    private long time = 0;

    public MultipleReferenceDataFieldValue(Hashtable data) {
        if (data != null) {
            this.data.putAll(data);
        }
    }

    public void setDataCache(Hashtable c, long time) {
        this.cache = c;
        this.time = time;
    }

    public Hashtable getCache() {
        return this.cache;
    }

    public long getCachedTime() {
        return this.time;
    }

}
