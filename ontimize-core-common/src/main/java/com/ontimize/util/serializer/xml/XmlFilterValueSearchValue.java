package com.ontimize.util.serializer.xml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "values", propOrder = { "uniqueValue", "multipleValues" })
@XmlRootElement
public class XmlFilterValueSearchValue {

    public static final String LIST_TYPE_NONE = "none";

    public static final String LIST_TYPE_List = "list";

    @XmlAttribute
    protected String listtype;

    @XmlElement(name = "unique-value")
    protected Object uniqueValue;

    @XmlElement(name = "multiple-values")
    protected List<Object> multipleValues;

    public XmlFilterValueSearchValue() {
        // TODO Auto-generated constructor stub
    }

    public XmlFilterValueSearchValue(Object value) {
        if (value instanceof List) {
            this.setListtype(XmlFilterValueSearchValue.LIST_TYPE_List);
            this.setMultipleValues((List<Object>) value);
        } else {
            this.setListtype(XmlFilterValueSearchValue.LIST_TYPE_NONE);
            this.setUniqueValue(value);
        }
    }

    public Object getUniqueValue() {
        return this.uniqueValue;
    }

    public void setUniqueValue(Object uniqueValue) {
        this.uniqueValue = uniqueValue;
    }

    public List<Object> getMultipleValues() {
        if (this.multipleValues == null) {
            this.multipleValues = new ArrayList<Object>();
        }
        return this.multipleValues;
    }

    public void setMultipleValues(List<Object> multipleValues) {
        if (this.multipleValues == null) {
            this.multipleValues = new ArrayList<Object>();
        }
        this.multipleValues.clear();
        this.multipleValues.addAll(multipleValues);
    }

    public String getListtype() {
        return this.listtype;
    }

    public void setListtype(String listtype) {
        this.listtype = listtype;
    }

    public Object getBaseValues() {
        if (this.getMultipleValues().isEmpty() && (this.getUniqueValue() != null)) {
            return this.getUniqueValue();
        } else if (!this.getMultipleValues().isEmpty() && (this.getUniqueValue() == null)) {
            if (this.getListtype().equalsIgnoreCase(XmlFilterValueSearchValue.LIST_TYPE_List)) {
                return new ArrayList(this.getMultipleValues());
            } else {
                return this.getMultipleValues();
            }
        }
        return null;
    }

}
