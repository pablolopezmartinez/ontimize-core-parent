package com.ontimize.report.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ontimize.report.DefaultReportDialog;

public class OrderListener implements ActionListener {

    protected boolean proccessEvents = true;

    DefaultReportDialog reportDialog;

    public OrderListener(DefaultReportDialog reportDialog) {
        this.reportDialog = reportDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!this.proccessEvents) {
            return;
        }
        if (e.getSource() == this.reportDialog.getAscendingOpMenu()) {
            this.reportDialog.getDescendingOpMenu().setSelected(!this.reportDialog.getAscendingOpMenu().isSelected());
            this.reportDialog.updateReport();
        } else if (e.getSource() == this.reportDialog.getDescendingOpMenu()) {
            this.reportDialog.getAscendingOpMenu().setSelected(!this.reportDialog.getDescendingOpMenu().isSelected());
            this.reportDialog.updateReport();
        } else {
        }
    }

};
