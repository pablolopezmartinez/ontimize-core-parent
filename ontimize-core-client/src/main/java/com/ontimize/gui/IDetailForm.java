package com.ontimize.gui;

import java.util.Hashtable;
import java.util.List;

import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.gui.table.Table;

public interface IDetailForm extends Internationalization, Freeable {

    public Form getForm();

    public void showDetailForm();

    public void hideDetailForm();

    public Table getTable();

    public void setQueryInsertMode();

    public void setQueryMode();

    public void setInsertMode();

    public void setAttributeToFix(Object attribute, Object value);

    public void resetParentkeys(List parentKeys);

    public void setParentKeyValues(Hashtable parentKeyValues);

    /**
     * This method sets the keys in the table records.<br>
     * This keys are used to query the record values
     * @param tableKeys
     * @param index
     */
    public void setKeys(Hashtable tableKeys, int index);

    public void setUpdateMode();

    public String getTableFieldName(Object name);

    public String getFormFieldName(Object name);

    public Hashtable valuesToTable(Hashtable values);

    public Hashtable valuesToForm(Hashtable values);

}
