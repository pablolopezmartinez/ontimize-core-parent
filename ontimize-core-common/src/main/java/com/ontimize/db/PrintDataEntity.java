package com.ontimize.db;

import java.util.Hashtable;

public interface PrintDataEntity extends java.rmi.Remote {

    public EntityResult getPrintingData(Hashtable keys, int sessionId) throws Exception;

}
