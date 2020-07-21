package com.ontimize.util.calendar;

import java.util.Hashtable;
import java.util.Locale;

public interface TimePeriodParser extends java.io.Serializable {

    public TimePeriod parse(String s, Locale l, String businessCalendarProperties) throws Exception;

    public void setPeriodAlias(Hashtable alias);

}
