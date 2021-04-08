package com.ontimize.gui.field;

import java.util.Hashtable;
import java.util.Vector;

public interface IFilterElement {

    public boolean hasParentKeys();

    public Vector getParentKeyList();

    public Hashtable getParentKeyValues();

}
