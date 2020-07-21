package com.ontimize.util.swing.text;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class XMLEditorKit extends StyledEditorKit {

    public static final String XML_CONTENT_TYPE = "text/xml";

    protected ViewFactory factory = null;

    public XMLEditorKit() {
        this.factory = new XMLViewFactory();
    }

    @Override
    public ViewFactory getViewFactory() {
        return this.factory;
    }

    @Override
    public String getContentType() {
        return XMLEditorKit.XML_CONTENT_TYPE;
    }

}
