package com.ontimize.ols;

public interface LOk {

    public boolean ok() throws Exception;

    public boolean ok(String number) throws Exception;

    public boolean isDevelopementL() throws Exception;

    public String getLValue(String name) throws Exception;

    public String getLContent() throws Exception;

    public Object getLInfoObject() throws Exception;

}
