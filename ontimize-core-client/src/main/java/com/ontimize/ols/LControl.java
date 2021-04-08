package com.ontimize.ols;

import java.util.Hashtable;

public interface LControl {

    public Hashtable getParameters() throws Exception;

    public Hashtable updateL(Hashtable h) throws Exception;

}
