package com.ontimize.util.rule;

import java.util.List;

public interface IAction {

    public void setParams(List params);

    public List getParams();

    public void setId(String id);

    public String getId();

    public void setCondition(ICondition condition);

    public ICondition getCondition();

    @Override
    public String toString();

}
