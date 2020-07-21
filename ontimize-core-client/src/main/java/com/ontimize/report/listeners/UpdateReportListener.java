package com.ontimize.report.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ontimize.report.DefaultReportDialog;

public class UpdateReportListener implements ActionListener {

    protected boolean processEvents = true;

    DefaultReportDialog reportDialog;

    public UpdateReportListener(DefaultReportDialog reportDialog) {
        this.reportDialog = reportDialog;
    }

    public boolean isProcessEvents() {
        return this.processEvents;
    }

    public void setProcessEvents(boolean processEvents) {
        this.processEvents = processEvents;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!this.processEvents) {
            return;
        }
        if (!this.reportDialog.isTable()) {
            this.reportDialog.updateReport(true);
        } else {
            this.reportDialog.updateReport(false);
        }
    }

};
