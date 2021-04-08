package com.ontimize.xml;

import com.ontimize.gui.Form;
import com.ontimize.printing.TemplateElement;

public class DefaultFormPrinter {

    private DefaultFormPrinter() {
    }

    public static TemplateElement printForm(Form f) throws Exception {
        XMLTemplateBuilder constructor = new XMLTemplateBuilder("com/ontimize/gui/labels.xml");
        StringBuffer fileContent = DefaultFormPrinter.getXMLTemplate(f);
        TemplateElement p = constructor.buildTemplate(fileContent);
        p.setContent(f.getDataFieldText());
        return p;
    }

    public static StringBuffer getXMLTemplate(Form f) {
        return null;
    }

}
