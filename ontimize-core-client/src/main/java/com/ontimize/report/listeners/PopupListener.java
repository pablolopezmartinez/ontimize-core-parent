package com.ontimize.report.listeners;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ontimize.report.DefaultReportDialog;
import com.ontimize.report.ReportUtils;
import com.ontimize.report.item.SelectableFunctionItem;

public class PopupListener extends MouseAdapter {

    DefaultReportDialog reportDialog;

    public PopupListener(DefaultReportDialog reportDialog) {
        this.reportDialog = reportDialog;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ((e.getClickCount() == 1) && (e.getModifiers() == InputEvent.META_MASK)) {
            // Check the selected item
            int x = e.getX();
            int y = e.getY();
            int index = this.reportDialog.getFunctionList().locationToIndex(e.getPoint());
            if (index >= 0) {
                this.reportDialog.setCurrentItem(
                        (SelectableFunctionItem) this.reportDialog.getFunctionList().getModel().getElementAt(index));
                if (this.reportDialog.getCurrentItem() == null) {
                    return;
                }

                int op = this.reportDialog.getCurrentItem().getOperation();
                if (op == ReportUtils.SUM) {
                    this.reportDialog.getSumOpMenu().setSelected(true);
                } else if (op == ReportUtils.AVG) {
                    this.reportDialog.getAverageOpMenu().setSelected(true);
                } else if (op == ReportUtils.MAX) {
                    this.reportDialog.getMaximumOpMenu().setSelected(true);
                } else if (op == ReportUtils.MIN) {
                    this.reportDialog.getMinimumOpMenu().setSelected(true);
                }
                this.reportDialog.getOperationTypePopup().setLabel(this.reportDialog.getCurrentItem().toString());
                this.reportDialog.getOperationTypePopup().show(this.reportDialog.getFunctionList(), x, y);
            }
        }
    }

}
