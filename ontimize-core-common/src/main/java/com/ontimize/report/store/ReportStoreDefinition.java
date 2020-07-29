package com.ontimize.report.store;

import com.ontimize.db.query.QueryExpression;
import com.ontimize.report.ReportResource;

public interface ReportStoreDefinition extends ReportProperties {

    public String getXMLDefinition();

    public void setXMLDefinition(String def);

    public ReportResource[] getResources();

    public void setResources(ReportResource[] res);

    public QueryExpression getQueryExpression();

    public void setQueryExpression(QueryExpression queryExpression);

}
