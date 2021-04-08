package com.ontimize.printing;

import java.text.DateFormat;
import java.util.Date;

public class PageHeader {

    protected String text = "";

    TextAttributes attributes = TextAttributes.getDefaultAttributes();

    public PageHeader(String headerText, TextAttributes textAttributes) {
        this.text = headerText;
        if (textAttributes != null) {
            this.attributes = textAttributes;
        }
    }

    public String toHTML() {
        return "<TABLE width='100%'>" + "<TR><TD>" + this.attributes.getStartTag() + this.text
                + this.attributes.getEndTag() + "</TD><TD align = 'right'>" + DateFormat
                    .getDateInstance()
                    .format(new Date())
                    .toString()
                + "</TD></TR></TABLE>";
    }

}
