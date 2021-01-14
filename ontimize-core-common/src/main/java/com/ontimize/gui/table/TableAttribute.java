package com.ontimize.gui.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Attribute used for the tables. Contains information about the column names, the entity, the keys,
 * the parent keys and the number of records to query
 */
public class TableAttribute extends Hashtable {

    private static final Logger logger = LoggerFactory.getLogger(TableAttribute.class);

    protected int recordNumberToInitiallyDownload = -1;

    protected String entity = null;

    protected Vector attributes = new Vector();

    protected int totalRecordNumberInQuery = 0;

    protected Vector parentkeys = new Vector();

    protected Hashtable hParentkeyEquivalences;

    protected Vector keys = null;

    protected Hashtable queryFilter;

    protected Vector orderBy;

    /**
     * Creates a TableAttribute with recordNumberToInitiallyDownload= -1
     */
    public TableAttribute() {
        super();
    }

    /**
     * Creates a new TableAttribute.<br>
     * Parameter 'recordNumberToInitiallyDownload' indicates the number of records to download when a
     * query includes this attribute . If this parameter is less than 0 then no limit exists.
     */
    public TableAttribute(int recordNumberToInitiallyDownload) {
        super();
        this.recordNumberToInitiallyDownload = recordNumberToInitiallyDownload;
    }

    public int getQueryRecordNumber() {
        return this.totalRecordNumberInQuery;
    }

    public void setTotalRecordNumberInQuery(int recordNumberToInitiallyDownload) {
        this.totalRecordNumberInQuery = recordNumberToInitiallyDownload;
    }

    public int getRecordNumberToInitiallyDownload() {
        return this.recordNumberToInitiallyDownload;
    }

    public void setRecordNumberToInitiallyDownload(int recordNumberToInitiallyDownload) {
        this.recordNumberToInitiallyDownload = recordNumberToInitiallyDownload;
    }

    public void setEntityAndAttributes(String entity, Vector attributes) {
        this.entity = entity;
        this.attributes = attributes;
        this.put(entity, attributes);
    }

    public void setKeysParentkeysOtherkeys(Vector keys, Vector parentkeys) {
        this.keys = keys;
        this.parentkeys = parentkeys;
    }

    public Hashtable getParentkeyEquivalences() {
        return this.hParentkeyEquivalences;
    }

    public String getParentkeyEquivalence(String parentkey) {
        if ((this.hParentkeyEquivalences != null) && this.hParentkeyEquivalences.containsKey(parentkey)) {
            return (String) this.hParentkeyEquivalences.get(parentkey);
        }
        return parentkey;
    }

    public void setParentkeyEquivalences(Hashtable hParentkeyEquivalences) {
        this.hParentkeyEquivalences = hParentkeyEquivalences;
    }

    public String getEntity() {
        return this.entity;
    }

    public Vector getAttributes() {
        return (Vector) this.attributes.clone();
    }

    public Vector getKeys() {
        return (Vector) this.keys.clone();
    }

    public Vector getParentKeys() {
        return (Vector) this.parentkeys.clone();
    }

    public Hashtable getQueryFilter() {
        return this.queryFilter;
    }

    public void setQueryFilter(Hashtable queryFilter) {
        this.queryFilter = queryFilter;
    }

    public Vector getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(Vector orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public boolean equals(Object o) {
        try {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TableAttribute)) {
                return false;
            }
            return ((TableAttribute) o).entity.equals(this.entity) && (this.hashCode() == o.hashCode());
        } catch (Exception ex) {
            TableAttribute.logger.error(null, ex);
            return false;
        }
    }

    @Override
    public int hashCode() {
        try {
            return this.entity.hashCode() + (this.keys != null ? this.keys.hashCode() : 0)
                    + (this.parentkeys != null ? this.parentkeys.hashCode() : 0);
        } catch (Exception ex) {
            TableAttribute.logger.error(null, ex);
            return -1;
        }
    }

    @Override
    public String toString() {
        return this.entity;
    }

}
