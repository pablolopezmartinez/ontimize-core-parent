package com.ontimize.cache;

import java.text.DateFormat;
import java.util.Hashtable;
import java.util.Locale;

public class DateFormatCache {

    private DateFormatCache() {

    }

    protected static DateFormatCache staticCache = new DateFormatCache();

    protected Hashtable cache = new Hashtable();

    public void put(Locale l, DateFormat df) {
        this.cache.put(l, df);
    }

    public boolean exists(Locale l) {
        return this.cache.containsKey(l);
    }

    public DateFormat get(Locale l) {
        return (DateFormat) this.cache.get(l);
    }

    public static void addDateFormat(Locale l, DateFormat df) {
        DateFormatCache.staticCache.put(l, df);
    }

    public static boolean containsDateFormat(Locale l) {
        if (l == null) {
            return false;
        }
        return DateFormatCache.staticCache.exists(l);
    }

    public static DateFormat getDateFormat(Locale l) {
        return DateFormatCache.staticCache.get(l);
    }

}
