package com.ontimize.db;

import java.util.Hashtable;
import java.util.Vector;

public interface CancellableQueryEntity extends CancellableOperationEntity {

    public EntityResult query(Hashtable keys, Vector attributes, int sessionId, String operationId) throws Exception;

}
