package com.ontimize.report.item;

import java.util.Comparator;
import java.util.ResourceBundle;

import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.report.TranslatedItem;

public class SelectableItem extends TranslatedItem implements Internationalization, Comparator {

    protected boolean selected = false;

    public SelectableItem(String text, ResourceBundle res) {
        super(text, res);
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setResourceBundle(ResourceBundle res) {
        super.setResourceBundle(res);
    }

    public void setSelected(boolean sel) {
        this.selected = sel;
    }

    @Override
    public String toString() {
        if (!this.isSelected()) {
            return this.translatedText;
        }
        return this.translatedText;
    }

    public int compareTo(Object o) {
        if (!(o instanceof com.ontimize.report.item.SelectableItem)) {
            return -1;
        } else {
            SelectableItem item = (SelectableItem) o;
            return item.translatedText.compareTo(this.translatedText);
        }
    }

    @Override
    public int compare(Object o1, Object o2) {
        com.ontimize.report.item.SelectableItem item1 = (com.ontimize.report.item.SelectableItem) o1;
        com.ontimize.report.item.SelectableItem item2 = (com.ontimize.report.item.SelectableItem) o2;
        return item2.compareTo(item1);
    }

};
