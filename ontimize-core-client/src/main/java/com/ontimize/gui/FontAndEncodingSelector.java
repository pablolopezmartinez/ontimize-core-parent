package com.ontimize.gui;

import java.awt.Font;

public interface FontAndEncodingSelector {

    /**
     * Shows the available fonts in the system. The parameter specifies if only fonts with support for
     * EURO character must be showed
     */
    public Font showAvaliableFonts(boolean supportingEuro);

    /**
     * Set the font to use by the object
     */
    public void useFont(Font f);

}
