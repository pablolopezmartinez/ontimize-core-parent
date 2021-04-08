package com.ontimize.builder;

import javax.swing.JToolBar;

public interface ButtonBarBuilder {

    /**
     * Creates the application toolbar with the components specified in the file (XML File).<br>
     * Parent component must be a JFrame, JDialog, JApplet, JInternalFrame,JRootPane
     * @param uriFile
     * @return
     */
    public JToolBar buildButtonBar(String uriFile);

    public void appendButtonBar(JToolBar toolbar, String xmlDefinition);

}
