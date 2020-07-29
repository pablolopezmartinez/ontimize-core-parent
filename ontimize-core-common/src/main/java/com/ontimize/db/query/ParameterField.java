package com.ontimize.db.query;

import com.ontimize.db.SQLStatementBuilder;

public class ParameterField extends SQLStatementBuilder.BasicField {

    Object value = null;

    public ParameterField() {
        super("{Parameter}");
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

}
