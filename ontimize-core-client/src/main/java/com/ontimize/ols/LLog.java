package com.ontimize.ols;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class LLog {

    private static final Logger logger = LoggerFactory.getLogger(LLog.class);

    public static boolean DEBUG = false;

    private static final String logFile = "LogFile";

    private static String fileName = null;

    private static BufferedWriter writer = null;

    // Borrar el fichero de configuracion LicenseLog.properties
    // para que por defecto lo saque en el system.out
    static {
        try {

            String s = System.getProperty("com.ontimize.ols.LLog.DEBUG", "false");
            LLog.DEBUG = ApplicationManager.parseStringValue(s, false);

            InputStream is = LLog.class.getResourceAsStream("prop/LicenseLog.properties");
            if (is != null) {
                Properties prop = new Properties();
                prop.load(is);
                is.close();
                LLog.fileName = prop.getProperty(LLog.logFile);
                LLog.writer = new BufferedWriter(new FileWriter(LLog.fileName, true));
            }

        } catch (Exception e) {
            LLog.logger.error(null, e);
        }
    }

    public static void log(String s) {
        if (!LLog.DEBUG) {
            return;
        }

        if (LLog.writer == null) {
            LLog.logger.debug("LCS: " + s);
        } else {
            try {
                s = new Date().toString() + "--> " + s;
                LLog.writer.write(s);
                LLog.writer.newLine();
                LLog.writer.flush();
            } catch (Exception e) {
                LLog.logger.error(null, e);
            }
        }
    }

}
