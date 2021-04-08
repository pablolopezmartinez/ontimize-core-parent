package com.ontimize.util.rule;

import java.util.List;

public interface IRule {

    public ICondition getCondition();

    public void setCondition(ICondition condition);

    public List getActions();

    public void addAction(IAction action);

    @Override
    public String toString();

}
