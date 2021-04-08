package com.ontimize.printing;

public class PageFooter {

    protected String text = "";

    protected int number = 0;

    public PageFooter(String footText) {
        this.text = footText;
    }

    public void setPageNumber(int pageNumber) {
        this.number = pageNumber;
    }

    public String toHTML() {
        return "<TABLE width='100%'>" + "<TR><TD>" + this.text + "</TD><TD align = 'right'>" + "Page "
                + Integer.toString(this.number) + "</TD></TR></TABLE>";
    }

}
