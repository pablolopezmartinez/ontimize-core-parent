package com.ontimize.gui.preferences;

public interface PreferencesWarehouse {

    public String getPreference(int sessionId, String user, String preferenceName);

    public void setPreference(int sessionId, String user, String preferenceName, String value);

    public void savePreferences(int sessionId);

    public void loadPreferences(int sessionId);

}
