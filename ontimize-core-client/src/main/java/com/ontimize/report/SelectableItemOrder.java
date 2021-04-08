package com.ontimize.report;

public class SelectableItemOrder {

    private boolean ascending = true;

    public boolean isAscending() {
        return this.ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    private String text = null;

    public SelectableItemOrder(String text) {
        this.text = text;
        this.ascending = true;
    }

    public String getText() {
        return this.text;
    }

    public void setOrder(boolean ascendent) {
        this.ascending = ascendent;
    }

    public boolean getOrder() {
        return this.ascending;
    }

    @Override
    public String toString() {
        return this.text;
    }

    @Override
    public Object clone() {
        SelectableItemOrder sioNew = new SelectableItemOrder(this.text);
        sioNew.setOrder(this.ascending);
        return sioNew;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof SelectableItemOrder) && this.text.equals(((SelectableItemOrder) o).getText());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
