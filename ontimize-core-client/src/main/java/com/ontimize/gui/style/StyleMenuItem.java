package com.ontimize.gui.style;

import java.util.Hashtable;

import com.ontimize.gui.RadioMenuItem;
import com.ontimize.gui.i18n.LocaleMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StyleMenuItem extends RadioMenuItem {

    private static final Logger logger = LoggerFactory.getLogger(StyleMenuItem.class);

    private String style;
    /**
     * The class constructor. Calls to super, initializes parameters and permissions and sets margin.
     * <p>
     *
     * @param parameters the hashtable with parameters
     */
    public StyleMenuItem(Hashtable parameters) {
        super(parameters);

        style = (String) parameters.get("style");
        if (style == null) {
            StyleMenuItem.logger.debug("'style' parameter is mandatory");
            throw new RuntimeException("'style' parameter is mandatory");
        }
    }

    public String getStyle(){
        return this.style;
    }
}
