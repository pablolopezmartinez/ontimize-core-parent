package com.ontimize.util.swing.text;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class XMLViewFactory implements ViewFactory {

    @Override
    public View create(Element elem) {
        return new XMLView(elem);
    }

}
