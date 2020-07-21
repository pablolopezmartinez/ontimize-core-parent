package com.ontimize.ols.compatibility;

import java.awt.Window;
import java.io.File;

public interface ICompatible {

    public String encode(String str, String encoding) throws Exception;

    public File getLFile(String filename) throws Exception;

    public void setFocusable(Window w, boolean focusable);

    public boolean verify(String s);

}
