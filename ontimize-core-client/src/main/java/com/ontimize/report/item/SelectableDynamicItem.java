package com.ontimize.report.item;

import java.util.ResourceBundle;

public class SelectableDynamicItem extends com.ontimize.report.item.SelectableItem {

    public SelectableDynamicItem(String text, ResourceBundle res) {
        super(text, res);
    }

    boolean dynamic = false;

    public boolean isDynamic() {
        return this.dynamic;
    }

    public void setDynamic(boolean d) {
        this.dynamic = d;
    }

}
