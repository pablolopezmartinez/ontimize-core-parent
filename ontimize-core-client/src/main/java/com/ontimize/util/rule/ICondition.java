package com.ontimize.util.rule;

public interface ICondition {

    public void setExpression(String expression);

    public String getExpression();

    @Override
    public String toString();

}
