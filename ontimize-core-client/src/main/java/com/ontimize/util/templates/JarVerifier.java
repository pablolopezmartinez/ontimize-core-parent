package com.ontimize.util.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Check if the jars given by the properties file are in the classpath.
 *
 * @author Imatia Innovation
 * @since 11/07/2007
 */
public class JarVerifier {

    private static final Logger logger = LoggerFactory.getLogger(JarVerifier.class);

    public static boolean DEBUG = true;

    protected Hashtable jars;

    protected void log(String log) {
        if (JarVerifier.DEBUG) {
            JarVerifier.logger.debug(log);
        }
    }

    /**
     * Get a properties file from a jar resource.
     * @param resource
     */
    public JarVerifier(String resource) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
        if (is == null) {
            return;
        }
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            JarVerifier.logger.error(null, e);
        }
        this.jars = p;
    }

    public JarVerifier(Hashtable jars) {
        this.jars = jars;
    }

    public boolean verify() {
        this.log("Verifying the list of required jars.");

        if ((this.jars == null) || this.jars.isEmpty()) {
            return true;
        }

        Enumeration ki = this.jars.keys();
        Collection vc = this.jars.values();
        Iterator vi = vc.iterator();
        boolean hasAll = true;

        while (ki.hasMoreElements() && vi.hasNext()) {
            Object o = ki.nextElement();
            if ((o == null) || !(o instanceof String)) {
                vi.next();
                continue;
            }
            String k = (String) o; // Key = library name

            o = vi.next();
            if ((o == null) || !(o instanceof String)) {
                continue;
            }
            String v = (String) o; // Value = Name of the class.

            if (!this.loadClass(v)) {
                this.log("Class " + v + " of the jar " + k + " can't be found.");
                this.log("Warning: Check properties file.");
                hasAll = false;
            }
        }
        return hasAll;
    }

    protected boolean loadClass(String className) {
        try {
            Class.forName(className);
        } catch (Exception e) {
            JarVerifier.logger.trace(null, e);
            return false;
        }
        return true;
    }

}
