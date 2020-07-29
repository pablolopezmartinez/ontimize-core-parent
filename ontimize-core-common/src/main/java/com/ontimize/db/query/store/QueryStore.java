package com.ontimize.db.query.store;

import com.ontimize.db.query.QueryExpression;

public interface QueryStore {

    public void addQuery(String description, QueryExpression query);

    public void removeQuery(String description, String entity);

    public String[] list(String entity);

    public QueryExpression get(String description, String entity);

}
