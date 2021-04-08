package com.ontimize.db;

import com.ontimize.dto.EntityResult;

import java.util.Map;

public interface PrintDataEntity extends java.rmi.Remote {

    public EntityResult getPrintingData(Map keys, int sessionId) throws Exception;

}
