package com.ontimize.db;

import com.ontimize.dto.EntityResult;

public interface DynamicMemoryEntity {

    public void setValue(EntityResult data);

    public void clear();

}
