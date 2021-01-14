package com.ontimize.xml;

import com.ontimize.util.remote.BytesBlock;

import java.util.Hashtable;

public interface XMLClientProvider extends XMLFormProvider {

    public Hashtable getFormManagerParameters(String formManagerId, int userid) throws Exception;

    public String getXMLRules(String form, int userid) throws Exception;

    public String getXMLMenu(int userid) throws Exception;

    public void reloadXMLMenu(int userId) throws Exception;

    public String getXMLToolbar(int userid) throws Exception;

    public void reloadXMLToolbar(int userId) throws Exception;

    public BytesBlock getImage(String image, int userId) throws Exception;

}
