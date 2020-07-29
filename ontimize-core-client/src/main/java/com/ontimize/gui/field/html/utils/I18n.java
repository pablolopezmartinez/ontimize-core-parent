package com.ontimize.gui.field.html.utils;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class I18n {

    private static final Logger logger = LoggerFactory.getLogger(I18n.class);

    private static final String MNEM_POSTFIX = ".mnemonic";

    private static I18n i18n;

    private I18n() {

    }

    public static I18n getInstance() {
        if (I18n.i18n == null) {
            I18n.i18n = new I18n();
        }

        return I18n.i18n;
    }

    public String str(String key) {
        try {
            return ApplicationManager.getTranslation(key);
        } catch (Exception ex) {
            I18n.logger.trace(null, ex);
            return '!' + key + '!';
        }
    }

    public char mnem(String key) {
        String s = this.str(key + I18n.MNEM_POSTFIX);
        if ((s != null) && (s.length() > 0)) {
            return s.charAt(0);
        }
        return '!';
    }

    /**
     * Converts slashes to dots in a pathname
     * @param path
     * @return
     */
    protected static String slashesToDots(String path) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(path, "/");
        while (st.hasMoreTokens()) {
            sb.append(".");
            sb.append(st.nextToken());
        }

        if (sb.toString().startsWith(".")) {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }

}
