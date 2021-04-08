package com.ontimize.report;

import java.util.EventObject;

public class ReportDesignerEvent extends EventObject {

    public static int SAVE = 0;

    public static int CLOSE = 1;

    private final int type;

    private final ReportName reportName;

    public ReportDesignerEvent(Object source, int type, ReportName report) {
        super(source);
        this.type = type;
        this.reportName = report;
    }

    public ReportName getReportName() {
        return this.reportName;
    }

    public int getType() {
        return this.type;
    }

}
