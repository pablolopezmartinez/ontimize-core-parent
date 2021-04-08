package com.ontimize.gui.preferences;

public class PreferenceEvent extends java.util.EventObject {

    protected String user = null;

    protected String preference = null;

    protected String value = null;

    public PreferenceEvent(Object source, String user, String pref, String value) {
        super(source);
        this.user = user;
        this.preference = pref;
        this.value = value;
    }

    public String getUser() {
        return this.user;
    }

    public String getPreference() {
        return this.preference;
    }

    public String getValue() {
        return this.value;
    }

}
