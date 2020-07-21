package com.ontimize.util.swing.list;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class FilterListModel extends AbstractListModel implements IFilterListModel {

    protected int[] map = new int[0];

    protected String filter;

    protected List values = new ArrayList();

    public FilterListModel() {

    }

    public void setValue(Object values) {
        this.values = new ArrayList();
        this.applyFilter();
    }

    @Override
    public int getSize() {
        return this.map.length;
    }

    @Override
    public Object getElementAt(int index) {
        return this.values.get(this.map[index]);
    }

    @Override
    public void applyFilter(String filter) {
        this.filter = filter;
        this.applyFilter();
    }

    @Override
    public void resetFilter() {
        this.filter = null;
        this.map = this.evaluateFilter();
        this.fireContentsChanged(this, 0, this.map.length);
    }

    protected void applyFilter() {
        this.map = this.evaluateFilter();
        this.fireContentsChanged(this, 0, this.map.length);
    }

    protected int[] evaluateFilter() {
        if ((this.filter == null) || (this.filter.length() == 0) || this.values.isEmpty()) {
            int[] mapAux = new int[this.values.size()];
            for (int i = 0; i < mapAux.length; i++) {
                mapAux[i] = i;
            }
            return mapAux;
        }

        int matchsNumber = 0;
        int[] mapAux = new int[this.values.size()];
        for (int i = 0; i < this.values.size(); i++) {
            Object currentRecord = this.values.get(i);
            StringBuilder buffer = new StringBuilder();
            if (currentRecord.toString().toLowerCase().contains(this.filter.toLowerCase())) {
                mapAux[matchsNumber] = i;
                matchsNumber++;
            }
        }

        return this.packMap(mapAux, matchsNumber);
    }

    protected int[] packMap(int[] mapAll, int nMatchs) {
        int[] ma = new int[nMatchs];
        System.arraycopy(mapAll, 0, ma, 0, nMatchs);
        return ma;
    }

}
