package com.ontimize.gui;


import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlafPreferences {

    private static final Logger logger = LoggerFactory.getLogger(PlafPreferences.class);

    private static final String STYLE_KEY = "com.ontimize.gui.lafstyle";
    private static final String FONT_SIZE_KEY = "com.ontimize.gui.laf_font_size";
    private static final String FONT_NAME_KEY = "com.ontimize.gui.laf_font_name";
    private static final String FONT_STYLE_KEY = "com.ontimize.gui.laf_style_name";

    private static final String PLAF_PREFERENCE_KEY = "com.ontimize.gui.lafpreferences";

    private String preferencesFile;
    private Properties preferences;

    private static PlafPreferences instance;

    private PlafPreferences(String file) {
        this.preferencesFile = file;
    }

    public static PlafPreferences getInstance() {
        if (instance==null){
            String fileName = System.getProperty("com.ontimize.gui.lafpreferences");
            if (fileName!=null){
                instance = new PlafPreferences(fileName);
                instance.loadPreferences();
            }
        }
        return instance;
    }

    private void loadPreferences() {
        try {
            FileInputStream fileInputStream = new FileInputStream(retrievePreferencesFile());
            this.preferences = new Properties();
            preferences.load(fileInputStream);
            processPreferences(preferences);
        } catch (IOException e) {
            logger.error("Loading -> {} ",preferencesFile, e);
        }
    }

    private void processPreferences(Properties preferences) {
        if (preferences.containsKey(STYLE_KEY)){
            String value = preferences.getProperty(STYLE_KEY);
            System.setProperty(STYLE_KEY, value);
        }

        String fontSize, fontName, fontStyle;
        boolean applyFont = false;

        if (preferences.containsKey(FONT_SIZE_KEY)){
            fontSize = preferences.getProperty(FONT_SIZE_KEY);
            applyFont = true;
        }else{
            fontSize = "12";
        }
        if (preferences.containsKey(FONT_NAME_KEY)){
            fontName = preferences.getProperty(FONT_NAME_KEY);
            applyFont = true;
        }else{
            fontName = "Arial";
        }

        if (preferences.containsKey(FONT_STYLE_KEY)){
            fontStyle = preferences.getProperty(FONT_STYLE_KEY);
            applyFont = true;
        }else{
            fontStyle = "PLAIN";
        }

        if (applyFont){
            Font defaultFont = Font.decode(fontName+"-"+fontStyle+"-"+fontSize);
            //new Font(fontName, Font.PLAIN, Integer.parseInt(fontSize));
            setDefaultAppFont(defaultFont);
        }
    }

    private File retrievePreferencesFile() throws IOException{
        String directory = System.getProperty("user.home");
        StringBuilder builder = new StringBuilder(this.preferencesFile);
        builder.append(".plaf.pref");
        File file = new File(directory, builder.toString());
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    private void savePreferences(){
        try {
            FileOutputStream outputStream = new FileOutputStream(retrievePreferencesFile());
            preferences.store(outputStream,"");
        } catch (IOException e) {
            logger.error("Saving -> {} ",preferencesFile, e);
        }
    }

    public void setPreference(String preferenceName, String value) {
        if (value!=null){
            preferences.setProperty(preferenceName, value);
        }else{
            preferences.remove(preferenceName);
        }
    }

    public String getPreference(String preferenceName) {
        return preferences.getProperty(preferenceName);
    }

    public static Font getDefaultAppFont(){
        String value = System.getProperty(PLAF_PREFERENCE_KEY);
        if (value!=null){
            try{
                // com.ontimize.plaf.OntimizeLookAndFeel.defaultAppFont
                Class plafClass = Class.forName("com.ontimize.plaf.OntimizeLookAndFeel");
                Field f = plafClass.getField("defaultAppFont");
                return (Font)f.get(null);
            }catch (Throwable ex){
            }
        }
        return null;
    }

    public static void setDefaultAppFont(Font font){

        try{
            // com.ontimize.plaf.OntimizeLookAndFeel.defaultAppFont
            Class plafClass = Class.forName("com.ontimize.plaf.OntimizeLookAndFeel");
            Field f = plafClass.getField("defaultAppFont");
            f.set(null, font);
        }catch (Throwable ex){
        }
    }

    public void setFontPreference(Font font){
        if (font==null){
            setDefaultAppFont(null);
            this.setPreference(FONT_NAME_KEY, null);
            this.setPreference(FONT_SIZE_KEY, null);
            this.setPreference(FONT_STYLE_KEY, null);
        }

        String vFont = font.toString();
        String fontName = font.getFontName();

        String  strStyle;
        if (font.isBold()) {
            strStyle = font.isItalic() ? "bolditalic" : "bold";
        } else {
            strStyle = font.isItalic() ? "italic" : "plain";
        }
        String strSize = String.valueOf(font.getSize());

        this.setPreference(FONT_NAME_KEY, fontName);
        this.setPreference(FONT_SIZE_KEY, strSize);
        this.setPreference(FONT_STYLE_KEY, strStyle);
        this.savePreferences();
        setDefaultAppFont(font);
    }

    public void setStylePreference(String style){
        System.setProperty(STYLE_KEY, style);
        this.setPreference(STYLE_KEY, style);
        this.savePreferences();
    }


}
