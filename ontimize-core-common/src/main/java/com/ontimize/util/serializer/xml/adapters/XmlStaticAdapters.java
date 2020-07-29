package com.ontimize.util.serializer.xml.adapters;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

public class XmlStaticAdapters {

    public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar cal) {
        return cal.toGregorianCalendar().getTime();
    }

}
