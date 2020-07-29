package com.ontimize.gui.calendar.event;

import java.util.Calendar;
import java.util.EventObject;
import java.util.Locale;

public class CalendarEvent extends EventObject {

    private int day = -1;

    private int month = -1;

    private int year = -1;

    private Locale locale = null;

    public CalendarEvent(Object source, int day, int month, int year, Locale l) {
        super(source);
        this.day = day;
        this.month = month;
        this.year = year;
        this.locale = l;
        Calendar c = Calendar.getInstance(l);
        c.set(Calendar.YEAR, year);
        int minimumMonth = c.getActualMinimum(Calendar.MONTH);
        int maximumMonth = c.getActualMaximum(Calendar.MONTH);
        if ((this.month < minimumMonth) || (this.month > maximumMonth)) {
            throw new IllegalArgumentException("Invalid Month");
        }

        c.set(Calendar.MONTH, month);
        int minimumDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
        int maximumDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if ((this.day < minimumDay) || (this.day > maximumDay)) {
            throw new IllegalArgumentException("Invalid day");
        }

    }

    public Locale getLocale() {
        return this.locale;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

}
