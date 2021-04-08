package com.ontimize.gui.table;

/**
 * Interface that must be implemented by all the components that must be placed in a table. For
 * example, the buttons that are in the table controls panel.
 */
public interface TableComponent {

    public Object getKey();

    public void setKey(Object o);

}
