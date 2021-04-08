package com.ontimize.util.webstart;

public class WebStartDownloadEvent extends java.util.EventObject {

    protected String jarName = null;

    public WebStartDownloadEvent(Object source, String jarName) {
        super(source);
        this.jarName = jarName;
    }

}
