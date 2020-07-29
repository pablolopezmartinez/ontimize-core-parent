package com.ontimize.util.rule;

import java.util.Hashtable;
import java.util.List;

public interface IEvent {

    public String getType();

    public void setType(String type);

    public List getRules();

    public Hashtable getAttributes();

    public void setAttributes(Hashtable attributes);

    public void addRule(IRule rule);

    @Override
    public String toString();

}
