package com.ontimize.db;

import java.util.Hashtable;
import java.util.Vector;

public interface CancellableQueryEntity extends CancellableOperationEntity {

    public EntityResultMapImpl query(Hashtable keys, Vector attributes, int sessionId, String operationId) throws Exception;

}
