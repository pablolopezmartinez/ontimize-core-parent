package com.ontimize.report.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import com.ontimize.report.DefaultReportDialog;

public class ItemListener implements ActionListener {

    DefaultReportDialog reportDialog;

    public ItemListener(DefaultReportDialog reportDialog) {
        this.reportDialog = reportDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o instanceof AbstractButton) {
            String command = ((AbstractButton) o).getActionCommand();
            this.reportDialog.loadConfiguration(command);
        }
        this.reportDialog.getConfMenu().setVisible(false);
    }

}
