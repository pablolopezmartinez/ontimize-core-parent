package com.ontimize.report.listeners;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ontimize.report.DefaultReportDialog;
import com.ontimize.report.ReportUtils;
import com.ontimize.report.engine.dynamicjasper.IGroupByDate;
import com.ontimize.report.item.SelectableDateGroupItem;

public class PopupGroupDateListener extends MouseAdapter {

    DefaultReportDialog reportDialog;

    public PopupGroupDateListener(DefaultReportDialog reportDialog) {
        this.reportDialog = reportDialog;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ((e.getClickCount() == 1) && (e.getModifiers() == InputEvent.META_MASK)
                && (this.reportDialog.getReportEngine() instanceof IGroupByDate)) {
            // Check the selected item
            int x = e.getX();
            int y = e.getY();
            int index = this.reportDialog.getGroupList().locationToIndex(e.getPoint());
            if (index >= 0) {
                if (!(this.reportDialog.getGroupList()
                    .getModel()
                    .getElementAt(index) instanceof SelectableDateGroupItem)) {
                    return;
                }
                this.reportDialog.setCurrentDateGroupItem(
                        (SelectableDateGroupItem) this.reportDialog.getGroupList().getModel().getElementAt(index));
                if (this.reportDialog.getCurrentDateGroupItem() == null) {
                    return;
                }

                int op = this.reportDialog.getCurrentDateGroupItem().getOperation();
                if (op == ReportUtils.GROUP_BY_DATE_TIME) {
                    this.reportDialog.getGroupByDateTimeMenuItem().setSelected(true);
                } else if (op == ReportUtils.GROUP_BY_DATE) {
                    this.reportDialog.getGroupByDateMenuItem().setSelected(true);
                } else if (op == ReportUtils.GROUP_BY_MONTH) {
                    this.reportDialog.getGroupByMonthMenuItem().setSelected(true);
                } else if (op == ReportUtils.GROUP_BY_QUARTER) {
                    this.reportDialog.getGroupByQuarterMenuItem().setSelected(true);
                } else if (op == ReportUtils.GROUP_BY_YEAR) {
                    this.reportDialog.getGroupByYearMenuItem().setSelected(true);
                }
                this.reportDialog.getGroupByDatePopup()
                    .setLabel(this.reportDialog.getCurrentDateGroupItem().toString());
                this.reportDialog.getGroupByDatePopup().show(this.reportDialog.getGroupList(), x, y);
            }
        }
    }

}
