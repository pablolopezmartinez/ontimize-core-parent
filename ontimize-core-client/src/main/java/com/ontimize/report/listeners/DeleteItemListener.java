package com.ontimize.report.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.report.DefaultReportDialog;

public class DeleteItemListener implements ActionListener {

    protected String deleteKey = "REPORT_DELETE_KEY";

    protected ResourceBundle bundle = null;

    public DefaultReportDialog reportDialog;

    public DeleteItemListener(ResourceBundle resource, DefaultReportDialog reportDialog) {
        this.bundle = resource;
        this.reportDialog = reportDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o instanceof AbstractButton) {
            this.reportDialog.getConfMenu().setVisible(false);
            int i = JOptionPane.showConfirmDialog(this.reportDialog.getContainer(),
                    ApplicationManager.getTranslation(this.deleteKey, this.bundle), "", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.OK_OPTION) {
                String command = ((AbstractButton) o).getActionCommand();
                this.reportDialog.deleteConfiguration(command);
            }
        }
        this.reportDialog.getConfMenu().setVisible(false);
    }

}
