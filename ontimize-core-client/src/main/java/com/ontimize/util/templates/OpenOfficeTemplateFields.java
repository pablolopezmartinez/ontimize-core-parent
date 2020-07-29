package com.ontimize.util.templates;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.ontimize.db.EntityResult;

public class OpenOfficeTemplateFields {

    protected List singleFields = new Vector();

    protected Hashtable tableFields = new Hashtable();

    public OpenOfficeTemplateFields(List fields) {
        if (fields != null) {
            for (int i = 0; i < fields.size(); i++) {
                if (((String) fields.get(i)).indexOf(".") < 0) {
                    this.singleFields.add(fields.get(i));
                } else {

                    String entityName = ((String) fields.get(i)).substring(0, ((String) fields.get(i)).indexOf("."));
                    String columnName = ((String) fields.get(i)).substring(((String) fields.get(i)).indexOf(".") + 1);
                    if (this.tableFields.containsKey(entityName)) {
                        List columns = (List) this.tableFields.get(entityName);
                        columns.add(columnName);
                        this.tableFields.put(entityName, columns);
                    } else {
                        List columns = new Vector();
                        columns.add(columnName);
                        this.tableFields.put(entityName, columns);
                    }
                }
            }
        }
    }

    /**
     * @return the singleFields
     */
    public List getSingleFields() {
        return this.singleFields;
    }

    public List getTableNames() {
        List result = new Vector();
        Iterator entities = this.tableFields.keySet().iterator();
        while (entities.hasNext()) {
            result.add(entities.next());
        }
        return result;
    }

    public List getTableFields(String tableName) {
        return (List) this.tableFields.get(tableName);
    }

    public Hashtable checkTemplateFieldValues(Hashtable fieldValues) {
        Hashtable result = new Hashtable();
        if (fieldValues != null) {
            result.putAll(fieldValues);
        }
        for (int i = 0; i < this.singleFields.size(); i++) {
            if (!result.containsKey(this.singleFields.get(i))) {
                result.put(this.singleFields.get(i), " - ");
            }
        }

        return result;
    }

    public Hashtable checkTemplateTableValues(Hashtable tableValues) {
        Hashtable result = new Hashtable();
        if (tableValues != null) {
            result.putAll(tableValues);
        }
        Iterator entities = this.tableFields.keySet().iterator();
        while (entities.hasNext()) {
            String entity = (String) entities.next();
            if (!result.containsKey(entity)) {
                EntityResult resEnt = new EntityResult();
                List entityColumns = (List) this.tableFields.get(entity);
                Hashtable currentReg = new Hashtable();
                for (int i = 0; i < entityColumns.size(); i++) {
                    currentReg.put(entityColumns.get(i), "  ");
                }
                resEnt.addRecord(currentReg);
                result.put(entity, resEnt);
            }
        }
        return result;
    }

}
