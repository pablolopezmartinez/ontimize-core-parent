package com.ontimize.report.columns;

import java.util.ArrayList;
import java.util.List;

public class OrderColumns {

    ArrayList columnNameList = new ArrayList();

    ArrayList orderColumn = new ArrayList();

    public void add(String column, String order) {
        this.columnNameList.add(column);
        this.orderColumn.add(order);
    }

    public List getColumnNameList() {
        return this.columnNameList;
    }

    public String getOrder(String column) {
        int index = this.columnNameList.indexOf(column);
        if (index < 0) {
            return null;
        }
        return (String) this.orderColumn.get(index);
    }

    public int size() {
        return this.columnNameList.size();
    }

}
