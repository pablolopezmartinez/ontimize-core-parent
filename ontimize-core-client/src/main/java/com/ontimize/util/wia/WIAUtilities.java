package com.ontimize.util.wia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WIAUtilities {

    private static final Logger logger = LoggerFactory.getLogger(WIAUtilities.class);

    private static boolean wiaEnabled = false;

    private static void check() {
        try {
            Class.forName("eu.gnome.morena.wia.WIAManager");
            WIAUtilities.logger.debug("WIAUtilities: WIA classes found");
            WIAUtilities.wiaEnabled = true;
        } catch (Exception e) {
            WIAUtilities.logger.trace("WIAUtilities: No WIA classes found " + e.getMessage(), e);
            WIAUtilities.wiaEnabled = false;
        }
    }

    static {
        WIAUtilities.check();
    }

    public static boolean isWIAEnabled() {
        return WIAUtilities.wiaEnabled;
    }

}
