package com.ontimize.gui.field.spinner;

import java.util.List;

import javax.swing.SpinnerListModel;

public class CustomSpinnerListModel extends SpinnerListModel {

    protected int index = 0;

    public CustomSpinnerListModel(List values) {
        super(values);
    }

    public CustomSpinnerListModel(Object[] values) {
        super(values);
    }

    @Override
    public void setValue(Object value) {
        int index = this.getList().indexOf(value);
        if (index != this.index) {
            this.index = index;
            this.fireStateChanged();
        }
    }

    @Override
    public Object getValue() {
        if (this.index < 0) {
            return null;
        }
        return this.getList().get(this.index);
    }

    @Override
    public Object getNextValue() {
        if (this.index == -1) {
            return this.getList().get(0);
        }
        return this.index >= (this.getList().size() - 1) ? null : this.getList().get(this.index + 1);
    }

    @Override
    public Object getPreviousValue() {
        return this.index <= 0 ? null : this.getList().get(this.index - 1);
    }

}
